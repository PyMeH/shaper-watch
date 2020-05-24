package maina;

/**
 * Everything in the clock can be represented in a cylindrical coordinate system, so here's one.
 * The class measures angle in degrees and has methods to convert to an affine coordinate system.
 * Center of affine system is hardcoded ({@link Constants#xCenter}, {@link Constants#yCenter})
 */
public class Coordinate implements Constants { //
    final double α; //αλφα
    final double r; //radius
    int decimalPlaces = 3; //-1 for "do not round"

    Coordinate(double a, double r) {
        this.α = a;
        this.r = r;
    }

    public double x(double degrees, double radius) {
        return xCenter + Math.cos(Math.toRadians(degrees)) * radius;
    }

    public double y(double degrees, double radius) {
        return yCenter + Math.sin(Math.toRadians(degrees)) * radius;
    }

    public double x() {
        return round(x(α, r));
    }

    public double y() {
        return round(y(α, r));
    }

    public int xAsInt() {
        return (int) Math.round(x(α, r));
    }

    public int yAsInt() {
        return (int) Math.round(y(α, r));
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "α=" + α +
                "°, r=" + r +
                ", x=" + x() +
                ", y=" + y() +
                '}';
    }

    private double round(double d) {
        if (-1  != decimalPlaces) {
            final double multiplier = Math.pow(10, decimalPlaces);
            long l = Math.round(d * multiplier);
            d = l / multiplier;
        }
        return d;
    }
}
