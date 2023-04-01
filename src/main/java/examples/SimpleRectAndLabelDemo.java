package examples;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Label;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class SimpleRectAndLabelDemo extends SimpleGraphicsApp {

    private Label label;
    private int i = 0;

    @Override
    public void initialize() {
        setCanvasSize(800, 600);
        setBackgroundColor(Colors.YELLOW.brighter().brighter().brighter());
        add(new Rectangle(50, 50, 300, 200, Colors.WHITE));
        label = add(new Label(150, 150, "Hello World"));
    }

    @Override
    public void runMain() {
        while (isRunning()) {
            label.setText("Counting " + i++);
            pause(1000);
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}