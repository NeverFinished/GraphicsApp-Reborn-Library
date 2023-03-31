package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class BouncingBall extends SimpleGraphicsApp {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int RADIUS = 60;

    Circle ball;
    boolean running = true;
    int dx = 1;
    int dy = 1;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        ball = add(new Circle(0, 0, RADIUS, Colors.BLUE));
    }

    @Override
    public void run() {
        while (running) {
            ball.move(dx, dy);
            if (ball.getXPos() > WIDTH || ball.getXPos() <= 0) {
                dx *= -1;
            }
            if (ball.getYPos() >= HEIGHT || ball.getYPos() <= 0) {
                dy *= -1;
            }
            pause(1);
        }
    }

    @Override
    public void onMousePressed(MousePressedEvent event) {
        running = false;
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
