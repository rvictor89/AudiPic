package de.victorfx.audipic.model;

import javafx.scene.paint.Color;

/**
 * @author Ramon Victor Mai 2016.
 */
public class SettingsStore {
    public static final String LINES = "Lines";
    public static final String OVALS = "Ovals";
    public static final String RECTS = "Rects";
    public static final String ARCS = "Arcs";
    public static final String TEXTS = "Texts";

    private double spektrum_interval = 0.033;
    private int multiplikatror = 8;
    private int line_width = 2;
    private int lineFactor = 1;
    private int diffMultiplikator = 0;
    private int spektrumThreshold = (-100);
    private boolean dynamicLines = true;
    private String type = "Lines";
    private Color bgColor = Color.WHITE;

    public int getSpektrumThreshold() {
        return spektrumThreshold;
    }

    public void setSpektrumThreshold(int spektrumThreshold) {
        this.spektrumThreshold = spektrumThreshold;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSpektrum_interval() {
        return spektrum_interval;
    }

    public void setSpektrum_interval(double spektrum_interval) {
        this.spektrum_interval = spektrum_interval;
    }

    public int getMultiplikatror() {
        return multiplikatror;
    }

    public void setMultiplikatror(int multiplikatror) {
        this.multiplikatror = multiplikatror;
    }

    public int getLine_width() {
        return line_width;
    }

    public void setLine_width(int line_width) {
        this.line_width = line_width;
    }

    public int getLineFactor() {
        return lineFactor;
    }

    public void setLineFactor(int lineFactor) {
        this.lineFactor = lineFactor;
    }

    public int getDiffMultiplikator() {
        return diffMultiplikator;
    }

    public void setDiffMultiplikator(int diffMultiplikator) {
        this.diffMultiplikator = diffMultiplikator;
    }

    public boolean isDynamicLines() {
        return dynamicLines;
    }

    public void setDynamicLines(boolean dynamicLines) {
        this.dynamicLines = dynamicLines;
    }
}
