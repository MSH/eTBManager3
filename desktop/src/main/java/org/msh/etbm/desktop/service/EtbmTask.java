package org.msh.etbm.desktop.service;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.msh.etbm.desktop.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by rmemoria on 13/10/16.
 */
public class EtbmTask extends Task<Void> {

    public static final String ETBM_JAR = "etbmanager-2.9.2.jar";

    private Process proc;

    private EtbmListener listener;

    public EtbmTask(EtbmListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void call() throws Exception {
        Configuration cfg = Configuration.instance();

        File jvm = new File(cfg.getJvmDirectory(), "bin/java");

        if (!jvm.exists()) {
            throw new RuntimeException("JVM not found in " + jvm);
        }

        File etbm = new File(cfg.getWorkingDirectory(), ETBM_JAR);
        if (!etbm.exists()) {
            throw new RuntimeException(ETBM_JAR + " not found");
        }

        String[] cmd = { jvm.toString(), "-jar", ETBM_JAR };

        Runtime rt = Runtime.getRuntime();

        notifyMessage(EtbmMessage.STARTING);

        proc = rt.exec(cmd, null, new File(cfg.getWorkingDirectory()));

        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        String s = null;
        while ((s = in.readLine()) != null) {
            System.out.println(s);
            checkStarted(s);
        }

        notifyMessage(EtbmMessage.STOPPED);

        // read any errors from the attempted command
        while ((s = error.readLine()) != null) {
            System.out.println("[ERROR] " + s);
        }

        return null;
    }

    public void stop() {
        notifyMessage(EtbmMessage.STOPPING);
        proc.destroy();
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyMessage(EtbmMessage type) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                listener.onEtbmMessage(type, null);
            }
        });
    }


    protected void checkStarted(String s) {
        if (s.contains("Started Application")) {
            notifyMessage(EtbmMessage.STARTED);
        }
    }
}
