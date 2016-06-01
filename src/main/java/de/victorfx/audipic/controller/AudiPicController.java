package de.victorfx.audipic.controller;

import de.victorfx.audipic.model.SettingsStore;
import de.victorfx.audipic.painter.IPainter;
import de.victorfx.audipic.painter.MagicPainter2;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.*;
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
import javafx.stage.Stage;

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
    private static final int BAND_COUNT = 10;
    @FXML
    private TextField inputThreshold;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane canvasPane;
    @FXML
    private VBox settingsBox;
    @FXML
    private Label durationLabel;
    @FXML
    private TextField inputInterval;
    @FXML
    private TextField inputMultiplikator;
    @FXML
    private TextField inputDiffMultiplikator;
    @FXML
    private CheckBox checkDynamicLines;
    @FXML
    private TextField inputLinesWidth;
    @FXML
    private TextField inputLinesFactor;
    @FXML
    private Button playbtn;
    @FXML
    private Button pausebtn;
    @FXML
    private Label fpsLabel;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private ColorPicker colorPickerBg;
    private FileChooser fc;
    private MediaPlayer mediaPlayer;
    private GraphicsContext context;
    private List<IPainter> painters = new ArrayList<>();
    private List<Color> colorList;
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private SettingsStore settingsStore;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AnimationTimer timer = new FPSCounter();
        timer.start();

        settingsStore = new SettingsStore();

        choiceBox.getItems().addAll(SettingsStore.LINES, SettingsStore.OVALS, SettingsStore.RECTS, SettingsStore.ARCS, SettingsStore.TEXTS);
        choiceBox.getSelectionModel().select(0);
        pausebtn.setDisable(true);
        playbtn.setDisable(true);
        inputLinesWidth.disableProperty().bind(checkDynamicLines.selectedProperty());
        inputLinesFactor.disableProperty().bind(checkDynamicLines.selectedProperty());

        int width = device.getDisplayMode().getWidth() - (int) settingsBox.getMinWidth();
        int height = device.getDisplayMode().getHeight();
        canvas.setWidth(width);
        canvas.setHeight(height);
        context = canvas.getGraphicsContext2D();

        preparePainter(width, height);
    }

    private void preparePainter(int width, int height) {
        for (int i = 0; i < BAND_COUNT; i++) {
            painters.add(new MagicPainter2());
        }

        for (IPainter painter : painters) {
            Canvas canvas = new Canvas();
            canvas.setHeight(height);
            canvas.setWidth(width);
            canvasPane.getChildren().add(canvas);
            painter.setGraphicContextForMagic(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight(), settingsStore);
        }

        colorList = getColorForPainter(painters.size());

        for (int i = 0; i < painters.size(); i++) {
            painters.get(i).setColor(colorList.get(i));
        }
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
            disableAllInputs(false);
            storeCurrentInputInSettingsStore();
            context.setFill(settingsStore.getBgColor());
            context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (IPainter painter : painters) {
                painter.updateSettings();
                painter.clearCanvas();
            }
            mediaPlayer.setAudioSpectrumInterval(settingsStore.getSpektrum_interval());
            mediaPlayer.setAudioSpectrumNumBands(painters.size());
            mediaPlayer.setAudioSpectrumThreshold(settingsStore.getSpektrumThreshold());
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
                    ImageIO.write(bImage, "png", new File(file.getName() + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void storeCurrentInputInSettingsStore() {
        settingsStore.setBgColor(colorPickerBg.getValue());
        settingsStore.setSpektrum_interval(inputInterval.getText().isEmpty() ? 0.1 : Double.parseDouble(inputInterval.getText()));
        settingsStore.setDynamicLines(checkDynamicLines.isSelected());
        settingsStore.setDiffMultiplikator(inputDiffMultiplikator.getText().isEmpty() ? 0 : Integer.parseInt(inputDiffMultiplikator.getText()));
        settingsStore.setMultiplikatror(inputMultiplikator.getText().isEmpty() ? 8 : Integer.parseInt(inputMultiplikator.getText()));
        settingsStore.setSpektrumThreshold(inputThreshold.getText().isEmpty() ? (-100) : Integer.parseInt(inputThreshold.getText()));
        settingsStore.setType(choiceBox.getValue().toString());
        if (!settingsStore.isDynamicLines()) {
            settingsStore.setLine_width(inputLinesWidth.getText().isEmpty() ? 2 : Integer.parseInt(inputLinesWidth.getText()));
            settingsStore.setLineFactor(inputLinesFactor.getText().isEmpty() ? 1 : Integer.parseInt(inputLinesFactor.getText()));
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
        choiceBox.setDisable(value);
        if (value) {
            inputLinesWidth.disableProperty().unbind();
            inputLinesWidth.setDisable(value);
            inputLinesFactor.disableProperty().unbind();
            inputLinesFactor.setDisable(value);
        } else {
            inputLinesWidth.disableProperty().bind(checkDynamicLines.selectedProperty());
            inputLinesFactor.disableProperty().bind(checkDynamicLines.selectedProperty());
        }
    }

    private List<Color> getColorForPainter(int size) {
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int painterValue = (765 / size) * (i + 1);
            double red = 0;
            double green = 0;
            double blue = 0;
            int tmp = 0;

            if ((painterValue / 510) >= 1) {
                tmp = painterValue % 510;
                if (tmp == 0) {
                    green = 255;
                } else {
                    blue = tmp;
                    green = 255 - tmp;
                }
            } else if ((painterValue / 255) >= 1) {
                tmp = painterValue % 255;
                if (tmp == 0) {
                    red = 255;
                } else {
                    green = tmp;
                    red = 255 - tmp;
                }
            } else {
                red = painterValue;
            }

            red = red / 255;
            green = green / 255;
            blue = blue / 255;

            colors.add(new Color(red, green, blue, 1.0));
        }
        return colors;
    }

    @FXML
    private void closeApplication() {
        Platform.exit();
    }

    @FXML
    private void setFullscreen() {
        Stage tmp = (Stage) canvasPane.getScene().getWindow();
        tmp.setFullScreen(true);
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
