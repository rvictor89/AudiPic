package de.victorfx.audipic.painter;

import de.victorfx.audipic.model.SettingsStore;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

/**
 * @author Ramon Victor Mai 2016.
 */
public class PainterLine implements IPainter {

    private GraphicsContext context;
    private double lastX = 0;
    private double newX = 0;
    private double lastY = 0;
    private double newY = 0;
    private double cwidth = 0;
    private double cheight = 0;
    private Color color = Color.BLACK;

    @Override
    public void setGraphicContextForMagic(GraphicsContext context, double cwidth, double cheight, SettingsStore settingsStore) {
        this.context = context;
        this.cheight = cheight;
        this.cwidth = cwidth;
        context.setLineWidth(10);
        context.setGlobalBlendMode(BlendMode.SRC_OVER);
        lastX = cwidth / 2;
        lastY = cheight / 2;
        newX = lastX;
        newY = lastY;
    }

    @Override
    public void paintMagic(double timestamp, double duration, float magnitudes, float phases) {
        timestamp = (int) timestamp;
        context.setGlobalAlpha((-magnitudes) / 100);
        if (timestamp % 4 == 0) {
            newX += (cwidth / magnitudes);
            if (newX >= cwidth || newX <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
            }
        }
        if (timestamp % 4 == 1) {
            newY += (cheight / magnitudes);
            if (newY >= cheight || newY <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
            }
        }
        if (timestamp % 4 == 2) {
            newX -= (cwidth / magnitudes);
            if (newX >= cwidth || newX <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
            }
        }
        if (timestamp % 4 == 3) {
            newY -= (cheight / magnitudes);
            if (newY >= cheight || newY <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
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
        context.setStroke(color);
    }

    @Override
    public void updateSettings() {

    }

    @Override
    public void clearCanvas() {

    }
}
