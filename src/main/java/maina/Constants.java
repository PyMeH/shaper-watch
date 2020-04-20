package maina;

public interface Constants {//in tenths of millimeters
    int zoom = 2;
    //radii, use zoom to make them bigger
    final double rOuterCut = zoom * 220;
    final double rHole = zoom * 5.0 / 2;
    final double rHoleVbottom = rHole * 8/5;
    final double rCollar = rHole * 8 / 5;
    final double rNumberBase = zoom * 140;
    final double dRope = zoom * 4;


    final double rMinuteMarks = rOuterCut - rHole * 5;
    final double rHourMarks = rMinuteMarks - 2 * rHole;
    final double rNumberTop = rNumberBase + 2 * rHole * (292.0 / 40);
    final double xCenter = rOuterCut;
    final double yCenter = rOuterCut;

}
