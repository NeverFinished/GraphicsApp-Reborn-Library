package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.events.MouseReleasedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class RandomClickCircles extends SimpleGraphicsApp {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private Circle current;
    private boolean grow;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
    }

    @Override
    public void runMain() {
        while (isRunning()) {
            if (current != null) {
                if (grow) {
                    current.setRadius(current.getRadius()+1);
                }
            }
            pause(10);
        }
    }

    @Override
    public void onMousePressed(MousePressedEvent event) {
        current = add(new Circle(event.getXPos(), event.getYPos(), 4, Colors.getRandomColor()));
        grow = true;
    }

    @Override
    public void onMouseReleased(MouseReleasedEvent event) {
        grow = false;
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
