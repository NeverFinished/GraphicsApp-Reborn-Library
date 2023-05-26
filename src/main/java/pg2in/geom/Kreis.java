package pg2in.geom;

import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.GraphicsObject;

public class Kreis extends Figur {
	private int radius;
	
	public Kreis(int x, int y, int radius) {
		super(x, y);
		this.radius = radius;
	}

	@Override
	public double umfang() {
		return 2. * Math.PI * (double) radius;
	}

	@Override
	public double flaeche() {
		return Math.PI * (double) radius*radius;
	}

	@Override
	public Figur hülle() {
		return new Rechteck(x-radius, y-radius, 2*radius, 2*radius);
	}

	@Override
	public void scale(double factorX, double factorY) {
		this.radius = (int) (radius * factorX);
	}

	@Override
	public GraphicsObject convertToGraphicsObject() {
		Circle ret = new Circle(x, y, radius);
		ret.setColor(null);
		ret.setBorderColor(Colors.BLACK);
		ret.setBorderWeight(2.0f);
		return ret;
	}
}
