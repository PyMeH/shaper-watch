package maina;

public interface Constants {//in tenths of millimeters
    int millimeter = 10;//well, this is .svg, no such thing as mm (unless you set viewBox, but I didn't)

    //radii

    double rOuterCut = millimeter * 220;
    double rHole = millimeter * 5.0 / 2; //5mm hole for a 4mm rope
    double rCenterHole = millimeter * 8.0 / 2; //8mm for a typical clock mechanism
    double rCollar = rHole * 8 / 5;
    double rNumberBase = millimeter * 140;
    double dRope = millimeter * 4;

    //Bottom hole of "V" should be an ellipse with d1=8mm and d2=5mm (rHole*2). Letter "I" and rope are 40 pixels.
    double dDistanceBetweenBottomHolesOfVInMeasuredPixels = 40;
    // 2 deg ~= 40 dots
    double pxAtNumberTopInOneDegree = 23.0;//23 looks best


    double rMinuteMarks = rOuterCut - rHole * 5;
    double rHourMarks = rMinuteMarks - 3 * rHole;
    double rNumberTop = rNumberBase + 2 * rHole * (292.0 / 40); // "I" is measured to be 292px tall and 40px wide.
    double xCenter = rOuterCut;
    double yCenter = rOuterCut;

}
