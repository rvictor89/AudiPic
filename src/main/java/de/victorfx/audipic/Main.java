package de.victorfx.audipic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * @author Ramon Victor Mai 2016.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.UNIFIED);
        URL fxml = getClass().getResource("fxml/audipic.fxml");
        Parent fxplayer = FXMLLoader.load(fxml);
        Scene root = new Scene(fxplayer);
        primaryStage.setScene(root);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
