package maina;

public interface Constants {//in tenths of millimeters
    int millimeter = 10;//well, this is .svg, no such thing as mm (unless you set viewBox, but I didn't)

    //radii
    double rOuterCut = millimeter * 220;
    double rHole = millimeter * 6.0 / 2; //6mm hole covered by a washer (5mm inner diameter). 4mm rope through washer
    double rCenterHole = millimeter * 8.0 / 2; //8mm for a typical clock mechanism
    double rWasherOutside = 5 * millimeter;
    double rNumberBase = millimeter * 140;
    double dRope = millimeter * 4;

    //Bottom hole of "V" should be an ellipse with d1=8mm and d2=5mm (rHole*2). Letter "I" and rope are 40 pixels.
    double dDistanceBetweenBottomHolesOfVInMeasuredPixels = 40;
    // 2 deg ~= 40 dots
    double pxAtNumberTopInOneDegree = 23;//23 looks best; 18.5 works too


    double rMinuteMarks = rOuterCut - rHole * 5;
    double rHourMarks = rMinuteMarks - 4 * rHole;
    double rNumberTop = rNumberBase + 2 * rHole * (292.0 / 40); // "I" is measured to be 292px tall and 40px wide.
    double xCenter = rOuterCut;
    double yCenter = rOuterCut;

}
