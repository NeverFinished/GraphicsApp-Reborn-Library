package examples;

import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.ArrayList;
import java.util.Random;

public class GameLoopDemo extends GraphicsApp {

    private static final int MAX_CARS = 100;
    private static final int CAR_START_X_POS = 5;
    private static final int CAR_START_Y_POS = 5;
    private static final int CAR_WIDTH = 30;
    private static final int CAR_HEIGHT = 4;
    private static final int CAR_MARGIN = 4;
    public static final int WINDOW_WIDTH = 1300;


    private Random rand;
    private ArrayList<Car> cars;

    @Override
    public void initialize() {
        setCanvasSize(WINDOW_WIDTH, 700);
        rand = new Random();
        cars = new ArrayList<Car>();
        int startY = CAR_START_Y_POS;
        for (int i = 0; i < MAX_CARS; i++) {
            Car car = new Car(CAR_START_X_POS, startY, CAR_WIDTH, CAR_HEIGHT);
            startY += (CAR_HEIGHT + CAR_MARGIN);
            cars.add(car);
        }
    }

    @Override
    public void draw() {
        drawBackground(Colors.WHITE);
        for (Car car : cars) {
            car.update();
            car.draw();
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}