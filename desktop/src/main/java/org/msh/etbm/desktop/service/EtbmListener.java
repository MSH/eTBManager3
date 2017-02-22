package org.msh.etbm.desktop.service;

/**
 * Created by rmemoria on 13/10/16.
 */
public interface EtbmListener {

    void onEtbmMessage(EtbmMessage type, String msg, int progress);
}
