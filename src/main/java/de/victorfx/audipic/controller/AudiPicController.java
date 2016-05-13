package de.victorfx.audipic.controller;

import de.victorfx.audipic.painter.IPainter;
import de.victorfx.audipic.painter.PainterLineTwo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Ramon Victor Mai 2016.
 */
public class AudiPicController implements Initializable {
    public Canvas canvas;
    public Pane canvasPane;
    public VBox settingsBox;
    private FileChooser fc;
    private MediaPlayer mediaPlayer;
    private GraphicsContext context;
    private List<IPainter> painters = new ArrayList<>();
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int width = device.getDisplayMode().getWidth() - (int) settingsBox.getMinWidth();
        int height = device.getDisplayMode().getHeight();
        canvas.setWidth(width);
        canvas.setHeight(height);
        context = canvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        context.setFill(Color.BLACK);

        IPainter painter1 = new PainterLineTwo();
        painters.add(painter1);
        IPainter painter2 = new PainterLineTwo();
        painters.add(painter2);
        IPainter painter3 = new PainterLineTwo();
        painters.add(painter3);
        IPainter painter4 = new PainterLineTwo();
        painters.add(painter4);
        IPainter painter5 = new PainterLineTwo();
        painters.add(painter5);
        IPainter painter6 = new PainterLineTwo();
        painters.add(painter6);
        IPainter painter7 = new PainterLineTwo();
        painters.add(painter7);
        IPainter painter8 = new PainterLineTwo();
        painters.add(painter8);
        //painters.add(new PainterLineTwo());
        //painters.add(new PainterCurve());
        // painters.add(new PainterOval());

        for (int i = 0; i < painters.size(); i++) {
            Canvas canvas = new Canvas();
            canvas.setHeight(height);
            canvas.setWidth(width);
            canvasPane.getChildren().add(canvas);
            painters.get(painters.size() - 1 - i).setGraphicContextForMagic(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
        }


//        for (IPainter painter : painters) {
//            Canvas canvas = new Canvas();
//            canvas.setHeight(height);
//            canvas.setWidth(width);
//            canvasPane.getChildren().add(canvas);
//            painter.setGraphicContextForMagic(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
//        }

        painter1.setColor(Color.YELLOW);
        painter2.setColor(Color.GREEN);
        painter3.setColor(Color.RED);
        painter4.setColor(Color.GREY);
        painter5.setColor(Color.VIOLET);
        painter6.setColor(Color.BLUE);
        painter7.setColor(Color.BLACK);
        painter8.setColor(Color.ORANGE);
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
            mediaPlayer.setAudioSpectrumInterval(0.1);
            mediaPlayer.setAudioSpectrumNumBands(painters.size());
            mediaPlayer.setAudioSpectrumThreshold(-100);
            mediaPlayer.setAutoPlay(true);
        }
    }

    /**
     * Intern AudioSpectrumListener for the audio visualization.
     */
    private class SpektrumListener implements AudioSpectrumListener {
        @Override
        public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
            for (int i = 0; i < painters.size(); i++) {
                painters.get(i).paintMagic(timestamp, duration, magnitudes[i], phases[i]);
            }
        }
    }
}
