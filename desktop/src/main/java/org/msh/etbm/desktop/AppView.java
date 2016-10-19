package org.msh.etbm.desktop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rmemoria on 18/10/16.
 */
public class AppView implements Initializable {

    @FXML
    private WebView webView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = webView.getEngine();
        webEngine.load("http://localhost:8080/");
    }

    @FXML
    protected void btnBackAction(ActionEvent evt) {
        webView.getEngine().executeScript("history.back()");
    }

    @FXML
    protected void btnForwardAction(ActionEvent evt) {
        webView.getEngine().executeScript("history.forward()");
    }

    @FXML
    protected void btnRefreshAction(ActionEvent evt) {
        webView.getEngine().executeScript("location.reload()");
    }

}