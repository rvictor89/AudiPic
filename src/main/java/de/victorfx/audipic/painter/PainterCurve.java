package de.victorfx.audipic.painter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Ramon Victor Mai 2016.
 */
public class PainterCurve implements IPainter {

    private GraphicsContext context;
    private double lastX = 0;
    private double lastY = 0;
    private double vorzeichen = 1;
    private double cwidth = 0;
    private double cheight = 0;

    @Override
    public void setGraphicContextForMagic(GraphicsContext context, double cwidth, double cheight) {
        this.context = context;
        this.cheight = cheight;
        this.cwidth = cwidth;
        context.beginPath();
        context.setStroke(Color.RED);
        lastX = cwidth / 2;
        lastY = cheight / 2;
        context.moveTo(lastX, lastY);
    }

    @Override
    public void paintMagic(double timestamp, double duration, float magnitudes, float phases) {
        timestamp = (int) timestamp;
        if (timestamp % 3 == 0 || timestamp % 3 == 2) {
            lastX += (cwidth / magnitudes) * vorzeichen;
            if (lastX >= cwidth) {
                vorzeichen = 1;
            }
            if (lastX <= 0) {
                vorzeichen = -1;
            }
        }
        if (timestamp % 3 == 1 || timestamp % 3 == 2) {
            lastY += (cheight / magnitudes) * vorzeichen;
            if (lastY >= cheight) {
                vorzeichen = 1;
            }
            if (lastY <= 0) {
                vorzeichen = -1;
            }
        }
        context.quadraticCurveTo(lastY, lastX, lastX, lastY);
        context.stroke();
        context.save();
    }
}
