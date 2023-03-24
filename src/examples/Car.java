package examples;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Rectangle;

import java.util.Random;

public class Car {

    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 2;

    private Rectangle body;
    private int speed;
    private static Random rand = new Random();

    public Car(int x, int y, int width, int height) {
        body = new Rectangle(x,y,width,height, Colors.getRandomColor());
        body.setBorderWeight(0);
        body.setBorderColor(Colors.TRANSPARENT);
        updateSpeed();
    }

    public void update() {
        updateSpeed();
        body.move(speed, 0);
        if (body.getXPos() > GameLoopDemo.WINDOW_WIDTH) {
            body.setXPos(0);
        }
    }

    private void updateSpeed() {
        speed = rand.nextInt((MAX_SPEED - MIN_SPEED) + 1) + MIN_SPEED;
    }

    public void draw() {
        body.draw();
    }
}