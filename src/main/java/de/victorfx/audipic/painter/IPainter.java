package de.victorfx.audipic.painter;

import de.victorfx.audipic.model.SettingsStore;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Ramon Victor Mai 2016.
 */
public interface IPainter {
    void setGraphicContextForMagic(GraphicsContext context, double cwidth, double cheight, SettingsStore settingsStore);

    void paintMagic(double timestamp, double duration, float magnitudes, float phases);

    void setColor(Color color);

    void updateSettings();
}
