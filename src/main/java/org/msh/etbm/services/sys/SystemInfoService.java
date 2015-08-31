package org.msh.etbm.services.sys;

import org.msh.etbm.commons.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by rmemoria on 16/8/15.
 */
@Service
public class SystemInfoService {

    /**
     * List of available languages supported by the system
     */
    @Value("${app.languages}")
    private String[] languages;


    /**
     * Store information about the manifest.md file
     */
    private JarManifest jarManifest;

    /**
     * Return information about the system
     * @return
     */
    public SystemInformation getInformation() {
        SystemInformation inf = new SystemInformation();

        inf.setState(SystemInformation.SystemState.NEW);

        inf.setLanguages(getLanguages());

        inf.setSystem(getJarManifest());

        return inf;
    }


    /**
     * Return the list of available languages, with its display name
     * @return list of Item objects
     */
    protected List<Item<String>> getLanguages() {
        // create the list of languages
        List<Item<String>> lst = new ArrayList<Item<String>>();
        for (String lang: languages) {
            Locale loc;
            String[] opts = lang.split("_");
            switch (opts.length) {
                case 2: loc = new Locale(opts[0], opts[1]);
                    break;
                case 3: loc = new Locale(opts[0], opts[1], opts[2]);
                    break;
                default: loc = new Locale(opts[0]);
            }
            lst.add(new Item<String>(lang, loc.getDisplayName()));
        }
        return lst;
    }


    /**
     * Load information from the MANIFEST.MF file in the META-INF folder
     */
    protected JarManifest getJarManifest() {
        if (jarManifest != null) {
            return jarManifest;
        }

        jarManifest = new JarManifest();

        Manifest manifest = getManifest();

        if (manifest != null) {
            Attributes attr = manifest.getMainAttributes();
            jarManifest.setBuildTime(attr.getValue("Build-Time"));
            jarManifest.setImplementationVendor(attr.getValue("Implementation-Vendor"));
            jarManifest.setImplementationVersion(attr.getValue("Implementation-Version"));
            jarManifest.setImplementationTitle(attr.getValue("Implementation-Title"));
            jarManifest.setBuiltBy(attr.getValue("Built-By"));
            jarManifest.setBuildNumber(attr.getValue("Implementation-Build"));
            jarManifest.setJavaBuildVersion(attr.getValue("Build-Jdk"));
        }

        return jarManifest;
    }

    /**
     * Get the manifest file declared in the application
     * @return instance of Manifest class
     */
    private Manifest getManifest() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString() + JarFile.MANIFEST_NAME;

        Enumeration resEnum;
        try {
            resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (resEnum.hasMoreElements()) {
                try {
                    URL url = (URL)resEnum.nextElement();
                    if (url.toString().equals(path)) {
                        InputStream is = url.openStream();
                        if (is != null) {
                            Manifest manifest = new Manifest(is);
                            return manifest;
                        }
                    }
                }
                catch (Exception e) {
                    // Silently ignore wrong manifests on classpath?
                }
            }
        } catch (IOException e1) {
            // Silently ignore wrong manifests on classpath?
        }
        return null;
    }

}
