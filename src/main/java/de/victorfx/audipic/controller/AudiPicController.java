package de.victorfx.audipic.controller;

import de.victorfx.audipic.painter.IPainter;
import de.victorfx.audipic.painter.PainterTest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ramon Victor Mai 2016.
 */
public class AudiPicController implements Initializable {
    public Canvas canvas;
    public FileChooser fc;
    public BorderPane borderPane;
    private MediaPlayer mediaPlayer;
    private GraphicsContext context;
    private double lastX = 0;
    private double lastY = 0;
    private double vorzeichen = 1;
    private IPainter painter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        context = canvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        context.setFill(Color.BLACK);
        context.beginPath();
        lastX = canvas.getWidth()/2;
        lastY = canvas.getHeight()/2;
        context.moveTo(lastX, lastY);
        painter = new PainterTest();
        painter.setGraphicContextForMagic(context, canvas.getWidth(), canvas.getHeight());
    }

    @FXML
    private void openAudio() {
        fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV", "*.wav"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4", "*.mp4"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("FLV", "*.flv"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            String songpath = file.getAbsolutePath().replace("\\", "/");
            Media media = new Media(new File(songpath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAudioSpectrumListener(new SpektrumListener());
            mediaPlayer.setAudioSpectrumInterval(1);
            mediaPlayer.setAudioSpectrumNumBands(1);
            mediaPlayer.setAutoPlay(true);
        }
    }

    /**
     * Intern AudioSpectrumListener for the audio visualization.
     */
    private class SpektrumListener implements AudioSpectrumListener {
        @Override
        public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
           painter.paintMagic(timestamp, duration, magnitudes[0], phases[0]);
        }
    }
}
