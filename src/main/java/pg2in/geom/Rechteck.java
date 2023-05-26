package pg2in.geom;


import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.graphics.Rectangle;

public class Rechteck extends Figur {
	private int breite, höhe;
	
	public Rechteck(int x, int y, int breite, int höhe) {
		super(x, y);
		this.breite = breite;
		this.höhe = höhe;
	}

	@Override
	public double umfang() {
		return (double) 2*breite + 2*höhe;
	}

	@Override
	public double flaeche() {
		return (double) breite*höhe;
	}

	@Override
	public Figur hülle() {
		int radius = (int)Math.sqrt( (höhe/2.)*(höhe/2.) + (breite/2.)*(breite/2.) );
		return new Kreis(x + breite/2, y + höhe/2, radius);
	}

	@Override
	public void scale(double factorX, double factorY) {
		this.breite = (int) (breite * factorX);
		this.höhe = (int) (höhe * factorY);
	}

	@Override
	public GraphicsObject convertToGraphicsObject() {
		Rectangle ret = new Rectangle(x, y, breite, höhe, null);
		ret.setBorderColor(Colors.BLACK);
		ret.setBorderWeight(2.0f);
		return ret;
	}

	protected void setBreite(int breite) {
		this.breite = breite;
	}

	protected void setHöhe(int höhe) {
		this.höhe = höhe;
	}

	public int getBreite() {
		return breite;
	}

	public int getHöhe() {
		return höhe;
	}
}
