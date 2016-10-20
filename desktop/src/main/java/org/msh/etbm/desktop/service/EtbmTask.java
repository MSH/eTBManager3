package org.msh.etbm.desktop.service;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.msh.etbm.desktop.Configuration;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by rmemoria on 13/10/16.
 */
public class EtbmTask extends Task<Void> {

    public static final String ETBM_JAR = "etbmanager-2.9.2.jar";

    private Process proc;

    private EtbmListener listener;

    private PrintWriter printWriter;

    public EtbmTask(EtbmListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void call() throws Exception {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
            getPrintWriter().print(e.getMessage());
            notifyMessage(EtbmMessage.ERROR, e.getMessage(), 0);
        }

        return null;
    }


    protected void start() throws Exception {
        checkJava();
        Configuration cfg = Configuration.instance();

        String java = isWindows() ? "bin/java.exe" : "bin/java";
        File jvm = new File(cfg.getJvmDirectory(), java);

        if (!jvm.exists()) {
            throw new RuntimeException("JVM not found in " + jvm);
        }

        addLog("working dir = " + cfg.getWorkingDirectory());
        File etbm = new File(cfg.getWorkingDirectory(), ETBM_JAR);
        if (!etbm.exists()) {
            throw new RuntimeException(ETBM_JAR + " not found");
        }

        addLog("JVM = " + jvm.toString());
        String[] cmd = { jvm.toString(), "-jar", ETBM_JAR };

        Runtime rt = Runtime.getRuntime();

        notifyMessage(EtbmMessage.STARTING, "Executing web version", 0);

        proc = rt.exec(cmd, null, new File(cfg.getWorkingDirectory()));

        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        String s = null;
        while ((s = in.readLine()) != null) {
            System.out.println(s);
            checkStartupStatus(s);
        }

        notifyMessage(EtbmMessage.STOPPING, null, 100);

        // read any errors from the attempted command
        try {
            while ((s = error.readLine()) != null) {
                String log = "[ERROR] " + s;
                System.out.println(log);
            }
        } catch (IOException e) {
            // error of stream already closed, simply ignore it
        }

        getPrintWriter().println("Finishing e-TB Manager web");
        getPrintWriter().close();
    }

    protected void addLog(String log) {
        getPrintWriter().println(log);
        getPrintWriter().flush();
    }

    /**
     * Return the instance of PrinterWriter that will write the log to a file
     * etbmanager.log in the local directory
     * @return
     */
    protected PrintWriter getPrintWriter() {
        if (printWriter == null) {
            String dir = Configuration.instance().getWorkingDirectory();
            File file = new File(dir, "etbmanager.log");
            try {
                printWriter = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return printWriter;
    }


    public void stop() {
        notifyMessage(EtbmMessage.STOPPING, "Finishing process", 0);
        proc.destroy();

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyMessage(EtbmMessage type, String msg, int prog) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                listener.onEtbmMessage(type, msg, prog);
            }
        });
    }


    protected void checkStartupStatus(String s) {
        // 1. check web app initialized
        if (s.contains("Root WebApplicationContext")) {
            notifyMessage(EtbmMessage.STARTING, "Initializing database", 20);
        }

        if (s.contains("checkpointClose end")) {
            notifyMessage(EtbmMessage.STARTING, "Initializing database", 35);
        }

        if (s.contains("Building JPA container EntityManagerFactory")) {
            notifyMessage(EtbmMessage.STARTING, "Initializing table mapping", 50);
        }

        if (s.contains("Registering beans for JMX")) {
            notifyMessage(EtbmMessage.STARTING, "Finishing initialization", 75);
        }

        if (s.contains("Started Application")) {
            notifyMessage(EtbmMessage.STARTING, "Application started", 100);
        }

        if (s.contains("Application failed to start")) {
            notifyMessage(EtbmMessage.ERROR, "Application failed to start. Check log file", 0);
        }
    }

    /**
     * Return true if Operating System is Windows (ARGH!)
     */
    protected boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }

    protected void checkJava() throws Exception {
        Configuration cfg = Configuration.instance();

        addLog("checking Java...");
        if (isWindows()) {
            File javaExe = new File(cfg.getWorkingDirectory(), "java.exe");
            File dest = new File(cfg.getJvmDirectory(), "bin/java.exe");
            addLog("Source = " + javaExe.toString());
            addLog("Dest = " + dest.toString());
            if (dest.exists()) {
                return;
            }

            Files.copy(javaExe.toPath(), dest.toPath());
        }
    }
}
