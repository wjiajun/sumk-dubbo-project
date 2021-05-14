package org.yx.dubbo.bean;

import org.apache.dubbo.config.AbstractInterfaceConfig;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ModuleConfig;
import org.apache.dubbo.config.MonitorConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.yx.bean.IOC;
import org.yx.dubbo.ioc.BeanRegistry;
import org.yx.dubbo.spec.DubboBeanSpec;

import java.util.List;

public abstract class AnnotatedInterfaceConfigBeanBuilder<C extends AbstractInterfaceConfig> {

    protected final DubboBeanSpec dubboBeanSpec;

    protected Object configBean;

    protected Class<?> interfaceClass;

    protected AnnotatedInterfaceConfigBeanBuilder(DubboBeanSpec dubboBeanSpec) {
        this.dubboBeanSpec = dubboBeanSpec;
    }

    /**
     * Build {@link C}
     *
     * @return non-null
     * @throws Exception
     */
    public final C build() throws Exception {

        checkDependencies();

        C configBean = doBuild();

        configureBean(configBean);

        return configBean;

    }

    private void checkDependencies() {

    }

    /**
     * Builds {@link C Bean}
     *
     * @return {@link C Bean}
     */
    protected abstract C doBuild();


    protected void configureBean(C configBean) throws Exception {

        preConfigureBean(dubboBeanSpec, configBean);

        configureRegistryConfigs(configBean);

        configureMonitorConfig(configBean);

        configureApplicationConfig(configBean);

        configureModuleConfig(configBean);

        postConfigureBean(dubboBeanSpec, configBean);

    }

    protected abstract void preConfigureBean(DubboBeanSpec dubboBeanSpec, C configBean) throws Exception;


    private void configureRegistryConfigs(C configBean) {

        String[] registryConfigBeanIds = resolveRegistryConfigBeanNames(dubboBeanSpec);

        List<RegistryConfig> registryConfigs = BeanRegistry.getBeans(registryConfigBeanIds, RegistryConfig.class);

        configBean.setRegistries(registryConfigs);

    }

    private void configureMonitorConfig(C configBean) {

        String monitorBeanName = resolveMonitorConfigBeanName(dubboBeanSpec);

        MonitorConfig monitorConfig = IOC.get(monitorBeanName, MonitorConfig.class);

        configBean.setMonitor(monitorConfig);

    }

    private void configureApplicationConfig(C configBean) {

        String applicationConfigBeanName = resolveApplicationConfigBeanName(dubboBeanSpec);

        ApplicationConfig applicationConfig = IOC.get(applicationConfigBeanName, ApplicationConfig.class);

        configBean.setApplication(applicationConfig);

    }

    private void configureModuleConfig(C configBean) {

        String moduleConfigBeanName = resolveModuleConfigBeanName(dubboBeanSpec);

        ModuleConfig moduleConfig = IOC.get(moduleConfigBeanName, ModuleConfig.class);

        configBean.setModule(moduleConfig);

    }

    /**
     * Resolves the configBean name of ModuleConfig
     *
     * @param dubboBeanSpec
     * @return
     */
    protected abstract String resolveModuleConfigBeanName(DubboBeanSpec dubboBeanSpec);

    /**
     * Resolves the configBean name of ApplicationConfig
     *
     * @param dubboBeanSpec
     * @return
     */
    protected abstract String resolveApplicationConfigBeanName(DubboBeanSpec dubboBeanSpec);


    /**
     * Resolves the configBean ids of RegistryConfig
     *
     * @param dubboBeanSpec
     * @return non-empty array
     */
    protected abstract String[] resolveRegistryConfigBeanNames(DubboBeanSpec dubboBeanSpec);

    /**
     * Resolves the configBean name of MonitorConfig
     *
     * @param dubboBeanSpec
     * @return
     */
    protected abstract String resolveMonitorConfigBeanName(DubboBeanSpec dubboBeanSpec);

    /**
     * Configures Bean
     *
     * @param dubboBeanSpec
     * @param configBean
     */
    protected abstract void postConfigureBean(DubboBeanSpec dubboBeanSpec, C configBean) throws Exception;


    public <T extends AnnotatedInterfaceConfigBeanBuilder<C>> T configBean(Object configBean) {
        this.configBean = configBean;
        return (T) this;
    }

    public <T extends AnnotatedInterfaceConfigBeanBuilder<C>> T interfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        return (T) this;
    }
}