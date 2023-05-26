package pg2in.geom;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class FigurenApp extends SimpleGraphicsApp {

    @Override
    public void initialize() {
        setCanvasSize(700, 500);
        Rechteck r = new Rechteck(80, 60, 150, 100);
        Kreis k = new Kreis(500, 100, 80);
        Quadrat q = new Quadrat(300, 200, 40);
        Dreieck d = new Dreieck(500, 300, 80);
        Figur[] figuren = { r, k, q, d, r.hülle(), k.hülle(), q.hülle(), d.hülle(), d.hülle().hülle() };

        for (Figur figur : figuren) {
            GraphicsObject go = figur.convertToGraphicsObject();
            add(go);
        }
    }
    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
