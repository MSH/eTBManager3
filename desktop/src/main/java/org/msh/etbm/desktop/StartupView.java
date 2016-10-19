package org.msh.etbm.desktop;

import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.msh.etbm.desktop.event.AppEvent;
import org.msh.etbm.desktop.event.EventService;
import org.msh.etbm.desktop.service.EtbmListener;
import org.msh.etbm.desktop.service.EtbmMessage;
import org.msh.etbm.desktop.service.EtbmService;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rmemoria on 16/10/16.
 */
public class StartupView implements EtbmListener, Initializable {

    @FXML
    private Button btnStart;

    @FXML
    private Label txtStatus;

    @FXML
    private ProgressBar pbStarting;

    @FXML
    private ImageView logo;

    @FXML
    protected void btnStartAction(ActionEvent evt) {
        EtbmService service = EtbmService.instance();

        showProgress(true);
        if (service.isStarted()) {
            service.stop();
        } else {
            service.start(this);
        }
    }

    @Override
    public void onEtbmMessage(EtbmMessage type, String msg, int progress) {
        switch (type) {
            case STARTING:
                handleStarting(msg, progress);
                break;

            case STOPPING:
                handleStoping(msg, progress);
                break;

            case ERROR:
                btnStart.setDisable(false);
                showError(msg);
                break;
        }
    }

    protected void handleStarting(String msg, int progress) {
        txtStatus.setText(msg);
        pbStarting.setProgress((double)progress / 100.0);

        if (progress == 100) {
            btnStart.setDisable(false);
            btnStart.setText("Stop e-TB Manager");
            showProgress(false);
            openApp();
        } else {
            btnStart.setDisable(true);
            btnStart.setText("Starting e-TB Manager");
        }
    }

    protected void handleStoping(String msg, int progress) {
        if (progress == 100) {
            btnStart.setText("Start e-TB Manager");
            btnStart.setDisable(false);
            showProgress(false);
        } else {
            btnStart.setDisable(true);
            btnStart.setText("Stopping e-TB Manager");
        }

        txtStatus.setText(msg);
        pbStarting.setProgress((double)progress / 100.0);
    }

    protected void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }


    protected void openApp() {
        EventService.raise(AppEvent.SHOW_APP, null);
//        try {
//            Desktop.getDesktop().browse(new URL("http://localhost:8080").toURI());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Show the progress bar control and its text status
     * @param show
     */
    protected void showProgress(boolean show) {
        pbStarting.setVisible(show);
        txtStatus.setVisible(show);
        txtStatus.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProgress(false);
        Image imgLogo = new Image(StartupView.class.getResourceAsStream("/etbm_icon_128x128.png"));
        logo.setImage(imgLogo);
    }
}
