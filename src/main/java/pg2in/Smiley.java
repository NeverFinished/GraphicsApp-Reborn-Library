package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.Ellipse;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class Smiley extends SimpleGraphicsApp {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final int BASE_RADIUS = 300;


    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.WHITE);
        int centerX = WIDTH/2;
        int centerY = HEIGHT/2;
        add(new Circle(centerX, centerY, BASE_RADIUS, Colors.YELLOW));
        int halfRadius = BASE_RADIUS / 2;
        Ellipse mund = new Ellipse(centerX, centerY + halfRadius,
                halfRadius, BASE_RADIUS/4, Colors.BLACK);
        add(mund);
        Circle linkesAuge = new Circle(centerX-halfRadius,
                centerY - (BASE_RADIUS/3), BASE_RADIUS/5, Colors.BLACK);
        add(linkesAuge);
        Circle rechtesAuge = new Circle(centerX+halfRadius,
                centerY - (BASE_RADIUS/3), BASE_RADIUS/5, Colors.BLACK);
        add(rechtesAuge);
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
