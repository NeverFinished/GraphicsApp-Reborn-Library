package pg2in;

import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class Ampel extends GraphicsApp {

    private static final int X_BASE = 100;
    private static final int Y_TOP = 100;
    private static final int Y_MIDDLE = 200;
    private static final int Y_BOTTOM = 300;

    private Circle red;
    private Circle orange;
    private Circle green;
    private Circle[] backgrounds = new Circle[3];

    State currentState = State.RED;

    enum State {
        RED,
        RED_ORANGE,
        GREEN,
        ORANGE
    }

    @Override
    public void initialize() {
        setCanvasSize(200, 400);
        red = new Circle(X_BASE, Y_TOP, 40, Colors.RED);
        backgrounds[0] = new Circle(X_BASE, Y_TOP, 42, Colors.BLACK);
        orange = new Circle(X_BASE, Y_MIDDLE, 40, Colors.ORANGE);
        backgrounds[1] = new Circle(X_BASE, Y_MIDDLE, 42, Colors.BLACK);
        green = new Circle(X_BASE, Y_BOTTOM, 40, Colors.GREEN);
        backgrounds[2] = new Circle(X_BASE, Y_BOTTOM, 42, Colors.BLACK);
    }

    @Override
    public void draw() {
        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i].draw();
        }
        if (currentState == State.RED || currentState == State.RED_ORANGE) {
            red.draw();
        }
        if (currentState == State.ORANGE || currentState == State.RED_ORANGE) {
            orange.draw();
        }
        if (currentState == State.GREEN) {
            green.draw();
        }

        if (getFrameCounter() % 120 == 0) {
            // next();
        }
    }

    @Override
    public void onMousePressed(MousePressedEvent event) {
        next();
    }

    public void next() {
        currentState = State.values()[(currentState.ordinal()+1)%State.values().length];
        System.out.println("current state is: " + currentState);
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}