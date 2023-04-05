package org.itstack.demo.design;

/**
 * 记录者类
 */
public class ConfigOriginator {

    private ConfigFile configFile;

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }

    public ConfigMemento saveMemento(){
        return new ConfigMemento(configFile);
    }

    public void getMemento(ConfigMemento memento){
        this.configFile = memento.getConfigFile();
    }

}
