package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MouseClickedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class JumpingBall extends SimpleGraphicsApp {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 500;
    public static final int RADIUS = 8;
    public static final int ANIM_DELAY = 10;

    Circle ball;
    int dx = 1;
    float dy = 1;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.LIGHT_GREY);
        ball = add(new Circle(0, 0, RADIUS, Colors.BLUE));
    }

    @Override
    public void runMain() {
        while (isRunning()) {

            moveBall();

            pause(ANIM_DELAY);
        }
        System.exit(0);
    }

    private void moveBall() {
        dy += 0.15;
        ball.move(dx, dy);
        if (ball.getXPos() > WIDTH) {
            ball.setXPos(0);
        }
        if (ball.getYPos() >= HEIGHT-RADIUS) {
            ball.setYPos(HEIGHT-RADIUS);
            dy *= -0.9;
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
