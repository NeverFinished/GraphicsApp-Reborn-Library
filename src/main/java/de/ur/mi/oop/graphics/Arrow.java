package de.ur.mi.oop.graphics;

import java.awt.*;
import java.awt.geom.*;

public class Arrow {
    public static Shape createArrowShape(Point2D fromPt, Point2D toPt) {
        float arrowWidth = 10.0f;

        AffineTransform transform = new AffineTransform();
        GeneralPath path = new GeneralPath();
        double x = toPt.getX() - fromPt.getX();
        double y = toPt.getY() - fromPt.getY();
        double angle = Math.atan2(y, x);
        transform.setToIdentity();
        transform.translate(toPt.getX(), toPt.getY());
        transform.rotate((angle - Math.PI / 2d));

        path.moveTo(0, 0);
        path.lineTo(arrowWidth / 2, -arrowWidth);
        path.lineTo(-arrowWidth / 2, -arrowWidth);
        path.closePath();

        return transform.createTransformedShape(path);
    }
}