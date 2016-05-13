package de.victorfx.audipic.painter;

import de.victorfx.audipic.model.SettingsStore;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * @author Ramon Victor Mai 2016.
 */
public class MagicPainter implements IPainter {

    private int multiplikator;
    private int line_width;
    private int diffmultiplikator;
    public static final BlendMode BLEND_MODE = BlendMode.EXCLUSION;
    private int linefactor;
    private boolean dynamiclines;
    private String type;
    private SettingsStore settingsStore;
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
    public void setGraphicContextForMagic(GraphicsContext context, double cwidth, double cheight, SettingsStore settingsStore) {
        this.context = context;
        this.cheight = cheight;
        this.cwidth = cwidth;
        this.settingsStore = settingsStore;
        multiplikator = settingsStore.getMultiplikatror();
        line_width = settingsStore.getLine_width();
        diffmultiplikator = settingsStore.getDiffMultiplikator();
        linefactor = settingsStore.getLineFactor();
        dynamiclines = settingsStore.isDynamicLines();
        type = settingsStore.getType();
        context.setLineWidth(line_width);
        context.setGlobalBlendMode(BLEND_MODE);
        lastX = cwidth / 2;
        lastY = cheight / 2;
        newX = lastX;
        newY = lastY;
    }

    @Override
    public void paintMagic(double timestamp, double duration, float magnitudes, float phases) {
        if (dynamiclines) {
            context.setLineWidth(-magnitudes / linefactor);
        }
        newTime = (int) (timestamp * (1 / duration));
        diff = (int) (timestamp - lastDiff) * diffmultiplikator;
        context.setGlobalAlpha((-magnitudes) / 100);
        if (newTime % 4 == 3) {
            newX += (cwidth / (-magnitudes)) * multiplikator + diff;
            if (newX >= cwidth || newX <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        if (newTime % 4 == 0) {
            newY += (cheight / (-magnitudes)) * multiplikator + diff;
            if (newY >= cheight || newY <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        if (newTime % 4 == 1) {
            newX -= (cwidth / (-magnitudes)) * multiplikator + diff;
            if (newX >= cwidth || newX <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        if (newTime % 4 == 2) {
            newY -= (cheight / (-magnitudes)) * multiplikator + diff;
            if (newY >= cheight || newY <= 0) {
                newX = cwidth / 2;
                newY = cheight / 2;
                lastX = newX;
                lastY = newY;
                lastDiff = timestamp;
            }
        }
        if (type.equals(settingsStore.LINES)) {
            context.strokeLine(lastX, lastY, newX, newY);
        }
        if (type.equals(settingsStore.OVALS)) {
            context.strokeOval(newX, newY, -magnitudes / linefactor, -magnitudes / linefactor);
        }
        if (type.equals(settingsStore.RECTS)) {
            context.strokeRect(newX, newY, -magnitudes / linefactor, -magnitudes / linefactor);
        }
        if (type.equals(settingsStore.ARCS)) {
            context.strokeArc(newX, newY, -magnitudes / linefactor, -magnitudes / linefactor, 0, Math.random()*360, ArcType.OPEN);
        }
        if (type.equals(settingsStore.TEXTS)) {
            context.strokeText("HackARThon2016", newX, newY, -magnitudes);
        }
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

    @Override
    public void updateSettings() {
        multiplikator = settingsStore.getMultiplikatror();
        line_width = settingsStore.getLine_width();
        diffmultiplikator = settingsStore.getDiffMultiplikator();
        linefactor = settingsStore.getLineFactor();
        dynamiclines = settingsStore.isDynamicLines();
        type = settingsStore.getType();
    }

    @Override
    public void clearCanvas() {
    }
}
