package org.msh.etbm.services.sys.info;

/**
 * Created by rmemoria on 30/8/15.
 */
public class JarManifest {

    /**
     * e-TB Manager implementation version
     */
    private String implementationVersion;

    /**
     * Title of the implementation version
     */
    private String implementationTitle;

    /**
     * Title of the implementation version
     */
    private String implementationVendor;

    /**
     * e-TB Manager version build date and time (format yyyy-mm-dd_hh:mm)
     */
    private String buildTime;

    /**
     * e-TB Manager build number (ex.: 342)
     */
    private String buildNumber;

    /**
     * Java version and build used to generate this version of e-TB Manager
     */
    private String javaBuildVersion;

    /**
     * Name of the user that built this version of e-TB Manager
     */
    private String builtBy;




    public String getImplementationVersion() {
        return implementationVersion;
    }

    public void setImplementationVersion(String implementationVersion) {
        this.implementationVersion = implementationVersion;
    }

    public String getImplementationTitle() {
        return implementationTitle;
    }

    public void setImplementationTitle(String implementationTitle) {
        this.implementationTitle = implementationTitle;
    }

    public String getImplementationVendor() {
        return implementationVendor;
    }

    public void setImplementationVendor(String implementationVendor) {
        this.implementationVendor = implementationVendor;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getJavaBuildVersion() {
        return javaBuildVersion;
    }

    public void setJavaBuildVersion(String javaBuildVersion) {
        this.javaBuildVersion = javaBuildVersion;
    }

    public String getBuiltBy() {
        return builtBy;
    }

    public void setBuiltBy(String builtBy) {
        this.builtBy = builtBy;
    }
}
