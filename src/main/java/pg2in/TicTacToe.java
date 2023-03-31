package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class TicTacToe extends SimpleGraphicsApp {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
    }

    @Override
    public void onMousePressed(MousePressedEvent event) {
        add(new Circle(event.getXPos(), event.getYPos(), 60, Colors.getRandomColor()));
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
