package maina;

import maina.customsvg.SvgFile;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.file.Path;


public class App implements Constants {

    public static void main(String[] args) {
        final App app = new App();
        app.drawSvg();
        app.isSvgForHumans = !app.isSvgForHumans;
        app.drawSvg();
    }

    //set to false if you are generating a file for a CNC
    private ShaperOrigin shaper;
    private boolean isSvgForHumans;

    public void drawSvg() {
        final String fileName = isSvgForHumans ? "result-for-human.svg" : "result-for-shaper.svg";
        try (final SvgFile svg = new SvgFile(Path.of(fileName),
                Constants.rOuterCut * 2 / millimeter,
                Constants.rOuterCut * 2 / millimeter,
                Constants.millimeter)) {
            shaper = new ShaperOrigin(svg);

            var cnc = this.shaper;
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

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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

        double radius = rWasherOutside;
        shaper.pocketingCut(new Ellipse2D.Double(c1.x() - radius, c1.y() - radius, 2 * radius, 2 * radius));
        shaper.pocketingCut(new Ellipse2D.Double(c2.x() - radius, c2.y() - radius, 2 * radius, 2 * radius));

        if (!isSvgForHumans) {
            radius = rHole;
            shaper.interiorCut(new Ellipse2D.Double(c1.x() - radius, c1.y() - radius, 2 * radius, 2 * radius));
            shaper.interiorCut(new Ellipse2D.Double(c2.x() - radius, c2.y() - radius, 2 * radius, 2 * radius));
        }
    }

    private void cutRopeHole(Coordinate c) {
        double radius = rWasherOutside;
        shaper.pocketingCut(new Ellipse2D.Double(c.x() - radius, c.y() - radius, 2 * radius, 2 * radius));
        if (!isSvgForHumans) {
            radius = rHole;
            shaper.interiorCut(new Ellipse2D.Double(c.x() - radius, c.y() - radius, 2 * radius, 2 * radius));
        }
    }

    private Ellipse2D getCircle(double x, double y, double radius) {
        return new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    private void drawRope(Coordinate a, Coordinate b) {
        if (isSvgForHumans) {
            Line2D.Double line = new Line2D.Double(a.x(), a.y(), b.x(), b.y());
            shaper.svg.draw(line, Color.orange, null, dRope);
        }
    }

    private void drawClockHands() {
        if (isSvgForHumans) {
            Coordinate c = new Coordinate(0, 0);
            Coordinate a = new Coordinate(-10, Constants.rNumberBase * 0.9);
            Line2D.Double line = new Line2D.Double(c.x(), c.y(), a.x(), a.y());
            shaper.svg.draw(line, ShaperOrigin.GUIDE_STROKE, null, dRope);

            a = new Coordinate(37, Constants.rNumberBase * 0.6);
            line = new Line2D.Double(c.x(), c.y(), a.x(), a.y());
            shaper.svg.draw(line, ShaperOrigin.GUIDE_STROKE, null, 1.5 * dRope);
        }
    }
}