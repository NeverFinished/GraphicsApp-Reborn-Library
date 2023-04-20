package pg2in;
import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class RandomCircles extends SimpleGraphicsApp {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.WHITE);
    }

    public void runMain() {
        for (int i = 0; i < 150; i++) {
            randomCircle();
            pause(25);
        }
        System.out.println("Erstmal fertig.");
        while (isRunning()) {
            waitForMouseEvent();
            randomCircle();
        }
        System.exit(0);
    }

    private void randomCircle() {
        double radius = 5.0 + (Math.random() * 95);
        double rx = radius + (Math.random() * (WIDTH-2*radius));
        double ry = radius + (Math.random() * (HEIGHT-2*radius));
        Color rc = Colors.getRandomColor();
        Circle c = new Circle((float) rx, (float) ry, (float) radius, rc);
        c.setBorder(Colors.BLACK, 1);
        add(c);
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
