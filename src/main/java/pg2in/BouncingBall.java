package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MouseClickedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class BouncingBall extends SimpleGraphicsApp {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int RADIUS = 60;
    public static final int ANIM_DELAY = 10;

    Circle ball;
    int dx = 2;
    int dy = 2;

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

            if (maybeMoveBallToMouseClickLocation()) break;

            pause(ANIM_DELAY);
        }
        System.exit(0);
    }

    private boolean maybeMoveBallToMouseClickLocation() {
        MouseClickedEvent clickedEvent = getNextMouseEvent();
        if (clickedEvent != null) {
            if (clickedEvent.isRightClick()) {
                return true;
            }
            ball.setXPos(clickedEvent.getXPos());
            ball.setYPos(clickedEvent.getYPos());
        }
        return false;
    }

    private void moveBall() {
        ball.move(dx, dy);
        if (ball.getXPos() > WIDTH || ball.getXPos() <= 0) {
            dx *= -1;
        }
        if (ball.getYPos() >= HEIGHT || ball.getYPos() <= 0) {
            dy *= -1;
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
