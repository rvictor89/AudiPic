package de.victorfx.audipic.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAudioSpectrumListener(new SpektrumListener());
//            mediaPlayer.setAudioSpectrumInterval(1);
//            mediaPlayer.setAudioSpectrumNumBands(1);
            mediaPlayer.setAutoPlay(true);
        }
    }

    /**
     * Intern AudioSpectrumListener for the audio visualization.
     */
    private class SpektrumListener implements AudioSpectrumListener {
        @Override
        public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
            System.out.println(timestamp + " : " + duration + " : " + magnitudes[0] + " : " + phases[0]);
        }
    }
}
