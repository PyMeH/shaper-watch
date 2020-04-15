package maina;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
    final SVGGraphics2D g = new SVGGraphics2D(
            GenericDOMImplementation.getDOMImplementation()
                    .createDocument(null, "svg", null));


    public static void main(String[] args) throws Exception {
        new App().draw();
    }

    public void draw() throws Exception {
        drawRawPlywoodBoard();
        //draw the clock board (red circle, brown fill)
        drawCutCircle(xCenter, yCenter, rOuterCut, colClockFace);


        for (double a = 0; a < _2PI; a += _2PI / 12) { // 12 hourly dots
            double x = xCenter + Math.cos(a) * rHourMarks;
            double y = yCenter + Math.sin(a) * rHourMarks;
            drawHole(x, y);
        }

        int i = -1;//skip hourly marks
        for (double a = 0; a < _2PI; a += _2PI / 60) { // 60-12 minute dots
            i++;
            if (0 == i % 5) continue;
            drawHole(xCenter + Math.cos(a) * rMinuteMarks, yCenter + Math.sin(a) * rMinuteMarks);
        }

        for (double a = 0; a < _2PI; a += _2PI / 360) { // 360 tiny dots for numbers
            drawCutCircle(xCenter + Math.cos(a) * rNumberTop, xCenter + Math.sin(a) * rNumberTop, 0.2, colClockFace);
            drawCutCircle(xCenter + Math.cos(a) * rNumberBase, xCenter + Math.sin(a) * rNumberBase, 0.2, colClockFace);
        }

        saveSvg();
        System.out.println("DONE.");
    }

    private void saveSvg() throws IOException {
        FileWriter file = new FileWriter("result.svg");
        PrintWriter writer = new PrintWriter(file);
        g.stream(writer);
        writer.close();
    }

    private void drawRawPlywoodBoard() {
        //draw the plywood(red square, light brown fill)
        RectangularShape rect = new Rectangle2D.Double(0, 0, 2 * rOuterCut, 2 * rOuterCut);
        g.setPaint(colCutLine);
        g.draw(rect);
        g.setPaint(colPlywood);
        g.fill(rect);
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