package maina;

public interface Constants {
    //radii
    final double rOuterCut = 200;
    final double rHole = 5.0 / 2;
    final double rMinuteMarks = 180;
    final double rHourMarks = rMinuteMarks - 2 * rHole;
    final double rNumberBase = 120;
    final double rNumberTop = rNumberBase + 2 * rHole * (292.0 / 40);
    final double xCenter = rOuterCut;
    final double yCenter = rOuterCut;

}
