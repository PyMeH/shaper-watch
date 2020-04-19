package maina;

import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import java.text.AttributedString;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

import org.apache.batik.dom.GenericDOMImplementation;

public class App {
    //radii
    final double rOuterCut = 200;
    final double rHole = 5.0 / 2;
    final double rMinuteMarks = 180;
    final double rHourMarks = rMinuteMarks - 2 * rHole;
    final double rNumberBase = 120;
    final double rNumberTop = rNumberBase + 2 * rHole * (292.0 / 40);

    final double _2PI = 2 * Math.PI;

    final double xCenter = rOuterCut;
    final double yCenter = rOuterCut;

    //colors
    final Color colCutLine = Color.black;
    final Color colPlywood = Color.white; // new Color(228, 198, 170); //light brown
    final Color colClockFace = new Color(188, 158, 130); //brown
    final Color colHole = Color.white; // new Color(245, 181, 88); //yellowish orange

    //.svg generator
    final Document doc = GenericDOMImplementation.getDOMImplementation()
            .createDocument(//SVGDOMImplementation.SVG_NAMESPACE_URI
                    null, "svg", null);
    final SVGGraphics2D g = new SVGGraphics2D(doc);
    final String[] hours = new String[]{"III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "I", "II"};

    public static class Width {
        final int widthInDots;
        final int leftMostToRightMostCenters;

        public Width(int widthInDots, int leftMostToRightMostCenters) {
            this.widthInDots = widthInDots;
            this.leftMostToRightMostCenters = leftMostToRightMostCenters;
        }
    }

    public final Map<String, Width> widths = Map.ofEntries(
            new SimpleEntry<>("I", new Width(40, 0)),
            new SimpleEntry<>("II", new Width(146, 106)),
            new SimpleEntry<>("III", new Width(252, 212)),
            new SimpleEntry<>("IV", new Width(323, 283)),
            new SimpleEntry<>("V", new Width(252, 209)),
            new SimpleEntry<>("VI", new Width(323, 283)),
            new SimpleEntry<>("VII", new Width(429, 389)),
            new SimpleEntry<>("VIII", new Width(533, 495)),
            new SimpleEntry<>("IX", new Width(323, 273)),
            new SimpleEntry<>("X", new Width(252, 196)),
            new SimpleEntry<>("XI", new Width(323, 273)),
            new SimpleEntry<>("XII", new Width(429, 302))
    );

    final Map<String, int[]> holesUpper = Map.ofEntries(
            new SimpleEntry<>("I", new int[]{0}),
            new SimpleEntry<>("II", new int[]{0, 106}),
            new SimpleEntry<>("III", new int[]{0, 106, 212}),
            new SimpleEntry<>("IV", new int[]{0, 74, 283}),
            new SimpleEntry<>("V", new int[]{0, 209}),
            new SimpleEntry<>("VI", new int[]{0, 209, 283}),
            new SimpleEntry<>("VII", new int[]{0, 209, 283, 389}),
            new SimpleEntry<>("VIII", new int[]{0, 209, 283, 389, 495}),
            new SimpleEntry<>("IX", new int[]{0, 77, 273}),
            new SimpleEntry<>("X", new int[]{0, 196}),
            new SimpleEntry<>("XI", new int[]{0, 196, 273}),
            new SimpleEntry<>("XII", new int[]{0, 196, 273, 379})
    );


    public static void main(String[] args) throws Exception {
        new App().drawEmAll();
    }

    public void drawEmAll() throws Exception {
        drawRawPlywoodBoardAndClockCut();
        draw360guides();
        drawHourAndMinuteHoles();

        //save .svg
        g.stream("result.svg");
        System.out.println("DONE.");
    }

    private void draw360guides() {
        for (double degrees = 0; degrees < 360; degrees += 1) { // 360 tiny dots for numbers
            double theta = Math.toRadians(degrees);
            drawCircleWithCutColor(xCenter + Math.cos(theta) * rNumberTop, xCenter + Math.sin(theta) * rNumberTop, 0.2, colClockFace);
            drawCircleWithCutColor(xCenter + Math.cos(theta) * rNumberBase, xCenter + Math.sin(theta) * rNumberBase, 0.2, colClockFace);
        }
    }

    private void drawHourAndMinuteHoles() {
        for (double degrees = 0; degrees < 360; degrees += 6) { //draw the 60 holes: 12 for hours + 48 for minutes
            double radius;
            if (0 == degrees % 30) {// it is an hour
                radius = rHourMarks;
                drawRomanLettersForHour(degrees);
            } else radius = rMinuteMarks; // it is a minute
            drawRopeHole(degrees, radius);
        }
    }

    private void drawRomanLettersForHour(double degrees) {
        final Font currentFont = g.getFont();
        final AffineTransform scale2x = new AffineTransform();
        scale2x.scale(2, 2);
        g.setFont(currentFont.deriveFont(scale2x)); //set font size to double

        g.setFont(new Font("TimesRoman", Font.PLAIN, 48));


        double radians = Math.toRadians(degrees);
        int hourIndex = (int) Math.round(degrees / 30);
        final String sHour = hours[hourIndex];
        final int[] upperHoles = this.holesUpper.get(sHour);
        final AttributedString aStr = new AttributedString(sHour);
        final AffineTransform transform = new AffineTransform();
        transform.rotate(radians + Math.toRadians(90.0)); // hour-by-hour rotation is 30 degrees, add 90 more, as we start at rightmost point
        // transform.scale(2,2); //does not work, as letters start to overlap (transformation is applied to every char, not the whole text)
        aStr.addAttribute(TextAttribute.TRANSFORM, transform);

        g.setPaint(Color.green);
        var r1 = rNumberBase;
        var r2 = rNumberTop;
        g.drawString(aStr.getIterator(), (float) (xCenter + Math.cos(radians) * r1), (float) (yCenter + Math.sin(radians) * r1));
        var width = upperHoles[upperHoles.length - 1];
        drawCircleWithCutColor_cyl(degrees, r1 - 10, 1, Color.orange);
        for (int i = 0; i < upperHoles.length; i++) {
            var a = degrees - widthToDegrees(width) / 2 + widthToDegrees(upperHoles[i]);
            //drawRopeHole(holeDegrees, rNumberTop);
            drawCircleWithCutColor_cyl(a, r1, 1, Color.green);
            drawCircleWithCutColor_cyl(a, r2, 1, Color.red);
            g.setColor(Color.orange);
            g.drawLine(
                    (int) Math.round(cyl2X(a, r1)),
                    (int) Math.round(cyl2Y(a, r1)),
                    (int) Math.round(cyl2X(a, r2)),
                    (int) Math.round(cyl2Y(a, r2))
            );

        }
    }

    private double cyl2X(double degrees, double radius) {
        return xCenter + Math.cos(Math.toRadians(degrees)) * radius;
    }

    private double cyl2Y(double degrees, double radius) {
        return yCenter + Math.sin(Math.toRadians(degrees)) * radius;
    }

    private double widthToDegrees(double dots) {
        // 2 deg ~= 40 dots
        return dots / 20.0;
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

    private void drawRopeHole(double degrees, double radiusFromCenter) {
        var radians = Math.toRadians(degrees);
        double x = xCenter + Math.cos(radians) * radiusFromCenter;
        double y = yCenter + Math.sin(radians) * radiusFromCenter;
        double radius = rHole * 2;
        Ellipse2D circle = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        g.setPaint(Color.black);
        g.draw(circle);
        g.setPaint(Color.black);
        g.fill(circle);
        drawCircleWithCutColor(x, y, rHole, colHole);
    }

    private void drawHole2(double xCenter, double yCenter) {
        double radius = rHole * 2;
        Ellipse2D circle = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
        g.setPaint(Color.black);
        g.draw(circle);
        g.setPaint(Color.black);
        g.fill(circle);
        drawCircleWithCutColor(xCenter, yCenter, rHole, colHole);


    }

    private void drawCircleWithCutColor_cyl(double angleDegrees, double radiusFromCenter, double circleRadius, Color infill) {
        drawCircleWithCutColor(cyl2X(angleDegrees, radiusFromCenter), cyl2Y(angleDegrees, radiusFromCenter), circleRadius, infill);
    }

    private void drawCircleWithCutColor(double x, double y, double radius, Color infill) {
        Ellipse2D circle = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        g.setPaint(colCutLine);
        g.draw(circle);
        g.setPaint(infill);
        g.fill(circle);
    }
}