package de.victorfx.audipic.controller;

import de.victorfx.audipic.model.SettingsStore;
import de.victorfx.audipic.painter.IPainter;
import de.victorfx.audipic.painter.PainterLineTwo;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    public Canvas canvas;
    public Pane canvasPane;
    public VBox settingsBox;
    public Label durationLabel;
    public TextField inputInterval;
    public TextField inputMultiplikator;
    public TextField inputDiffMultiplikator;
    public CheckBox checkDynamicLines;
    public TextField inputLinesWidth;
    public TextField inputLinesFactor;
    public Button playbtn;
    public Button pausebtn;
    public Label fpsLabel;
    private FileChooser fc;
    private MediaPlayer mediaPlayer;
    private GraphicsContext context;
    private List<IPainter> painters = new ArrayList<>();
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private SettingsStore settingsStore;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AnimationTimer timer = new FPSCounter();
        timer.start();

        pausebtn.setDisable(true);
        playbtn.setDisable(true);
        inputLinesWidth.disableProperty().bind(checkDynamicLines.selectedProperty());
        inputLinesFactor.disableProperty().bind(checkDynamicLines.selectedProperty());

        settingsStore = new SettingsStore();
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
            painters.get(painters.size() - 1 - i).setGraphicContextForMagic(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight(), settingsStore);
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
            settingsStore.setSpektrum_interval(inputInterval.getText().isEmpty() ? 0.1 : Double.parseDouble(inputInterval.getText()));
            settingsStore.setDynamicLines(checkDynamicLines.isSelected());
            settingsStore.setDiffMultiplikator(inputDiffMultiplikator.getText().isEmpty() ? 10 : Integer.parseInt(inputDiffMultiplikator.getText()));
            settingsStore.setMultiplikatror(inputMultiplikator.getText().isEmpty() ? 20 : Integer.parseInt(inputMultiplikator.getText()));
            if (!settingsStore.isDynamicLines()) {
                settingsStore.setLine_width(inputLinesWidth.getText().isEmpty() ? 2 : Integer.parseInt(inputLinesWidth.getText()));
                settingsStore.setLineFactor(inputLinesFactor.getText().isEmpty() ? 5 : Integer.parseInt(inputLinesFactor.getText()));
            }
            painters.forEach(IPainter::updateSettings);
            mediaPlayer.setAudioSpectrumInterval(settingsStore.getSpektrum_interval());
            mediaPlayer.setAudioSpectrumNumBands(painters.size());
            mediaPlayer.setAudioSpectrumThreshold(-100);
            mediaPlayer.setAutoPlay(true);
            disableAllInputs(true);
            pausebtn.setDisable(false);
            mediaPlayer.setOnReady(() -> durationLabel.setText("-" + ((int) mediaPlayer.getTotalDuration().toMinutes() % 60) + ":" + ((int) mediaPlayer.getTotalDuration().toSeconds() % 60) + "min"));
            mediaPlayer.setOnEndOfMedia(() -> {
                disableAllInputs(false);
                pausebtn.setDisable(true);
                playbtn.setDisable(true);
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
        playbtn.setDisable(true);
        pausebtn.setDisable(false);
        disableAllInputs(true);
    }

    @FXML
    private void pauseAudio() {
        mediaPlayer.pause();
        playbtn.setDisable(false);
        pausebtn.setDisable(true);
        disableAllInputs(false);
    }

    private void disableAllInputs(boolean value) {
        inputMultiplikator.setDisable(value);
        inputDiffMultiplikator.setDisable(value);
        inputInterval.setDisable(value);
        checkDynamicLines.setDisable(value);
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

    /**
     * Intern AnimationTimer for computing the frames per second rate.
     */
    private class FPSCounter extends AnimationTimer {

        long oldNowTime = 0;
        long currentFramerate = 0;
        long tmpTime = 0;

        @Override
        public void handle(long now) {
            long elapsedTime = now - oldNowTime;
            long tmpElapsedTime = now - tmpTime;
            currentFramerate = 1_000_000_000 / elapsedTime;
            if (tmpElapsedTime >= 1_000_000_000) {
                fpsLabel.setText("FPS: " + String.valueOf(currentFramerate));
                tmpTime = now;
            }
            oldNowTime = now;
        }
    }
}
