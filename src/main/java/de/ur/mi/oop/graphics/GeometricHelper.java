package de.ur.mi.oop.graphics;

import java.awt.geom.Point2D;

public class GeometricHelper {

    public static double[] findLineCircleIntersections(double cx, double cy, double r, double m, double b) {
        // Coefficients for the quadratic equation Ax^2 + Bx + C = 0
        double A = 1 + m * m;
        double B = 2 * (m * (b - cy) - cx);
        double C = cx * cx + (b - cy) * (b - cy) - r * r;

        // Calculate the discriminant
        double discriminant = B * B - 4 * A * C;

        if (discriminant < 0) {
            // No intersection
            return new double[0];
        } else if (discriminant == 0) {
            // One intersection (tangent)
            double x = -B / (2 * A);
            double y = m * x + b;
            return new double[]{x, y};
        } else {
            // Two intersections
            double sqrtDiscriminant = Math.sqrt(discriminant);
            double x1 = (-B + sqrtDiscriminant) / (2 * A);
            double y1 = m * x1 + b;

            double x2 = (-B - sqrtDiscriminant) / (2 * A);
            double y2 = m * x2 + b;

            return new double[]{x1, y1, x2, y2};
        }
    }

    public static void main(String[] args) {
        // Circle parameters
        double h = 0.0; // x-coordinate of circle center
        double k = 0.0; // y-coordinate of circle center
        double r = 5.0; // radius of the circle

        // Line parameters (y = mx + b)
        double m = 1.0; // slope of the line
        double b = 0.0; // y-intercept of the line

        double[] intersections = findLineCircleIntersections(h, k, r, m, b);

        if (intersections.length == 0) {
            System.out.println("No intersection points.");
        } else {
            for (int i = 0; i < intersections.length; i += 2) {
                System.out.println("Intersection Point: (" + intersections[i] + ", " + intersections[i + 1] + ")");
            }
        }
    }

    // Method to calculate y given x, slope m, and point (x1, y1)
    public static double calculateY(double x, double m, double x1, double y1) {
        double b = y1 - m * x1;
        return m * x + b;
    }

    // Method to get the slope-intercept form b given slope m and point (x1, y1)
    public static double calculateIntercept(double m, double x1, double y1) {
        return y1 - m * x1;
    }

    public static void main_line(String[] args) {
        // Given point (x1, y1) and slope m
        double x1 = 2.0;
        double y1 = 3.0;
        double m = 4.0;

        // Calculate the y value for a given x
        double x = 5.0;
        double y = calculateY(x, m, x1, y1);
        System.out.println("For x = " + x + ", y = " + y);

        // Calculate the intercept b
        double b = calculateIntercept(m, x1, y1);
        System.out.println("The line equation is: y = " + m + "x + " + b);
    }

    public static void main_coord(String[] args) {
        double x = 3.0;
        double y = 4.0;
        double scaleFactor = 2.0;

        double[] polar = cartesianToPolar(x, y);
        System.out.println("Polar Coordinates: r = " + polar[0] + ", theta = " + polar[1]);

        double[] scaledCartesian = scalePolarAndConvertToCartesian(polar[0], polar[1], scaleFactor);
        System.out.println("Scaled Cartesian Coordinates: x' = " + scaledCartesian[0] + ", y' = " + scaledCartesian[1]);
    }

    public static Point2D.Double scaleVector(double x, double y, double scaleFactor) {
        double[] pc = GeometricHelper.cartesianToPolar(x, y);
        double[] scaled = GeometricHelper.scalePolarAndConvertToCartesian(pc[0], pc[1], scaleFactor);
        return new Point2D.Double(scaled[0], scaled[1]);
    }

    public static Point2D.Double scaleVector(double x, double y, double rOffset, double scaleFactor) {
        double[] pc = GeometricHelper.cartesianToPolar(x, y);
        double[] scaled = GeometricHelper.scalePolarAndConvertToCartesian(pc[0], pc[1], rOffset, scaleFactor);
        return new Point2D.Double(scaled[0], scaled[1]);
    }

    private static double[] cartesianToPolar(double x, double y) {
        //return cartesianToPolar(x, y, false);
        double r = Math.sqrt(x * x + y * y);
        double theta = Math.atan2(y, x);
        return new double[]{r, theta};
    }

    public static double[] scalePolarAndConvertToCartesian(double r, double theta, double scaleFactor) {
        double scaledR = r * scaleFactor;
        double x = scaledR * Math.cos(theta);
        double y = scaledR * Math.sin(theta);
        return new double[]{x, y};
    }

    public static double[] scalePolarAndConvertToCartesian(double r, double theta, double rOffset, double scaleFactor) {
        double scaledR = r * scaleFactor;
        scaledR += rOffset;
        double x = scaledR * Math.cos(theta);
        double y = scaledR * Math.sin(theta);
        return new double[]{x, y};
    }
}
