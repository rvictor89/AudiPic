package de.victorfx.audipic.controller;

import de.victorfx.audipic.painter.IPainter;
import de.victorfx.audipic.painter.PainterLineTwo;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Ramon Victor Mai 2016.
 */
public class AudiPicController implements Initializable {
    public static final double SPECTRUM_INTERVAL = 0.1;
    public Canvas canvas;
    public Pane canvasPane;
    public VBox settingsBox;
    public Label durationLabel;
    private String duration;
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

        for (int i = 0; i < painters.size(); i++) {
            Canvas canvas = new Canvas();
            canvas.setHeight(height);
            canvas.setWidth(width);
            canvasPane.getChildren().add(canvas);
            painters.get(painters.size() - 1 - i).setGraphicContextForMagic(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
        }

        painter1.setColor(Color.DARKRED);
        painter2.setColor(Color.RED);
        painter3.setColor(Color.ORANGERED);
        painter4.setColor(Color.ORANGE);
        painter5.setColor(Color.YELLOW);
        painter6.setColor(Color.YELLOWGREEN);
        painter7.setColor(Color.GREEN);
        painter8.setColor(Color.LIGHTBLUE);
    }

    @FXML
    private void openAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
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
            mediaPlayer.setAudioSpectrumInterval(SPECTRUM_INTERVAL);
            mediaPlayer.setAudioSpectrumNumBands(painters.size());
            mediaPlayer.setAudioSpectrumThreshold(-100);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnReady(() -> durationLabel.setText("-" + ((int) mediaPlayer.getTotalDuration().toMinutes() % 60) + ":" + ((int) mediaPlayer.getTotalDuration().toSeconds() % 60) + "min"));
            mediaPlayer.setOnEndOfMedia(() -> {
                WritableImage image = canvasPane.snapshot(null, null);
                BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
                try {
                    //ImageIO.write(bImage, "png", new File("PicTest" + new Date().getTime() + ".png"));
                    ImageIO.write(bImage, "png", new File(file.getName() + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void playAudio() {
        mediaPlayer.play();
    }

    @FXML
    private void pauseAudio() {
        mediaPlayer.pause();
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
            int minutes = (int) mediaPlayer.getCurrentTime().toMinutes() % 60;
            int seconds = (int) mediaPlayer.getCurrentTime().toSeconds() % 60;
            int minutesDuration = (int) mediaPlayer.getTotalDuration().toMinutes() % 60;
            int secondsDuration = (int) mediaPlayer.getTotalDuration().toSeconds() % 60;
            durationLabel.setText(String.format("Zeit: %02d:%02d / %02d:%02d", minutes, seconds, minutesDuration,
                    secondsDuration));
        }
    }
}
