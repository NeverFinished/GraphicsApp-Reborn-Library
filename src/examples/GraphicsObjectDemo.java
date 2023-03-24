package examples;

import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.Ellipse;
import de.ur.mi.oop.graphics.Line;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class GraphicsObjectDemo extends GraphicsApp {

    private static final int Y_LINE_HEIGHT = 100;

    private Line line;
    private Circle circle;
    private Ellipse ellipse;

    @Override
    public void initialize() {
        line = new Line(150, Y_LINE_HEIGHT, 250, Y_LINE_HEIGHT, Colors.RED, 15);
        circle = new Circle(50, Y_LINE_HEIGHT, 20);
        ellipse = new Ellipse(100, Y_LINE_HEIGHT, 10, 30);
    }

    @Override
    public void draw() {
        line.draw();
        circle.draw();
        ellipse.draw();
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}