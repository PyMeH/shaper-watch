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
        holesRowBottom = calculateSecondRow(holesRowTop);
    }

    private static Map<String, double[]> calculateSecondRow(Map<String, double[]> rowTop) {
        Map<String, double[]> rowBottom = new HashMap<>(rowTop);
        rowTop.forEach((k, t) -> {
            final int idx = k.indexOf('V');
            if (-1 != idx) {
                double[] b = new double[t.length - 1];
                for (int i = 0, j = 0; i < t.length; i++, j++) {
                    if (idx == i) {// merge the two holes in the upper row to one hole in the lower
                        b[j] = (t[i] + t[i + 1]) / 2.0;
                        i++;
                    } else b[j] = t[i];
                }
                rowBottom.put(k, b);
                assert b.length + 1 == t.length;
                assert rowBottom.get(k).length + 1 == rowTop.get(k).length;
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
        for (int i = 0; i < t.length; i++)
            result[0][i] = new Coordinate(additionalRotation - widthToDegrees(width) / 2 + widthToDegrees(t[i]), Constants.rNumberTop);

        for (int i = 0; i < b.length; i++)
            result[1][i] = new Coordinate(additionalRotation - widthToDegrees(width) / 2 + widthToDegrees(b[i]), Constants.rNumberBase);

        return result;
    }

    static private double widthToDegrees(double dots) {
        // 2 deg ~= 40 dots
        return dots / 20.0;
    }

}
