package pg2in.geom;


public class Quadrat extends Rechteck {

	public Quadrat(int x, int y, int seiten) {
		super(x, y, seiten, seiten);
	}
	@Override
	public void scale(double factorX, double factorY) {
		int seite = (int) (getBreite() * factorX);
		// wir ignorieren factorY, quadrat bleibt quadrat
		setBreite (seite);
		setHöhe(seite);
	}

}
