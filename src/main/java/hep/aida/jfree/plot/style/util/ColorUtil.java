package hep.aida.jfree.plot.style.util;

import hep.aida.IBorderStyle;
import hep.aida.IFillStyle;
import hep.aida.IGridStyle;
import hep.aida.ILineStyle;
import hep.aida.IMarkerStyle;
import hep.aida.ITextStyle;

import java.awt.Color;

import org.freehep.swing.ColorConverter;
import org.freehep.swing.ColorConverter.ColorConversionException;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public final class ColorUtil {

    private ColorUtil() {
    }

    private static Color getTransparentColor(Color c, double alpha) {
        if (alpha == -1 || alpha < 0 || alpha > 1)
            return c;
        int t = (int) (255 * alpha);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), t);
    }

    public static Color toColor(Object object, Color defaultColor) {
        Color color = null;

        if (object instanceof ILineStyle)
            color = toColor((ILineStyle) object);
        else if (object instanceof IMarkerStyle)
            color = toColor((IMarkerStyle) object);
        else if (object instanceof ITextStyle)
            color = toColor((ITextStyle) object);
        else if (object instanceof IFillStyle)
            color = toColor((IFillStyle) object);
        else if (object instanceof IGridStyle)
            color = toColor((IGridStyle) object);
        else if (object instanceof IBorderStyle)
            color = toColor((IBorderStyle) object);
        else
            throw new IllegalArgumentException("Object is wrong type: " + object.getClass().getCanonicalName());

        if (color == null)
            color = defaultColor;

        return color;
    }

    public static Color toColor(ILineStyle style) {
        // Color.
        Color color = null;

        try {
            // Get the basic color.
            color = ColorConverter.get(style.color());

            // Apply opacity setting.
            color = getTransparentColor(color, style.opacity());

        } catch (ColorConversionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }

        return color;
    }

    public static Color toColor(IMarkerStyle style) {
        // Color.
        Color color = null;

        try {
            // Get the basic color.
            color = ColorConverter.get(style.color());

            // Apply opacity setting.
            color = getTransparentColor(color, style.opacity());

        } catch (ColorConversionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }

        return color;
    }

    public static Color toColor(ITextStyle style) {
        // Color.
        Color color = null;

        try {
            // Get the basic color.
            color = ColorConverter.get(style.color());

            // Apply opacity setting.
            color = getTransparentColor(color, style.opacity());

        } catch (ColorConversionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }

        return color;
    }

    public static Color toColor(IFillStyle style) {
        // Color.
        Color color = null;

        try {
            // Get the basic color.
            color = ColorConverter.get(style.color());

            // Apply opacity setting.
            color = getTransparentColor(color, style.opacity());

        } catch (ColorConversionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }

        return color;
    }

    public static Color toColor(IGridStyle style) {
        // Color.
        Color color = null;

        try {
            // Get the basic color.
            color = ColorConverter.get(style.color());

            // Apply opacity setting.
            color = getTransparentColor(color, style.opacity());

        } catch (ColorConversionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }

        return color;
    }
    
    public static Color toColor(IBorderStyle style) {

        // Color.
        Color color = null;

        try {
            // Get the basic color.
            color = ColorConverter.get(style.color());

            // Apply opacity setting.
            color = getTransparentColor(color, style.opacity());

        } catch (ColorConversionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }

        return color;
    }

}
