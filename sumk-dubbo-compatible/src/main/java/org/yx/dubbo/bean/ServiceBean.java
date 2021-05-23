package org.yx.dubbo.bean;

import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.support.Parameter;

/**
 * @author : wjiajun
 * @description:
 */
public class ServiceBean<T> extends ServiceConfig<T> {

    private static final long serialVersionUID = 213195494150089726L;

    private final transient Service service;

    private final transient com.alibaba.dubbo.config.annotation.Service oldService;

    private final transient DubboService dubboService;

    private transient String beanName;

    public ServiceBean() {
        super();
        this.service = null;
        this.oldService = null;
        this.dubboService = null;
    }

    public ServiceBean(Service service) {
        super(service);
        this.oldService = null;
        this.service = service;
        this.dubboService = null;
    }

    public ServiceBean(com.alibaba.dubbo.config.annotation.Service service) {
        super();
        this.oldService = service;
        this.service = null;
        this.dubboService = null;
        appendAnnotation(com.alibaba.dubbo.config.annotation.Service.class, service);
    }

    public ServiceBean(DubboService service) {
        super();
        this.oldService = null;
        this.service = null;
        this.dubboService = service;
        appendAnnotation(DubboService.class, service);
        setMethods(MethodConfig.constructMethodConfig(service.methods()));
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

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

    @Parameter(excluded = true)
    public String getBeanName() {
        return this.beanName;
    }

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
