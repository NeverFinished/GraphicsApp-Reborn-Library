package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.KeyPressedEvent;
import de.ur.mi.oop.events.MouseClickedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Pong extends SimpleGraphicsApp {

    public static final int WIDTH = 1200;
    public static final int PAD_WIDTH = WIDTH / 60;
    public static final int HEIGHT = 800;
    public static final int PAD_HEIGHT = HEIGHT / 4;
    public static final int RADIUS = 30;
    public static final int MARGIN = WIDTH / 60;
    public static final int ANIM_DELAY = 10;
    public static final int PAD_MOVE_STEP = 5;

    public static final boolean R = false;

    Circle ball;
    float dx = R ? (float) (2.0 + (Math.random() * 3)) : 3f;
    float dy = R ? (float) (Math.random() * 3) : 0f;
    Rectangle leftPaddle, rightPaddle;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.LIGHT_GREY);
        ball = add(new Circle(WIDTH / 2, HEIGHT / 2, RADIUS, Colors.BLACK));
        leftPaddle = add(new Paddle(Pong.MARGIN, Pong.HEIGHT / 2 - Pong.PAD_HEIGHT / 2, Pong.PAD_WIDTH, Pong.PAD_HEIGHT, Colors.RED));
        rightPaddle = add(new Paddle(WIDTH - MARGIN - PAD_WIDTH, HEIGHT / 2 - PAD_HEIGHT / 2, PAD_WIDTH, PAD_HEIGHT, Colors.BLUE));
        new Thread(() -> {
            try {
                var bis = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = bis.readLine()) != null) {
                    System.out.println("got:" + line);
                    parseLine(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void parseLine(String line) {
        String[] tokens = line.split(" ");
        if (tokens.length < 11) {
            System.err.println("cannot parse: " + line);
            return;
        }
        int buttonA = Integer.parseInt(tokens[7]);
        int buttonB = Integer.parseInt(tokens[8]);
        if (line.startsWith("A")) {
            if (buttonA > 0 && buttonB == 0) {
                leftPaddle.move(0, -PAD_MOVE_STEP * buttonA);
            }
            if (buttonB > 0 && buttonA == 0) {
                leftPaddle.move(0, PAD_MOVE_STEP * buttonB);
            }
        } else if (line.startsWith("B")) {
            if (buttonA > 0 && buttonB == 0) {
                rightPaddle.move(0, -PAD_MOVE_STEP * buttonA);
            }
            if (buttonB > 0 && buttonA == 0) {
                rightPaddle.move(0, PAD_MOVE_STEP * buttonB);
            }
        }
    }

    @Override
    public void runMain() {
        while (isRunning()) {

            moveBall();

            if (getFrameCounter() % 100 == 0) {
                dx *= 1.1;
                dy *= 1.1;
            }

            pause(ANIM_DELAY);
        }
        System.exit(0);
    }

    char prevKeyPress = 0;

    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        int scale = 1;
        if (prevKeyPress == event.getKeyChar()) {
            scale = 2;
        }
        switch (event.getKeyChar()) {
            case 'w':
                leftPaddle.move(0, -PAD_MOVE_STEP * scale);
                break;
            case 's':
                leftPaddle.move(0, PAD_MOVE_STEP * scale);
                break;
            case 'o':
                rightPaddle.move(0, -PAD_MOVE_STEP * scale);
                break;
            case 'l':
                rightPaddle.move(0, PAD_MOVE_STEP * scale);
                break;
            default:
        }
        prevKeyPress = event.getKeyChar();
    }


    private void moveBall() {
        ball.move(dx, dy);
        if (ball.getXPos() > WIDTH || ball.getXPos() <= 0) {
            resetBall();
        }
        if (ball.getYPos() >= HEIGHT || ball.getYPos() <= 0) {
            dy *= -1;
        }
        float bx = ball.getXPos();
        float by = ball.getYPos();
        double vl = Math.sqrt(dx * dx + dy * dy);
        bx += dx * (RADIUS / vl);
        by += dy * (RADIUS / vl);
        if (leftPaddle.hitTest(bx, by)) {
            dx *= -1;
        }
        if (rightPaddle.hitTest(bx, by)) {
            dx *= -1;
        }
    }

    private void resetBall() {
        ball.setPosition(WIDTH / 2, HEIGHT / 2);
        dx = (float) (1.0 + (Math.random() * 3));
        dy = (float) (Math.random() * 3);
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }

    private static class Paddle extends Rectangle {
        public Paddle(float x, float y, float width, float height, Color color) {
            super(x, y, width, height, color);
        }

        @Override
        public void move(float dx, float dy) {
            if (getYPos() > 0 && getYPos() < HEIGHT) super.move(dx, dy);
        }
    }
}
