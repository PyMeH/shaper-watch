package maina;

import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import org.apache.batik.dom.GenericDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;

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
    final Color colCutLine = Color.red;
    final Color colPlywood = new Color(228, 198, 170); //light brown
    final Color colClockFace = new Color(188, 158, 130); //brown
    final Color colHole = new Color(245, 181, 88); //yellowish orange

    //.svg generator
    final Document doc = GenericDOMImplementation.getDOMImplementation()
            .createDocument(//SVGDOMImplementation.SVG_NAMESPACE_URI
                    null, "svg", null);
    final SVGGraphics2D g = new SVGGraphics2D(doc);
    final String[] hours = new String[]{"XII", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI"};

    public static void main(String[] args) throws Exception {
        new App().drawEmAll();
    }

    public void drawEmAll() throws Exception {
        drawRawPlywoodBoardAndClockCut();
        drawHourAndMinuteHoles();
        draw360guides();

        //save .svg
        g.stream("result.svg");
        System.out.println("DONE.");
    }

    private void draw360guides() {
        for (double degrees = 0; degrees < 360; degrees += 1) { // 360 tiny dots for numbers
            double theta = Math.toRadians(degrees);
            drawCutCircle(xCenter + Math.cos(theta) * rNumberTop, xCenter + Math.sin(theta) * rNumberTop, 0.2, colClockFace);
            drawCutCircle(xCenter + Math.cos(theta) * rNumberBase, xCenter + Math.sin(theta) * rNumberBase, 0.2, colClockFace);
        }
    }

    private void drawHourAndMinuteHoles() {
        for (double degrees = 0; degrees < 360; degrees += 6) { //draw the 60 holes: 12 for hours + 48 for minutes
            double radians = Math.toRadians(degrees);
            double radius;
            if (0 == degrees % 30) {// it is an hour
                radius = rHourMarks;
                drawRomanLettersForHour(degrees);


            } else radius = rMinuteMarks; // it is theta minute
            drawHole(xCenter + Math.cos(radians) * radius, yCenter + Math.sin(radians) * radius);
        }
    }

    private void drawRomanLettersForHour(double degrees) {
        double radians = Math.toRadians(degrees);
        int hour = (int) Math.round(degrees / 30);
        System.out.println(radians + " rad \t= " + (int) degrees + " deg\tis " + hour + "-th hour, which is\t" + hours[hour]);
        final AttributedString aStr = new AttributedString(hours[hour]);
        final AffineTransform transform = new AffineTransform();
        transform.rotate(radians + Math.toRadians(90.0)); // hour-by-hour rotation is 30 degrees, add 90 more, as we start at rightmost point
        // transform.scale(2,2);
        aStr.addAttribute(TextAttribute.TRANSFORM, transform);
        double x = xCenter + Math.cos(radians) * rNumberBase;
        double y = yCenter + Math.sin(radians) * rNumberBase;
        g.setPaint(Color.green);
        g.drawString(aStr.getIterator(), (float) x, (float) y);
    }


    private void drawRawPlywoodBoardAndClockCut() {
        //draw the plywood(red square, light brown fill)
        RectangularShape rect = new Rectangle2D.Double(0, 0, 2 * rOuterCut, 2 * rOuterCut);
        g.setPaint(colCutLine);
        g.draw(rect);
        g.setPaint(colPlywood);
        g.fill(rect);
        //draw the clock board (red circle, brown fill)
        drawCutCircle(xCenter, yCenter, rOuterCut, colClockFace);
    }

    private void drawHole(double xCenter, double yCenter) {
        double radius = rHole * 2;
        Ellipse2D circle = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
        g.setPaint(Color.black);
        g.draw(circle);
        g.setPaint(Color.black);
        g.fill(circle);
        drawCutCircle(xCenter, yCenter, rHole, colHole);


    }

    private void drawCutCircle(double xCenter, double yCenter, double radius, Color infill) {
        Ellipse2D circle = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
        g.setPaint(colCutLine);
        g.draw(circle);
        g.setPaint(infill);
        g.fill(circle);
    }
}