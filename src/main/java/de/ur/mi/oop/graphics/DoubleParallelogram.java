package de.ur.mi.oop.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class DoubleParallelogram {
    public static Shape createShape(Point2D origin, double width, double height, double shear) {
        AffineTransform transform = new AffineTransform();
        GeneralPath path = new GeneralPath();
        transform.setToIdentity();
        transform.translate(origin.getX(), origin.getY());

        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width+shear, height/2);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.lineTo(shear, height/2);
        path.closePath();

        return transform.createTransformedShape(path);
    }
}