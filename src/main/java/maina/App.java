package maina;

import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;


public class App implements Constants {

    //set to false if you are generating a file for a CNC
    private boolean isSvgForHumans = true;

    //.svg generator
    final SVGGraphics2D g = new SVGGraphics2D(
            GenericDOMImplementation.getDOMImplementation()
                    .createDocument(null, "svg", null));

    private static final int shameonme = 1234567890;
    public static void main(String[] args) throws Exception {
        final App app = new App();
        // TODO: Set proper canvas size for 72dpi SVG:
        // 72 dot/inch  =  2.834646 pixel/millimeter
        //  1 pixel/millimeter  =  25.4 dot/inch
        //254 pixel/millimeter  =  6451.6 dot/inch
        // 10 pixel/millimeter  =  254 dot/inch
        // int size = (int) Math.round(rOuterCut * 2.0d * 2.834646 / millimeter) + 1;
        app.g.setSVGCanvasSize(new Dimension(shameonme, shameonme));
        app.cutShapesForCncAndDrawBeautiesForHumans();
    }

    public void cutShapesForCncAndDrawBeautiesForHumans() throws Exception {
        final ShaperOriginCuts cnc = new ShaperOriginCuts(g);

        // cut clock body
        Coordinate center = new Coordinate(0, 0);
        cnc.exteriorCut(getCircle(center.x(), center.y(), rOuterCut));

        cutOuterHourAndMinuteHoles();

        for (double degrees = 0; degrees < 360; degrees += 30) { //draw the 12 holes for hours
            cutHolesForRomanNumber(degrees);
        }

        //cut pocket for clock mechanism
        cnc.pocketingCut(new Rectangle2D.Double(
                center.x() - millimeter * 29, center.y() - millimeter * 29,
                millimeter * 58, millimeter * 58));
        //cut hole for clock mechanism
        cnc.interiorCut(getCircle(center.x(), center.y(), rCenterHole));

        //draw clock hands
        drawClockHands();


        //save .svg
        Writer sWriter = new StringWriter();
        g.stream(sWriter);
        sWriter.close();
        Writer fWriter = new FileWriter("result.svg");
        //set document size in mm
        final int px = (int) (rOuterCut * 2.0d);
        final int mm = (int) (rOuterCut * 2.0d / millimeter);
        fWriter.append(sWriter.toString()
                .replace("" + shameonme, "" + mm + "mm")
                .replace("<svg ", "<svg viewBox=\"0 0 " + px + " " + px +"\" ")
        );
        fWriter.close();
        System.out.println("DONE.");
    }

    private void cutOuterHourAndMinuteHoles() {
        for (double degrees = 0; degrees < 360; degrees += 6) { //draw the 60 holes: 12 for hours + 48 for minutes
            double radius;
            if (0 == degrees % 30) {// it is an hour
                radius = rHourMarks;
                cutHolesForRomanNumber(degrees);
            } else radius = rMinuteMarks; // it is a minute
            final Coordinate c = new Coordinate(degrees, radius);
            cutRopeHole(c);
            drawRope(c, new Coordinate(c.α + 2, rOuterCut + rHole));

        }
    }

    private void cutHolesForRomanNumber(double degrees) {
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
        for (final char c : sHour.toCharArray()) {
            switch (c) {
                case 'I':
                    a = holesTop[i1];
                    b = holesBottom[i2];
                    cutRopeHole(a);
                    cutRopeHole(b);
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
                    cutRopeHole(a);
                    cutRopeHole(a1);
                    cutRopeHoleForBottomOfV(b, b1);
                    drawRope(a, b);
                    drawRope(a1, b1);
                    i1++;
                    i2++;
                    break;
                case 'X':
                    a = holesTop[i1];
                    b = holesBottom[i2];
                    cutRopeHole(a);
                    cutRopeHole(b);
                    i1++;
                    i2++;
                    a1 = holesTop[i1];
                    b1 = holesBottom[i2];
                    cutRopeHole(a1);
                    cutRopeHole(b1);
                    drawRope(a1, b);
                    drawRope(a, b1);
                    i1++;
                    i2++;
                    break;
            }
        }
    }

    private void cutRopeHoleForBottomOfV(Coordinate c1, Coordinate c2) {
        final ShaperOriginCuts shaper = new ShaperOriginCuts(g);

        double radius = rCollar;
        shaper.pocketingCut(new Ellipse2D.Double(c1.x() - radius, c1.y() - radius, 2 * radius, 2 * radius));
        shaper.pocketingCut(new Ellipse2D.Double(c2.x() - radius, c2.y() - radius, 2 * radius, 2 * radius));

        radius = rHole;
        final Color gray = ShaperOriginCuts.INTERIOR_FILL;
        shaper.cut(new Ellipse2D.Double(c1.x() - radius, c1.y() - radius, 2 * radius, 2 * radius), gray, gray);
        shaper.cut(new Ellipse2D.Double(c2.x() - radius, c2.y() - radius, 2 * radius, 2 * radius), gray, gray);
    }

    private void cutRopeHole(Coordinate c) {
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
        if (isSvgForHumans) {
            g.setColor(Color.orange);
            g.setStroke(new BasicStroke((float) dRope));
            g.drawLine(a.x(), a.y(), b.x(), b.y());
        }
    }

    private void drawClockHands() {
        if (isSvgForHumans) {
            Coordinate c = new Coordinate(0, 0);
            g.setStroke(new BasicStroke((float) dRope));
            Coordinate a = new Coordinate(-10, Constants.rNumberBase * 0.9);
            g.setColor(ShaperOriginCuts.GUIDE_STROKE);
            g.drawLine(c.x(), c.y(), a.x(), a.y());
            g.setStroke(new BasicStroke((float) (1.5 * dRope)));
            a = new Coordinate(37, Constants.rNumberBase * 0.6);
            g.drawLine(c.x(), c.y(), a.x(), a.y());
        }
    }

}