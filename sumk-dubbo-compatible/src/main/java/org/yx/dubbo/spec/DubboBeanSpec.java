package org.yx.dubbo.spec;

/**
 * @author : wjiajun
 * @description:
 */
public class DubboBeanSpec {

    private Class<?> interfaceClass;

    private String interfaceName;

    private String version;

    private String group;

    private String application;

    public DubboBeanSpec(Class<?> interfaceClass, String interfaceName, String version, String group, String application) {
        this.interfaceClass = interfaceClass;
        this.interfaceName = interfaceName;
        this.version = version;
        this.group = group;
        this.application = application;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
