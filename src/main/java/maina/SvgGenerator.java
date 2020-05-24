package maina;


import java.awt.*;

public interface SvgGenerator {
    void draw(Shape s, Color stroke, Color fill);
    void draw(Shape s, Color stroke, Color fill, double lineThickness);
}
