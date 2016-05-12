package de.victorfx.audipic.controller;

import de.victorfx.audipic.painter.IPainter;
import de.victorfx.audipic.painter.PainterCurve;
import de.victorfx.audipic.painter.PainterLine;
import de.victorfx.audipic.painter.PainterOval;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
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
    public FileChooser fc;
    public Pane canvasPane;
    public VBox settingsBox;
    private MediaPlayer mediaPlayer;
    private GraphicsContext context;
    private double lastX = 0;
    private double lastY = 0;
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

        painters.add(new PainterLine());
        painters.add(new PainterCurve());
        painters.add(new PainterOval());

        for (int i = 0; i < painters.size(); i++) {
            Canvas canvas = new Canvas();
            canvas.setHeight(height);
            canvas.setWidth(width);
            canvasPane.getChildren().add(canvas);
            painters.get(i).setGraphicContextForMagic(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
        }
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
            mediaPlayer.setAudioSpectrumNumBands(3);
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
