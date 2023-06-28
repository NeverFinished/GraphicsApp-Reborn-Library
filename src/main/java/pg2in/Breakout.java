package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MouseMovedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.Random;

public class Breakout extends SimpleGraphicsApp {
    /* Size of the window */

    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    /* Dimensions of the paddle */
    public static final int PADDLE_WIDTH = 60;
    public static final int PADDLE_HEIGHT = 10;

    /* Offset of the paddle up from the bottom */
    public static final int PADDLE_Y_OFFSET = 30;

    /* Number of bricks per row */
    public static final int BRICKS_IN_ROW = 10;

    /* Number of rows of bricks */
    public static final int BRICK_ROWS = 10;

    /* Separation between bricks */
    public static final int BRICK_SEP = 4;

    /* Width of a brick */
    public static final int BRICK_WIDTH = (WIDTH - (BRICKS_IN_ROW - 1) * BRICK_SEP) / BRICKS_IN_ROW;

    /* Height of a brick */
    public static final int BRICK_HEIGHT = 8;

    /* Radius of the ball in pixels */
    public static final int BALL_RADIUS = 10;

    /* Offset of the top brick row from the top */
    public static final int BRICK_Y_OFFSET = 70;

    /* Number of turns */
    public static final int NTURNS = 3;

    /* pause time in milliseconds */
    public static final int PAUSE_TIME = 10;

    /* Delay between turns */
    public static final int TURN_DELAY = 500;

    private float vx = 2.0f, vy = 2.0f;
    private Rectangle paddle;
    private Circle ball;
    private Random random = new Random(0);

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.WHITE);
        paddle = new Rectangle(WIDTH/2-PADDLE_WIDTH/2, HEIGHT-PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT, Colors.BLACK);
        add(paddle);
        ball = new Circle(WIDTH/2, HEIGHT/2, BALL_RADIUS, Colors.BLACK);
        add(ball);
        createBricks();
    }

    private void createBricks() {
    }

    @Override
    public void runMain() {
        while (isRunning()) {
            moveBall();
            pause(PAUSE_TIME);
        }
        System.exit(0);
    }

    private void moveBall() {
        ball.move(vx, vy);
        if (ball.getXPos() > WIDTH || ball.getXPos() <= 0) {
            vx *= -1;
        }
        if (ball.getYPos() >= HEIGHT || ball.getYPos() <= 0) {
            vy *= -1;
        }
    }

    @Override
    public void onMouseMoved(MouseMovedEvent event) {
        int mouseX = event.getXPos();
        paddle.setXPos(mouseX);
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }

    protected GraphicsObject getGObjectAt(float x, float y) {
        for (GraphicsObject go : getScene()) {
            if (go.getXPos() <= x && x < go.getXPos() + go.getWidth()
            && go.getYPos() <= y && y < go.getYPos() + go.getHeight()) {
                return go;
            }
        }
        return null;
    }
}
