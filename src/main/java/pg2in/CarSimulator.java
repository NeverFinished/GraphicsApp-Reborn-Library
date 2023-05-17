package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.Random;

public class CarSimulator extends SimpleGraphicsApp {

    private static final int MAX_CARS = 100;
    private static final int CAR_WIDTH = 30;
    private static final int CAR_HEIGHT = 9;
    private static final int CAR_MARGIN = 1;
    public static final int WINDOW_WIDTH = 1300;
    public static final int WINDOW_HEIGHT = MAX_CARS * (CAR_HEIGHT+CAR_MARGIN);

    private static final int MIN_SPEED = 2;
    private static final int MAX_SPEED = 6;
    private static final int ANIMATION_PAUSE_MS = 10;

    private Car[] cars = new Car[MAX_CARS];
    Random rand = new Random();

    @Override
    public void initialize() {
        setCanvasSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setBackgroundColor(Colors.BLACK);
        int startY = CAR_MARGIN;
        for (int i = 0; i < MAX_CARS; i++) {
            Car car = new Car(0, startY, CAR_WIDTH, CAR_HEIGHT);
            startY += (CAR_HEIGHT + CAR_MARGIN);
            cars[i] = car;
            add(car);
        }
    }

    @Override
    public void runMain() {
        while (isRunning()) {
            for (Car car : cars) {
                car.update();
            }
            pause(ANIMATION_PAUSE_MS);
        }
    }

    class Car extends Rectangle {

        private int speed;

        public Car(float x, float y, float width, float height) {
            super(x, y, width, height, Colors.getRandomColor());
            updateSpeed(true);
        }

        public void update() {
            move(speed, 0);
            if (getXPos() > WINDOW_WIDTH) {
                setXPos(0);
                // links am rand → komplett neue geschwindigkeit "auswürfeln"
                updateSpeed(true);
            } else {
                // geschwindigkeit sonst nur etwas ändern, -1,0,+1
                updateSpeed(false);

            }
        }

        private void updateSpeed(boolean fullRange) {
            if (fullRange) {
                speed = rand.nextInt((MAX_SPEED - MIN_SPEED) + 1) + MIN_SPEED;
            } else {
                speed += rand.nextInt(3)-1; // [-1,1]
                // bounding, sonst kann die geschwindigkeit auf 0 fallen
                speed = Math.min(MAX_SPEED, Math.max(MIN_SPEED, speed));
            }
        }

    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}