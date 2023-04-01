package examples;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class CenteredCircle extends SimpleGraphicsApp {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Circle circle;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);

        circle = add(new Circle(WIDTH / 2, HEIGHT / 2, 100, Colors.BLUE));
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}