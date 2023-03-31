package examples;

import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class ExpandingCircleDemo extends GraphicsApp {

    private static final int INITIAL_RADIUS = 10;
    private Circle circle;

    @Override
    public void initialize() {
        circle = new Circle(getWidth() / 2, getHeight() / 2, INITIAL_RADIUS);
    }

    @Override
    public void draw() {
        drawBackground(Colors.WHITE);
        int newRadius = (int) (circle.getRadius() + 1);
        circle.setRadius(newRadius);
        circle.draw();
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}