package maina.customsvg;

import maina.SvgGenerator;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class SvgFile implements AutoCloseable, SvgGenerator {
    final Path file;
    final Writer writer;

    public SvgFile(Path path, double canvasWidhtInMillimeters, double canvasHeightInMillimeters, double pixelsPerMillimeter) throws IOException {
        Files.deleteIfExists(path);
        file = Files.createFile(path);
        writer = new FileWriter(file.toFile());
        // TODO: Set proper canvas size for 72dpi SVG:
        // 72 dot/inch  =  2.834646 pixel/millimeter
        //  1 pixel/millimeter  =  25.4 dot/inch
        //254 pixel/millimeter  =  6451.6 dot/inch
        // 10 pixel/millimeter  =  254 dot/inch
        // int size = (int) Math.round(rOuterCut * 2.0d * 2.834646 / millimeter) + 1;

        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                "<svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
                "\t width=\"" + canvasWidhtInMillimeters +
                "mm\" height=\"" + canvasHeightInMillimeters +
                "mm\"\n\t" +
                " viewBox=\"0 0 " +
                canvasWidhtInMillimeters * pixelsPerMillimeter + " " +
                canvasHeightInMillimeters * pixelsPerMillimeter + "\"\n" +
                "\t xml:space=\"preserve\">\n" +
                "<g>\n");
    }


    @Override
    public void close() throws IOException {
        writer.write("</g>\n" +
                "</svg>\n");
        writer.close();
    }

    @Override
    public void draw(Shape s, Color stroke, Color fill) {
        draw(s, stroke, fill, 1);
    }

    @Override
    public void draw(Shape s, Color stroke, Color fill, double lineThickness) {
        try {
            if (s instanceof Ellipse2D) {
                Ellipse2D c = (Ellipse2D) s;
                if (c.getWidth() != c.getHeight()) {
                    // <ellipse fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" cx="111" cy="55.5" rx="111" ry="55.5"/>
                    writer.write("<ellipse ");
                    addAttributesIfNecessary(stroke, fill, lineThickness);
                    writer.write("rx=\"" + c.getWidth() / 2 + "\" ");
                    writer.write("ry=\"" + c.getHeight() / 2 + "\" ");
                } else {
                    // <circle fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" cx="50" cy="230.536" r="50"/>
                    writer.write("<circle  ");
                    addAttributesIfNecessary(stroke, fill, lineThickness);
                    writer.write("r=\"" + c.getWidth() / 2 + "\" ");
                }
                writer.write("cx=\"" + c.getCenterX() + "\" ");
                writer.write("cy=\"" + c.getCenterY() + "\" ");
            } else if (s instanceof Rectangle2D) {
                // <rect x="100" y="100" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="100" height="100"/>
                Rectangle2D r = (Rectangle2D) s;
                writer.write("<rect    ");
                addAttributesIfNecessary(stroke, fill, lineThickness);
                writer.write("x=\"" + r.getX() + "\" y=\"" +
                        r.getY() + "\" width=\"" +
                        r.getWidth() + "\" height=\"" +
                        r.getHeight() + "\" ");
            } else if (s instanceof Line2D) {
                //<line fill="none" stroke="#000000" stroke-width="40" stroke-miterlimit="10" x1="279.741" y1="325.773" x2="549.583" y2="527.36"/>
                Line2D l = (Line2D) s;
                writer.write("<line    ");
                addAttributesIfNecessary(stroke, null, lineThickness);
                writer.write("x1=\"" + l.getX1() +
                        "\" y1=\"" + l.getY1() +
                        "\" x2=\"" + l.getX2() +
                        "\" y2=\"" + l.getY2() + "\" ");
            }
            writer.write("/>\n");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addAttributesIfNecessary(Color stroke, Color fill, double lineThickness) throws IOException {
        if (null != fill) writer.write("fill=\"" + color2svg(fill) + "\"\t");
        else writer.write("fill=\"none\"\t");
        if (null != stroke) writer.write("stroke=\"" + color2svg(stroke) + "\"\t");
        else writer.write("stroke=\"none\"\t");
        if (1d != lineThickness)
            writer.write("stroke-width=\"" + lineThickness + "\" "
                    + "stroke-linecap=\"round\" "
            );
    }

    private String color2svg(Color c) {
        if (Color.black == c) return "black";
        if (Color.white == c) return "white";
        if (Color.gray == c) return "gray";
        if (Color.blue == c) return "blue";
        return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
    }
}
