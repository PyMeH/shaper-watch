package maina.measurements;

import java.util.AbstractMap;
import java.util.Map;

/**
 * See measured_widths.png. Numbers (in pixels) are taken from there
 */
public class RomanNumberMeasurements {
    final int widthInDots; // right-most black point minus left-most black point
    final int leftMostToRightMostCenters; // holes are to be cut in the center of the line

    public RomanNumberMeasurements(int widthInDots, int leftMostToRightMostCenters) {
        this.widthInDots = widthInDots;
        this.leftMostToRightMostCenters = leftMostToRightMostCenters;
    }

    public static final Map<String, RomanNumberMeasurements> measured_widths_in_pixels = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("I", new RomanNumberMeasurements(40, 0)),
            new AbstractMap.SimpleEntry<>("II", new RomanNumberMeasurements(146, 106)),
            new AbstractMap.SimpleEntry<>("III", new RomanNumberMeasurements(252, 212)),
            new AbstractMap.SimpleEntry<>("IV", new RomanNumberMeasurements(323, 283)),
            new AbstractMap.SimpleEntry<>("V", new RomanNumberMeasurements(252, 209)),
            new AbstractMap.SimpleEntry<>("VI", new RomanNumberMeasurements(323, 283)),
            new AbstractMap.SimpleEntry<>("VII", new RomanNumberMeasurements(429, 389)),
            new AbstractMap.SimpleEntry<>("VIII", new RomanNumberMeasurements(533, 495)),
            new AbstractMap.SimpleEntry<>("IX", new RomanNumberMeasurements(323, 273)),
            new AbstractMap.SimpleEntry<>("X", new RomanNumberMeasurements(252, 196)),
            new AbstractMap.SimpleEntry<>("XI", new RomanNumberMeasurements(323, 273)),
            new AbstractMap.SimpleEntry<>("XII", new RomanNumberMeasurements(429, 302))
    );

    /**
     * Contains info about the holes Shaper Origin has to cut for each roman number.
     * A roman number is "drawn" by pulling rope through holes.
     * The holes are located in two rows, e.g.
     * - "I" has one hole in the upper row and one hole in the bottom row. Rope goes from hole1 to hole2 and draws "I"
     * - "V" - the upper row contains two holes, and the lower row contains one hole, located in the middle between the two in the upper row.
     * - "X" - contains two holes in the upper row, and identical holes in the lower row.
     * <p>
     * Measurements for hole offsets is in pixels, and is taken from /measured_widths.png
     * <p>
     * Since upper and lower row are identical only except for the cases there is a "V" in the number - we only have entered the values
     * for the upper row, and the bottom row is calculated as a function of the upper row at {@link RomanNumberHoles}
     */
    static final Map<String, int[]> holesUpperRow = Map.ofEntries( //numeric values shuold match #measured_widths_in_pixels
            new AbstractMap.SimpleEntry<>("I", new int[]{0}),
            new AbstractMap.SimpleEntry<>("II", new int[]{0, 106}),
            new AbstractMap.SimpleEntry<>("III", new int[]{0, 106, 212}),
            new AbstractMap.SimpleEntry<>("IV", new int[]{0, 74, 283}),
            new AbstractMap.SimpleEntry<>("V", new int[]{0, 209}),
            new AbstractMap.SimpleEntry<>("VI", new int[]{0, 209, 283}),
            new AbstractMap.SimpleEntry<>("VII", new int[]{0, 209, 283, 389}),
            new AbstractMap.SimpleEntry<>("VIII", new int[]{0, 209, 283, 389, 495}),
            new AbstractMap.SimpleEntry<>("IX", new int[]{0, 77, 273}),
            new AbstractMap.SimpleEntry<>("X", new int[]{0, 196}),
            new AbstractMap.SimpleEntry<>("XI", new int[]{0, 196, 273}),
            new AbstractMap.SimpleEntry<>("XII", new int[]{0, 196, 273, 379})
    );

}


