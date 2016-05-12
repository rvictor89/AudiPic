package de.victorfx.audipic.painter;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @author Ramon Victor Mai 2016.
 */
public interface IPainter {
    void setGraphicContextForMagic(GraphicsContext context, double cwidth, double cheight);

    void paintMagic(double timestamp, double duration, float magnitudes, float phases);
}
