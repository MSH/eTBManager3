package org.msh.etbm.desktop;

/**
 * Created by rmemoria on 12/10/16.
 */
public class Configuration {

    private static final Configuration _instance = new Configuration();

    /**
     * The working directory where the e-TB Manager is located
     */
    private String workingDirectory;

    /**
     * The directory of the JVM
     */
    private String jvmDirectory;

    private Configuration() {

    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }


    public static Configuration instance() {
        return _instance;
    }

    public String getJvmDirectory() {
        return jvmDirectory;
    }

    public void setJvmDirectory(String jvmDirectory) {
        this.jvmDirectory = jvmDirectory;
    }
}
