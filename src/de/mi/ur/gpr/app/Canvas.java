package de.mi.ur.gpr.app;

import de.mi.ur.gpr.graphics.*;
import de.mi.ur.gpr.graphics.Image;
import de.mi.ur.gpr.graphics.Label;
import de.mi.ur.gpr.graphics.Rectangle;

import javax.swing.*;
import java.awt.*;


public class Canvas extends JPanel {

    private GraphicsObject[] components;
    private RenderingHints renderingHints;

    public Canvas() {
        this.setOpaque(false);
        createRenderingHints();
    }

    // TODO: Decide which RenderingHint regarding Quality we want to pass,
    // since at the moment, we pass different values for the same Rendering Hint (KEY_RENDERING)
    private void createRenderingHints() {
         renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

    }

    public void setComponents(GraphicsObject[] components) {
        this.components = components;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(renderingHints);
        draw(g2d);
    }

    private void draw(Graphics2D g2d) {
        if(components != null) {
            for (int i = 0; i < components.length; i++) {
                drawComponent(g2d, components[i]);
            }
        }
    }

    private void drawComponent(Graphics2D g2d, GraphicsObject object) {
        switch(object.getType()) {
            case BACKGROUND:
            case LINE:
                drawLine(g2d, (Line) object);
                break;
            case RECTANGLE:
                drawRectangle(g2d, (Rectangle) object);
                break;
            case CIRCLE:
                drawCircle(g2d, (Circle) object);
                break;
            case ELLIPSE:
                drawEllipse(g2d, (Ellipse) object);
                break;
            case LABEL:
                drawLabel(g2d, (Label) object);
                break;
            case IMAGE:
                drawImage(g2d, (Image) object);
                break;
            default:
                break;
        }
    }

    private void drawLine(Graphics2D g2d, Line line) {
        g2d.setColor(line.getColor().asAWTColor());
        Stroke stroke = new BasicStroke(line.getLineWidth());
        g2d.setStroke(stroke);
        g2d.drawLine(
                (int) line.getXPos(),
                (int) line.getYPos(),
                (int) line.getEndpointX(),
                (int) line.getEndpointY());
    }

    private void drawEllipse(Graphics2D g2d, Ellipse ellipse) {
        g2d.setColor(ellipse.getColor().asAWTColor());
        g2d.fillOval(
                (int) ellipse.getXPos() - (int) ellipse.getRadiusX(),
                (int) ellipse.getYPos() - (int) ellipse.getRadiusY(),
                (int) ellipse.getRadiusX() * 2, (int) ellipse.getRadiusY() * 2);
    }

    private void drawRectangle(Graphics2D g2d, Rectangle rect) {
        g2d.setColor(rect.getColor().asAWTColor());
        g2d.fillRect((int) rect.getXPos(), (int) rect.getYPos(), rect.getWidth(), rect.getHeight());
    }

    private void drawCircle(Graphics2D g2d, Circle circle) {
        g2d.setColor(circle.getColor().asAWTColor());
        g2d.fillOval(
                (int) circle.getXPos() - (int) circle.getRadius(),
                (int) circle.getYPos() - (int) circle.getRadius(),
                (int) circle.getRadius() * 2,
                (int) circle.getRadius() * 2);
    }

    private void drawImage(Graphics2D g2d, Image image) {
        g2d.drawImage(image.getImage(), (int)  image.getXPos(), (int) image.getYPos(), null);
    }

    private void drawLabel(Graphics2D g2d, Label label) {
        g2d.setColor(label.getColor().asAWTColor());
        g2d.drawString(label.getText(), label.getXPos(), label.getYPos());
    }


}
