package org.msh.etbm.desktop;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
public class Application extends javafx.application.Application implements EtbmListener {

    private Button btn;

    public static void main(String[] args) {
        Configuration cfg = ConfigurationReader.read(args);
        launch(Application.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("eTB Manager");
        btn = new Button();
        btn.setText("Start eTB Manager");
        btn.setOnAction(evt -> startApp());

        primaryStage.setOnCloseRequest(evt -> EtbmService.instance().stop());

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public void startApp() {
        EtbmService service = EtbmService.instance();

        if (service.isStarted()) {
            service.stop();
        } else {
            service.start(this);
        }
    }

    @Override
    public void onEtbmMessage(EtbmMessage type, String msg) {
        switch (type) {
            case STARTING:
                btn.setDisable(true);
                btn.setText("Starting eTB Manager");
                break;
            case STARTED:
                btn.setDisable(false);
                btn.setText("Stop eTB Manager");
                try {
                    Desktop.getDesktop().browse(new URL("http://localhost:8080").toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case STOPPED:
                btn.setDisable(false);
                btn.setText("Start eTB Manager");
                break;
            case STOPPING:
                btn.setDisable(true);
                break;
        }
    }
}
