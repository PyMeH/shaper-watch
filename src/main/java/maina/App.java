package maina;

import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import org.apache.batik.dom.GenericDOMImplementation;


public class App implements Constants {


    //.svg generator
    final Document doc = GenericDOMImplementation.getDOMImplementation()
            .createDocument(//SVGDOMImplementation.SVG_NAMESPACE_URI
                    null, "svg", null);
    final SVGGraphics2D g = new SVGGraphics2D(doc);

    public static void main(String[] args) throws Exception {
        final App app = new App();
        app.drawEmAll();
    }

    public void drawEmAll() throws Exception {
        g.setSVGCanvasSize(new Dimension((int) Math.round(2 * rOuterCut), (int) Math.round(2 * rOuterCut)));
        // draw clock body
        Coordinate c = new Coordinate(0, 0);
        new ShaperOriginCuts(g).exteriorCut(getCircle(c.x(), c.y(), rOuterCut));

        drawHourAndMinuteHoles();

        for (double degrees = 0; degrees < 360; degrees += 30) { //draw the 12 holes for hours
            double radius = rHourMarks;
            drawHolesForRomanNumber(degrees);
        }


        //draw clock hands
        g.setStroke(new BasicStroke(10));
        Coordinate a = new Coordinate(-10, Constants.rNumberBase * 0.9);
        g.setColor(ShaperOriginCuts.GUIDE_STROKE);
        g.drawLine(c.x(), c.y(), a.x(), a.y());
        g.setStroke(new BasicStroke(15));
        a = new Coordinate(37, Constants.rNumberBase * 0.6);
        g.drawLine(c.x(), c.y(), a.x(), a.y());

        //save .svg
        g.stream("result.svg");
        System.out.println("DONE.");
    }

    private void drawHourAndMinuteHoles() {
        for (double degrees = 0; degrees < 360; degrees += 6) { //draw the 60 holes: 12 for hours + 48 for minutes
            double radius;
            if (0 == degrees % 30) {// it is an hour
                radius = rHourMarks;
                drawHolesForRomanNumber(degrees);
            } else radius = rMinuteMarks; // it is a minute
            final Coordinate c = new Coordinate(degrees, radius);
            drawRopeHole(c);
            drawRope(c, new Coordinate(c.α + 2, rOuterCut + rHole));

        }
    }

    private void drawHolesForRomanNumber(double degrees) {
        int hourIndex = (int) Math.round(degrees / 30);
        final String[] hours = new String[]{"III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "I", "II"};
        final String sHour = hours[hourIndex];
//        if (!"I".equals(sHour))
//            return;

        var holes = RomanNumberHoles.toCoordinates(sHour, degrees);
        var holesTop = holes[0];
        var holesBottom = holes[1];

        int i1 = 0;
        int i2 = 0;

        Coordinate a, a1, b, b1;
        final char[] chars = sHour.toCharArray();
        for (int i = 0; i < chars.length; i++) {

            final char c = chars[i];
            switch (c) {
                case 'I':
                    a = holesTop[i1];
                    b = holesBottom[i2];
                    drawRopeHole(a);
                    drawRopeHole(b);
                    drawRope(a, b);
                    i1++;
                    i2++;
                    break;
                case 'V':
                    a = holesTop[i1];
                    b = holesBottom[i2];
                    i1++;
                    i2++;
                    a1 = holesTop[i1];
                    b1 = holesBottom[i2];
                    drawRopeHole(a);
                    drawRopeHole(a1);
                    drawRopeHoleForBottomOfV(b, b1);
                    drawRope(a, b);
                    drawRope(a1, b1);
                    i1++;
                    i2++;
                    break;
                case 'X':
                    a = holesTop[i1];
                    b = holesBottom[i2];
                    drawRopeHole(a);
                    drawRopeHole(b);
                    i1++;
                    i2++;
                    a1 = holesTop[i1];
                    b1 = holesBottom[i2];
                    drawRopeHole(a1);
                    drawRopeHole(b1);
                    drawRope(a1, b);
                    drawRope(a, b1);
                    i1++;
                    i2++;
                    break;
            }
        }
    }

    private void drawRopeHoleForBottomOfV(Coordinate c1, Coordinate c2) {
        final ShaperOriginCuts shaper = new ShaperOriginCuts(g);

        double radius = rCollar;
        shaper.pocketingCut(new Ellipse2D.Double(c1.x() - radius, c1.y() - radius, 2 * radius, 2 * radius));
        shaper.pocketingCut(new Ellipse2D.Double(c2.x() - radius, c2.y() - radius, 2 * radius, 2 * radius));

        radius = rHole;
        shaper.cut(new Ellipse2D.Double(c1.x() - radius, c1.y() - radius, 2 * radius, 2 * radius), shaper.INTERIOR_FILL, shaper.INTERIOR_FILL);
        shaper.cut(new Ellipse2D.Double(c2.x() - radius, c2.y() - radius, 2 * radius, 2 * radius), shaper.INTERIOR_FILL, shaper.INTERIOR_FILL);
    }

    private void drawRopeHole(Coordinate c) {
        double radius = rCollar;
        final ShaperOriginCuts shaper = new ShaperOriginCuts(g);
        shaper.pocketingCut(new Ellipse2D.Double(c.x() - radius, c.y() - radius, 2 * radius, 2 * radius));
        radius = rHole;
        shaper.interiorCut(new Ellipse2D.Double(c.x() - radius, c.y() - radius, 2 * radius, 2 * radius));
    }

    private Ellipse2D getCircle(double x, double y, double radius) {
        return new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    private void drawRope(Coordinate a, Coordinate b) {
        if (1 == 2) return;
        var s = g.getStroke();
        g.setColor(Color.orange);
        g.setStroke(new BasicStroke((float) dRope));
        g.drawLine(a.x(), a.y(), b.x(), b.y());
        g.setStroke(s);
    }

}