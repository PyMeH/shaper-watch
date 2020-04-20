package maina;

import java.util.*;

import static maina.RomanNumberMeasurements.holesUpperRow;

/**
 * @see RomanNumberMeasurements
 * and especially {@link RomanNumberMeasurements#holesUpperRow}
 * <p>
 * In this class we calculate the second row of holes.
 * For roman numbers that do not contain "V" - the second row is a copy of the first row.
 * For numbers with "V" (iv, v, vi, vii, viii) the two dots from row1 that correspond to "V" are converted to one dot.
 */
public class RomanNumberHoles {
    /**
     * Values in this Map are in pixels.
     */
    public static final Map<String, double[]> holesRowTop;
    /**
     * Values in this Map are in pixels.
     */
    public static final Map<String, double[]> holesRowBottom;

    static {
        final Map<String, double[]> m = new HashMap<>();
        holesUpperRow.forEach((romanNumber, holes) -> {//convert int[] to double[]
            double[] result = new double[holes.length];
            for (int i = 0; i < holes.length; i++) {
                result[i] = holes[i];
            }
            m.put(romanNumber, result);
        });
        holesRowTop = m;
        holesRowBottom = calculateSecondRow();
    }

    private static Map<String, double[]> calculateSecondRow() {
        Map<String, double[]> rowBottom = new HashMap<>(RomanNumberHoles.holesRowTop);
        RomanNumberHoles.holesRowTop.forEach((k, t) -> {
            final int idx = k.indexOf('V');
            if (-1 != idx) {
                double[] b = new double[t.length];
                for (int i = 0; i < t.length; i++) {
                    if (idx == i) {// merge the two holes in the upper row to one hole in the lower
                        b[i] = (t[i] + t[i + 1] - Constants.dDistanceBetweenBottomHolesOfVInMeasuredPixels) / 2.0;
                        b[i + 1] = (t[i] + t[i + 1] + Constants.dDistanceBetweenBottomHolesOfVInMeasuredPixels) / 2.0;
                        i++;
                    } else b[i] = t[i];
                }
                rowBottom.put(k, b);
            }
        });
        return rowBottom;
    }

    /**
     * @param romanNumber        e.g. "XI"
     * @param additionalRotation e.g. -30 //for "XI"
     * @return coordinates of first and second row of holes that need to be drilled for the given romanNumber
     */
    static Coordinate[][] toCoordinates(String romanNumber, double additionalRotation) {
        final double[] t = holesRowTop.get(romanNumber);
        final double[] b = holesRowBottom.get(romanNumber);
        var width = t[t.length - 1];

        Coordinate[][] result = {new Coordinate[t.length], new Coordinate[b.length]};
        double r = Constants.rNumberTop;
        for (int i = 0; i < t.length; i++)
            result[0][i] = new Coordinate(additionalRotation - pxAtNumberTop2Degrees(width, r) / 2 + pxAtNumberTop2Degrees(t[i], r), r);

        //there is a bug here. Invisible ...
        //TODO: remove bottom row from all calculations, only make the logic that draws "V" to really care about lower V dot.
        //TODO: alternatively (not sure it works) - change #widthToDegrees method below to input a radius and scale accordingly
        r = Constants.rNumberBase;
        for (int i = 0; i < b.length; i++)
            result[1][i] = new Coordinate(additionalRotation - pxAtNumberTop2Degrees(width, r) / 2 + pxAtNumberTop2Degrees(b[i], r), r);

        return result;
    }

    static private double pxAtNumberTop2Degrees(double dots, double radiusAtWhichWeCalculate) {
        double radiusCorrection = radiusAtWhichWeCalculate / Constants.rNumberTop;
        radiusCorrection = 1;
        return radiusCorrection * dots / Constants.pxAtNumberTopInOneDegree;
    }

}
