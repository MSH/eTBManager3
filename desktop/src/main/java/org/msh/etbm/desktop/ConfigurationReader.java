package org.msh.etbm.desktop;

/**
 * Read the configuration from the command line arguments
 *
 * Created by rmemoria on 12/10/16.
 */
public class ConfigurationReader {

    public static Configuration read(String[] args) {
        Configuration cfg = Configuration.instance();

        cfg.setJvmDirectory(System.getProperty("java.home"));

        for (String arg: args) {
            String[] p = arg.split("=");

            if (p.length != 2) {
                continue;
            }

            if (p[0].equals("-workingDir")) {
                cfg.setWorkingDirectory(p[1]);
                System.out.println("working directory = " + p[1]);
            }
        }

        return cfg;
    }
}
