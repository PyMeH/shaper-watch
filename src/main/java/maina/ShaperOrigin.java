package maina;

import java.awt.Color;
import java.awt.Shape;
import java.io.IOException;

/**
 * See ./shaper origin template.png
 */
public class ShaperOrigin {
    //colors
    public static final Color INTERIOR_STROKE = Color.black;
    public static final Color INTERIOR_FILL = Color.white;

    public static final Color EXTERIOR_STROKE = Color.black;
    public static final Color EXTERIOR_FILL = EXTERIOR_STROKE;

    public static final Color ON_LINE_STROKE = Color.gray;
    public static final Color ON_LINE_FILL = Color.white;


    public static final Color POCKET_STROKE = Color.gray;
    public static final Color POCKET_FILL = POCKET_STROKE;

    public static final Color GUIDE_STROKE = Color.blue;
    public static final Color GUIDE_FILL = GUIDE_STROKE;

    public SvgGenerator svg;

    public ShaperOrigin(SvgGenerator svg) throws IOException {
        this.svg = svg;
    }


    public void interiorCut(Shape s) {
        cut(s, INTERIOR_STROKE, INTERIOR_FILL);
    }

    public void exteriorCut(Shape s) {
        cut(s, EXTERIOR_STROKE, EXTERIOR_FILL);
    }

    public void pocketingCut(Shape s) {
        cut(s, POCKET_STROKE, POCKET_FILL);
    }

    public void guideCut(Shape s) {
        cut(s, GUIDE_STROKE, GUIDE_FILL);
    }

    public void cut(Shape s, Color stroke, Color fill) {
        svg.draw(s, stroke, fill);
    }
}
