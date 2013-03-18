package hep.aida.jfree.converter;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jfree.util.ShapeUtilities;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public final class MarkerUtil {

    private MarkerUtil() {
    }

    // Available shape names for data markers.
    public static final String[] availableShapes = new String[] { "dot", "box", "triangle", "diamond", "star", "verticalLine", "horizontalLine", "cross", "circle", "square" };

    /**
     * Create an AWT shape from a name and a size.
     * 
     * @param markerShape
     *            The name of the shape or AIDA index.
     * @param size
     *            The size of the shape, the effect of which depends on the
     *            type.
     * @return The name of the shape.
     */
    public static Shape getMarkerShape(String markerShape, float size) {
        if (markerShape == null)
            return new Ellipse2D.Float(0 - size / 2, 0 - size / 2, size, size);
        else if (markerShape.equals(availableShapes[0]) || markerShape.equals("0"))
            return new Ellipse2D.Float(0 - size / 2, 0 - size / 2, size, size);
        else if (markerShape.equals(availableShapes[1]) || markerShape.equals("1"))
            return new Rectangle2D.Double(0 - size / 2, 0 - size / 2, size, size);
        else if (markerShape.equals(availableShapes[2]) || markerShape.equals("2"))
            return ShapeUtilities.createDownTriangle(size);
        else if (markerShape.equals(availableShapes[3]) || markerShape.equals("3"))
            return ShapeUtilities.createDiamond(size);
        else if (markerShape.equals(availableShapes[4]) || markerShape.equals("4"))
            return createStar(size);
        else if (markerShape.equals(availableShapes[5]) || markerShape.equals("5"))
            return new Line2D.Float(0, size, 0, -size);
        else if (markerShape.equals(availableShapes[6]) || markerShape.equals("6"))
            return new Line2D.Float(-size, 0, size, 0);
        else if (markerShape.equals(availableShapes[7]) || markerShape.equals("7"))
            return ShapeUtilities.createRegularCross(size, 1);
        else if (markerShape.equals(availableShapes[8]) || markerShape.equals("8"))
            return new Ellipse2D.Float(0 - size / 2, 0 - size / 2, size, size);
        else if (markerShape.equals(availableShapes[9]) || markerShape.equals("9"))
            return new Rectangle2D.Double(0 - size, 0 - size, size, size);
        else
            return null;
    }

    // Taken from => jas.plot.java2.PlotGraphics12
    private static Shape createStar(float size) {
        GeneralPath path = new GeneralPath();

        // half size
        float ss = (float) size / 2;

        // Position of shape, which should be at (0,0).
        float xx = 0;
        float yy = 0;

        // This code is straight copy from Tony's method.
        path.moveTo(xx, yy + ss);
        path.lineTo(xx, yy - ss);
        path.moveTo(xx - ss, yy);
        path.lineTo(xx + ss, yy);
        path.moveTo(xx - ss, yy - ss);
        path.lineTo(xx + ss, yy + ss);
        path.moveTo(xx + ss, yy - ss);
        path.lineTo(xx - ss, yy + ss);

        return path;
    }
}
