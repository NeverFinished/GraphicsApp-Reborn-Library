package pg2in.geom;

public interface Skalierbar {
    default void scale(double factor) {
        scale(factor, factor);
    }
    void scale(double factorX, double factorY);
}
