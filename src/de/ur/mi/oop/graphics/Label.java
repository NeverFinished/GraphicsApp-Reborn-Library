package de.ur.mi.oop.graphics;

import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.utils.FontHelper;

import java.awt.*;

/**
 * Die Klasse Label ist ein grafisches Objekt, das einen Text darstellt.
 */
public class Label extends GraphicsObject {

    private static final int DEFAULT_FONT_SIZE = 12;
    private static final String DEFAULT_FONT = "SansSerif";

    private String text;
    private String font;
    private int fontSize;

    /**
     * Konstruiert ein neues Label, das an den Koordinaten x und y mit
     * angegebenem Text und Schriftfarbe.
     *
     * @param x    Die x-Position des Labels in Pixel
     * @param y    Die y-Position des Labels in Pixel
     * @param text Der Text des Labels
     */
    public Label(float x, float y, String text) {
        super(x, y);
        this.text = text;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.font = DEFAULT_FONT;
        this.type = GraphicsObjectType.LABEL;
    }

    /**
     * Konstruiert ein neues Label, das an den Koordinaten x und y mit
     * angegebenem Text und Schriftfarbe.
     *
     * @param x         Die x-Position des Labels in Pixel
     * @param y         y
     *                  Die y-Position ds Labels in Pixel
     * @param text      Der Text des Labels
     * @param fontColor Die Farbe des Labels
     */
    public Label(float x, float y, String text, Color fontColor) {
        this(x, y, text);
        this.setColor(fontColor);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gibt die geschätze Breite des Labels auf dem Bildschirm zurück (in Pixel)
     *
     */
    public int getWidthEstimate() {
        FontMetrics metrics = FontHelper.getFontMetrics(font, fontSize);
        return metrics.stringWidth(text);
    }

    /**
     * Gibt die geschätze Höhe des Labels auf dem Bildschirm zurück (in Pixel)
     *
     */
    public int getHeightEstimate() {
        FontMetrics metrics = FontHelper.getFontMetrics(font, fontSize);
        return metrics.getHeight();
    }

}
