package de.victorfx.audipic.painter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

/**
 * @author Ramon Victor Mai 2016.
 */
public class PainterLineTwo implements IPainter {

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
        context.setLineWidth(10);
        context.setGlobalBlendMode(BlendMode.EXCLUSION);
        lastX = cwidth / 2;
        lastY = cheight / 2;
        context.moveTo(lastX, lastY);
    }

    @Override
    public void paintMagic(double timestamp, double duration, float magnitudes, float phases) {
        timestamp = (int) timestamp;
        context.setGlobalAlpha((-magnitudes)/100);
        if (timestamp % 4 == 2) {
            lastX += (cwidth / magnitudes)+ timestamp * 10;
            if (lastX >= cwidth || lastX <= 0) {
                lastX = cwidth / 2;
                lastY = cheight / 2;
            }
        }
        if (timestamp % 4 == 3) {
            lastY += (cheight / magnitudes)+ timestamp * 10;
            if (lastY >= cheight || lastY <= 0) {
                lastX = cwidth / 2;
                lastY = cheight / 2;
            }
        }
        if (timestamp % 4 == 0) {
            lastX -= (cwidth / magnitudes)+ timestamp * 10;
            if (lastX >= cwidth || lastX <= 0) {
                lastX = cwidth / 2;
                lastY = cheight / 2;
            }
        }
        if (timestamp % 4 == 1) {
            lastY -= (cheight / magnitudes) + timestamp * 10;
            if (lastY >= cheight || lastY <= 0) {
                lastX = cwidth / 2;
                lastY = cheight / 2;
            }
        }
        context.lineTo(lastX, lastY);
        context.stroke();
        context.save();
    }
}
