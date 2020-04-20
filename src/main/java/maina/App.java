package maina;

import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;


import org.apache.batik.dom.GenericDOMImplementation;


public class App implements Constants, ShaperOriginTypesOfCuts {


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
        for (double degrees = 0; degrees < 360; degrees += 1) { // 360 tiny dots for numbers
            double theta = Math.toRadians(degrees);
//            drawCircleWithCutColor(xCenter + Math.cos(theta) * rNumberTop, xCenter + Math.sin(theta) * rNumberTop, 0.2, colClockFace);
//            drawCircleWithCutColor(xCenter + Math.cos(theta) * rNumberBase, xCenter + Math.sin(theta) * rNumberBase, 0.2, colClockFace);
        }

        drawRawPlywoodBoardAndClockCut();
        drawHourAndMinuteHoles();

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
            drawRopeHole(new Coordinate(degrees, radius));
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
                    drawRopeHole(a);
                    drawRopeHole(b);
                    drawRope(a, b);
                    i1++;
                    a = holesTop[i1];
                    drawRopeHole(a);
                    drawRope(a, b);
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


    private void drawRawPlywoodBoardAndClockCut() {
        //draw the plywood(red square, light brown fill)
        RectangularShape rect = new Rectangle2D.Double(0, 0, 2 * rOuterCut, 2 * rOuterCut);
        g.setPaint(colCutLine);
        g.draw(rect);
        g.setPaint(colPlywood);
        g.fill(rect);
        //draw the clock board (red circle, brown fill)
        drawCircleWithCutColor(xCenter, yCenter, rOuterCut, colClockFace);
    }


    private void drawRopeHole(Coordinate c) {
        g.setStroke(new BasicStroke());
        double radius = rHole * 2;
        Ellipse2D circle = new Ellipse2D.Double(c.x() - radius, c.y() - radius, 2 * radius, 2 * radius);
        g.setPaint(Color.black);
        g.draw(circle);
        g.setPaint(Color.black);
        g.fill(circle);
        drawCircleWithCutColor(c.x(), c.y(), rHole, colHole);
    }

    private void drawCircleWithCutColor(double x, double y, double radius, Color infill) {
        g.setStroke(new BasicStroke());
        Ellipse2D circle = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        g.setPaint(colCutLine);
        g.draw(circle);
        g.setPaint(infill);
        g.fill(circle);
    }

    private void drawRope(Coordinate a, Coordinate b) {
        var s = g.getStroke();
        g.setColor(Color.orange);
        g.setStroke(new BasicStroke((float) rHole * 2));
        g.drawLine(a.x(), a.y(), b.x(), b.y());
        g.setStroke(s);
    }

}