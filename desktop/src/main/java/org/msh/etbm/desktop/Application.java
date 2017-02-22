package org.msh.etbm.desktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.msh.etbm.desktop.event.AppEvent;
import org.msh.etbm.desktop.event.EventListener;
import org.msh.etbm.desktop.event.EventService;
import org.msh.etbm.desktop.service.EtbmListener;
import org.msh.etbm.desktop.service.EtbmMessage;
import org.msh.etbm.desktop.service.EtbmService;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This is the main class for starting up e-TB Manager in a desktop mode
 *
 * Created by rmemoria on 12/10/16.
 */
public class Application extends javafx.application.Application implements EventListener {

    private Stage stage;

    public static void main(String[] args) {
        Configuration cfg = ConfigurationReader.read(args);
        launch(Application.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        EventService.addListener(this);

        primaryStage.setTitle("eTB Manager 3 - Desktop");

        primaryStage.setOnCloseRequest(evt -> EtbmService.instance().stop());

        Parent parent = FXMLLoader.load(getClass().getResource("StartupView.fxml"));
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void onEvent(Object event, Object data) {
        if (event == AppEvent.SHOW_APP) {
            openApp();
        }
    }

    /**
     * Called after the initialization of the web version
     */
    protected void openApp() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("AppView.fxml"));
            parent.setId("background");
            Scene scene = new Scene(parent);
            scene.getStylesheets().add("app.css");

            stage.setScene(scene);

            stage.setX(100);
            stage.setY(50);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
