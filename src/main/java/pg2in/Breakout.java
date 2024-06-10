package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MouseMovedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.LinkedList;
import java.util.List;
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

    /* Delay between turns/Runden */
    public static final int TURN_DELAY = 500;

    private float vx = 3.0f, vy = 3.0f;
    private Rectangle paddle;
    private Circle ball;
    private final List<Rectangle> bricks = new LinkedList<>();
    /* Für die Zufalls-Startgeschwindigkeit des Balls */
    private final Random random = new Random(0);
    int turn = 1;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.WHITE);
        paddle = new Rectangle(WIDTH / 2 - PADDLE_WIDTH / 2, HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT, Colors.BLACK);
        add(paddle);
        ball = new Circle(WIDTH / 2, HEIGHT / 2, BALL_RADIUS, Colors.BLACK);
        add(ball);
        createBricks();
        initBallSpeed();
    }

    private void initBallSpeed() {
        // vy bleibt wie oben schon initialisiert
        vx = random.nextFloat() * 2.0f + 1.0f;
        if (random.nextBoolean()) {
            vx *= -1f;
        }
    }

    private void createBricks() {
        Color[] colors = {Colors.RED, Colors.ORANGE, Colors.YELLOW, Colors.GREEN, new Color(0, 255, 255)};
        int x_offset = (WIDTH - (BRICKS_IN_ROW * BRICK_WIDTH) - ((BRICKS_IN_ROW - 1) * BRICK_SEP)) / 2;
        for (int r = 0; r < BRICK_ROWS; r++) {
            for (int c = 0; c < BRICKS_IN_ROW; c++) {
                bricks.add(add(new Rectangle(x_offset + c * (BRICK_WIDTH + BRICK_SEP), BRICK_Y_OFFSET + r * (BRICK_HEIGHT + BRICK_SEP),
                        BRICK_WIDTH, BRICK_HEIGHT, colors[r / 2])));
            }
        }
    }

    @Override
    public void runMain() {
        waitForMouseEvent();
        while (turn <= NTURNS) {
            moveBall();
            checkCollision();
            pause(PAUSE_TIME);
            if (bricks.isEmpty()) {
                System.out.println("You won! In turn: " + turn);
                break;
            }
        }
        System.exit(0);
    }

    private void checkCollision() { // TODO kreis/rechteck schnitt?
        float ex = ball.getXPos();
        float ey = ball.getYPos();
        //float scale = (BALL_RADIUS) / (float) Math.sqrt(vx * vx + vy * vy);
        //ex += vx * scale;
        //ey += vy * scale;
        Rectangle some = getBrickAt(ex, ey);
        if (some != null) {
            bricks.remove(remove(some));
            vy *= -1f; // TODO: genauer: welche seite getroffen (4 linien...)
        } else { // check paddle collision
            if (paddle.hitTest(ex, ey)) {
                vy *= -1f;
            }
        }
    }

    private void moveBall() { // Wie in BoucingBall erstmal nur übernommen, braucht noch Anpassungen
        ball.move(vx, vy);
        if (ball.getXPos() > WIDTH || ball.getXPos() <= 0) {
            vx *= -1;
        }
        if (ball.getYPos() <= 0) {
            vy *= -1;
        }
        if (ball.getYPos() >= HEIGHT) {
            vy *= -1;
/*            turn++;
            System.out.println("turn: " + turn);
            ball.setPosition(WIDTH / 2, HEIGHT / 2);
            initBallSpeed();
            pause(TURN_DELAY); */
        }
    }

    @Override
    public void onMouseMoved(MouseMovedEvent event) {
        int mouseX = event.getXPos();
        mouseX = Math.min(mouseX, WIDTH-PADDLE_WIDTH);
        paddle.setXPos(mouseX);
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }

    protected Rectangle getBrickAt(float x, float y) {
        for (Rectangle go : bricks) {
            if (go.hitTest(x, y)) {
                return go;
            }
        }
        return null;
    }

    protected GraphicsObject getGObjectAt(float x, float y) { // FIXME ist schon in Compound...? Works with Circle..?
        for (GraphicsObject go : getScene()) {
            if (go.hitTest(x, y)) {
                return go;
            }
        }
        return null;
    }
}
