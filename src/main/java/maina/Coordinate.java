package maina;

/**
 * Everything in the clock can be represented in a cylindrical coordinate system, so here's one.
 * The class measures angle in degrees and has methods to convert to radians and into affine coordinate system.
 * Center of affine system is hardcoded ({@link Constants#xCenter}, {@link Constants#yCenter})
 */
public class Coordinate implements Constants{ //
    final double α; //αλφα
    final double r; //radius
    Coordinate(double a, double r) {
        this.α = a;
        this.r = r;
    }
    double radians(){return Math.toRadians(α);}
    public double x(double degrees, double radius) {
        return xCenter + Math.cos(Math.toRadians(degrees)) * radius;
    }

    public double y(double degrees, double radius) {
        return yCenter + Math.sin(Math.toRadians(degrees)) * radius;
    }
    public int x() {return (int) Math.round(x(α, r));}
    public int y() {return (int) Math.round(y(α, r));}

    @Override
    public String toString() {
        return "Coordinate{" +
                "α=" + α +
                "°, r=" + r +
                ", x=" + x() +
                ", y=" + y() +
                '}';
    }
}
