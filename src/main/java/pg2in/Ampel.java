package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class Ampel extends SimpleGraphicsApp {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 700;
    private static final int X_BASE = 150;
    private static final int Y_TOP = 200;
    private static final int Y_MIDDLE = 350;
    private static final int Y_BOTTOM = 500;
    public static final int RADIUS_BG = 65;
    public static final int RADIUS_LIGHT = 60;

    private Circle red;
    private Circle orange;
    private Circle green;

    AmpelZustand zustand = AmpelZustand.RED;

    enum AmpelZustand {
        RED,
        RED_ORANGE,
        GREEN,
        ORANGE
    }

    public void next() {
        zustand = AmpelZustand.values()[(zustand.ordinal()+1)% AmpelZustand.values().length];
        System.out.println("current state is: " + zustand);
    }

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);

        add(new Circle(X_BASE, Y_TOP, RADIUS_BG, Colors.BLACK));
        add(new Circle(X_BASE, Y_MIDDLE, RADIUS_BG, Colors.BLACK));
        add(new Circle(X_BASE, Y_BOTTOM, RADIUS_BG, Colors.BLACK));

        red = add(new Circle(X_BASE, Y_TOP, RADIUS_LIGHT, Colors.RED));
        orange = add(new Circle(X_BASE, Y_MIDDLE, RADIUS_LIGHT, Colors.ORANGE));
        green = add(new Circle(X_BASE, Y_BOTTOM, RADIUS_LIGHT, Colors.GREEN));

        updateCircles();
    }

    @Override
    public void runMain() {
        while (isRunning()) {
            waitForMouseEvent();
            next();
            updateCircles();
        }
    }

    private void updateCircles() {
        if (zustand.name().toLowerCase().contains("red")) {
            red.setVisible(true);
        } else {
            red.setVisible(false);
        }
        if (zustand.name().toLowerCase().contains("orange")) {
            orange.setVisible(true);
        } else {
            orange.setVisible(false);
        }
        if (zustand.name().toLowerCase().contains("green")) {
            green.setVisible(true);
        } else {
            green.setVisible(false);
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}