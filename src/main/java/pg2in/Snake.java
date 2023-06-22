package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.KeyPressedEvent;
import de.ur.mi.oop.graphics.Label;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.LinkedList;
import java.util.List;

public class Snake extends SimpleGraphicsApp {

    private static final int WIDTH = 800, HEIGHT = 600;
    private static final int SIZE = 20;

    List<Rectangle> snakeRects = new LinkedList<>();
    int xDir = 1, yDir = 0;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setFrameRate(20);
        int x = WIDTH / 2;
        int y = HEIGHT / 2;
        for (int i = 0; i < 5; i++, x+= SIZE) {
            snakeRects.add(add(new Rectangle(x, y, SIZE, SIZE, Colors.BLUE)));
        }
    }

    @Override
    public void runMain() {
        while (isRunning()) {
            if (move(getFrameCounter() % 20 == 0)) {
                add(new Label(200, 200, "Verloren!").withFontSize(64));
                pause(5000);
                break;
            }
            pause(100);
        }
        System.exit(1);
    }

    private boolean checkCollision(float newX, float newY) {
        for (Rectangle rect : snakeRects) {
            if (newX == rect.getXPos() && newY == rect.getYPos()) {
                return true;
            }
        }
        return false;
    }

    private boolean move(boolean grow) {
        Rectangle last = snakeRects.get(snakeRects.size() - 1);
        float newX = constrain((int) (last.getXPos() + (SIZE * xDir)), WIDTH-SIZE);
        float newY = constrain((int) (last.getYPos() + (SIZE * yDir)), HEIGHT-SIZE);
        if (checkCollision(newX, newY)) {
            return true;
        }
        if (grow) {
            snakeRects.add(add(new Rectangle(newX, newY, SIZE, SIZE, Colors.BLUE)));
        } else {
            Rectangle toBeMoved = snakeRects.remove(0);
            toBeMoved.setXPos(newX);
            toBeMoved.setYPos(newY);
            snakeRects.add(toBeMoved);
        }
        return false;
    }

    private static int constrain(int given, int max) {
        if (given > max) {
            return given - max - SIZE;
        } else if (given < 0) {
            return given + max + SIZE;
        }
        return given;
    }

    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        if (event.getKeyChar() == 'w' && yDir == 0) {
            xDir = 0; yDir = -1;
        } else if (event.getKeyChar() == 'a' && xDir == 0) {
            xDir = -1; yDir = 0;
        } else if (event.getKeyChar() == 's' && yDir == 0) {
            xDir = 0; yDir = 1;
        } else if (event.getKeyChar() == 'd' && xDir == 0) {
            xDir = 1; yDir = 0;
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
