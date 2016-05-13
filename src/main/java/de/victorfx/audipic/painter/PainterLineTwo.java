package de.victorfx.audipic.painter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;

/**
 * @author Ramon Victor Mai 2016.
 */
public class PainterLineTwo implements IPainter {

    private static final int MULTIPLIKATOR = 20;
    private static final int LINE_WIDTH = 10;
    private static final int DIFFMULTIPLIKATOR = 10;
    public static final BlendMode BLEND_MODE = BlendMode.EXCLUSION;
    private GraphicsContext context;
    private double lastX = 0;
    private double newX = 0;
    private double lastY = 0;
    private double newY = 0;
    private double cwidth = 0;
    private double cheight = 0;
    private Color color = Color.BLACK;
    private double lastDiff = 0;
    private int diff = 0;
    private int newTime = 0;

    @Override
    public void setGraphicContextForMagic(GraphicsContext context, double cwidth, double cheight) {
        this.context = context;
        this.cheight = cheight;
        this.cwidth = cwidth;
        context.setLineWidth(LINE_WIDTH);
        context.setGlobalBlendMode(BLEND_MODE);
        lastX = cwidth / 2;
        lastY = cheight / 2;
        newX = lastX;
        newY = lastY;
    }

    @Override
    public void paintMagic(double timestamp, double duration, float magnitudes, float phases) {
        //context.setLineWidth(-magnitudes/2);
        newTime = (int) (timestamp * (1 / duration));
        diff = (int) (timestamp - lastDiff) * DIFFMULTIPLIKATOR;
        context.setGlobalAlpha((-magnitudes) / 100);
        if (newTime % 4 == 3) {
            newX += (cwidth / (-magnitudes)) * MULTIPLIKATOR + diff;
            if (newX >= cwidth || newX <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        if (newTime % 4 == 0) {
            newY += (cheight / (-magnitudes)) * MULTIPLIKATOR + diff;
            if (newY >= cheight || newY <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        if (newTime % 4 == 1) {
            newX -= (cwidth / (-magnitudes)) * MULTIPLIKATOR + diff;
            if (newX >= cwidth || newX <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        if (newTime % 4 == 2) {
            newY -= (cheight / (-magnitudes)) * MULTIPLIKATOR + diff;
            if (newY >= cheight || newY <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        context.strokeLine(lastX, lastY, newX, newY);
        lastX = newX;
        lastY = newY;
        context.stroke();
        context.save();
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        context.setStroke(this.color);
    }
}
