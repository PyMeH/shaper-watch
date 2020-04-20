package maina;

import java.awt.*;

/**
 * See ./shaper origin template.png
 */
public class ShaperOriginCuts {
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

    public ShaperOriginCuts(Graphics2D g) {
        this.g = g;
    }


    protected Graphics2D g;


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
        g.setColor(stroke);
        g.setStroke(new BasicStroke(1));
        g.draw(s);
        g.setColor(fill);
        g.fill(s);
    }
}
