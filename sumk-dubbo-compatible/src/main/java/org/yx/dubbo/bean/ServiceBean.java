package org.yx.dubbo.bean;

import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.support.Parameter;

/**
 * @author : wjiajun
 * @description:
 */
public class ServiceBean<T> extends ServiceConfig<T> {

    private static final long serialVersionUID = 213195494150089726L;

    private final transient Service service;

    private transient String beanName;

    public ServiceBean() {
        super();
        this.service = null;
    }

    public ServiceBean(Service service) {
        super(service);
        this.service = service;
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * Gets associated {@link Service}
     *
     * @return associated {@link Service}
     */
    public Service getService() {
        return service;
    }

    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(getPath())) {
            if (StringUtils.isNotEmpty(getInterface())) {
                setPath(getInterface());
            }
        }
    }

    /**
     * Get the name of {@link org.apache.dubbo.config.spring.ServiceBean}
     *
     * @return {@link org.apache.dubbo.config.spring.ServiceBean}'s name
     * @since 2.6.5
     */
    @Parameter(excluded = true)
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * @since 2.6.5
     */
    @Override
    public void exported() {
        super.exported();
        // Publish ServiceBeanExportedEvent
    }


    // merged from dubbox
    @Override
    protected Class getServiceClass(T ref) {
        return super.getServiceClass(ref);
    }

}
