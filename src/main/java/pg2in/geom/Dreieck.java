package pg2in.geom;

import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Compound;
import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.graphics.Line;

public class Dreieck extends Figur {
    private int seite;

    public Dreieck(int x, int y, int seite) {
        super(x, y);
        this.seite = seite;
    }

//    public void zeichne(Graphics g) {
//        int höhe = (int) (seite/2.*Math.sqrt(3.));
//        g.drawLine(x, y+höhe, x+seite, y+höhe); // Grundlinie
//        g.drawLine(x+seite/2, y, x, y+höhe);
//        g.drawLine(x+seite, y+höhe, x+seite/2, y);
//    }

    @Override
    public double umfang() {
        return 3. * seite;
    }

    @Override
    public double flaeche() {
        return (double) seite * seite * Math.sqrt(3.);
    }

    @Override
    public Figur hülle() {
        int höhe = (int) (seite / 2. * Math.sqrt(3.));
        return new Kreis(x + seite / 2, y + höhe - (int) (seite / 6. * Math.sqrt(3.)), (int) (seite / 3. * Math.sqrt(3.)) + 1);
    }

    @Override
    public void scale(double factorX, double factorY) {
        this.seite = (int) (seite * factorX);
    }

    @Override
    public GraphicsObject convertToGraphicsObject() {
        int höhe = (int) (seite / 2. * Math.sqrt(3.));
        Line l1 = new Line(x, y + höhe, x + seite, y + höhe, Colors.BLACK); // Grundlinie
        Line l2 = new Line(x + seite / 2, y, x, y + höhe, Colors.BLACK);
        Line l3 = new Line(x + seite, y + höhe, x + seite / 2, y, Colors.BLACK);
        return new Compound(l1, l2, l3);
    }
}

