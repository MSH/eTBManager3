package org.msh.etbm.desktop.service;

/**
 * Service responsible for controlling the execution of e-TB Manager
 *
 * Created by rmemoria on 12/10/16.
 */
public class EtbmService {

    private static final EtbmService _instance = new EtbmService();

    private Thread thread;
    private EtbmTask task;

    /**
     * Constructor just initialized locally
     */
    private EtbmService() {

    }

    public void start(EtbmListener listener) {
        if (thread != null && thread.isAlive()) {
            return;
        }

        task = new EtbmTask(listener);

        thread = new Thread(task);
        thread.start();
    }


    public void stop() {
        if (thread == null || !thread.isAlive()) {
            return;
        }

        task.stop();
        if (thread.isAlive()) {
            thread.interrupt();
        }

        task = null;
        thread = null;
    }

    public boolean isStarted() {
        return thread != null;
    }

    public static EtbmService instance() {
        return _instance;
    }
}
