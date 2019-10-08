package de.mi.ur.gpr.graphics;

import de.mi.ur.gpr.app.GraphicsApp;
import de.mi.ur.gpr.colors.Color;
import de.mi.ur.gpr.colors.Colors;

public abstract class GraphicsObject {


    protected static final Color DEFAULT_COLOR = Colors.RED;

    private float xPos;
    private float yPos;
    private Color color;
    private Color borderColor;
    protected GraphicsObjectType type;

    public GraphicsObject(float x, float y) {
        this(x , y, DEFAULT_COLOR);
    }

    public GraphicsObject(float x, float y, Color color) {
        this.xPos = x;
        this.yPos = y;
        this.color = color;
        this.type = GraphicsObjectType.NONE;
        GraphicsApp.getApp().addObject(this);
    }

    public void draw() {
        GraphicsApp.getApp().addToDrawBuffer(this);
    }

    public float getXPos() {
        return xPos;
    }

    public void setXPos(float xPos) {
        this.xPos = xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public void setPosition(float xPos, float yPos) {
        setXPos(xPos);
        setYPos(yPos);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
    }

    public GraphicsObjectType getType() {
        return type;
    }

    public void move(float dx, float dy) {
        this.xPos += dx;
        this.yPos += dy;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        GraphicsApp.getApp().removeObject(this);
    }
}
