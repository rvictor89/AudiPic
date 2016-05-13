package de.victorfx.audipic.model;

/**
 * @author Ramon Victor Mai 2016.
 */
public class SettingsStore {
    private double spektrum_interval = 0.1;
    private int multiplikatror = 20;
    private int line_width = 2;
    private int lineFactor = 5;
    private int diffMultiplikator = 10;
    private boolean dynamicLines = true;

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