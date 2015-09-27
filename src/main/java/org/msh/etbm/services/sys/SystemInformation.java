package org.msh.etbm.services.sys;

import org.msh.etbm.commons.Item;

import java.util.List;

/**
 * Store information about the system, like status, build version and number and available languages
 *
 * Created by rmemoria on 16/8/15.
 */
public class SystemInformation {

    /**
     * Store the state of the system
     */
    private SystemState state;

    private List<Item<String>> languages;

    private JarManifest system;


    public SystemState getState() {
        return state;
    }

    public void setState(SystemState state) {
        this.state = state;
    }

    public List<Item<String>> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Item<String>> languages) {
        this.languages = languages;
    }

    public JarManifest getSystem() {
        return system;
    }

    public void setSystem(JarManifest system) {
        this.system = system;
    }
}
