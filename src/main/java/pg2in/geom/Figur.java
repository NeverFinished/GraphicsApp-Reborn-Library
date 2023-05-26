package pg2in.geom;

import de.ur.mi.oop.graphics.GraphicsObject;

public abstract class Figur implements Comparable<Figur>, Skalierbar {
	
	protected int x, y;
	
	public Figur(int x, int y) {
		this.x = x; this.y = y;
	}
	public abstract double umfang();
	public abstract double flaeche();
	public abstract Figur hülle();

	@Override
	public String toString() {
		String klassenName = this.getClass().getSimpleName();
		return String.format("%s [x: %d, y: %d, Fläche: %.1f, Umfang: %.1f]", klassenName, x, y, flaeche(), umfang() );
	}

	@Override
	public int compareTo(Figur o) {
		int cmp = Double.compare(flaeche(), o.flaeche());
		if (cmp == 0) {
			return Double.compare(umfang(), o.umfang());
		}
		return cmp;
	}

	public abstract GraphicsObject convertToGraphicsObject();
}
