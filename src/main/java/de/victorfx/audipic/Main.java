package de.victorfx.audipic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * @author Ramon Victor Mai 2016.
 */
public class Main extends Application {

    public static final String VERSION = "1.2-SNAPSHOT";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.DECORATED);
        addFavIcons(primaryStage);
        primaryStage.setTitle("AudiPic v." + VERSION + " created by Nico Bastian and Ramon Victor, B.Sc.");
        URL fxml = getClass().getResource("fxml/audipic.fxml");
        Parent fxplayer = FXMLLoader.load(fxml);
        Scene root = new Scene(fxplayer);
        primaryStage.setScene(root);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void addFavIcons(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(this.getClass().getResource("images/logo/AudiPic_16.png").toString()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource("images/logo/AudiPic_32.png").toString()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource("images/logo/AudiPic_64.png").toString()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource("images/logo/AudiPic_128.png").toString()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource("images/logo/AudiPic_256.png").toString()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource("images/logo/AudiPic_512.png").toString()));
    }
}
