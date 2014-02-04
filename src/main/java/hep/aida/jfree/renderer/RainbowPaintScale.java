package hep.aida.jfree.renderer;

import java.awt.Color;
import java.awt.Paint;

/**
 * This class provides a rainbow color scale for JFreeChart plots.
 * 
 * The code is copied and modified from part of jas.plot.ColorMap in the JAS plotter.
 * 
 * There is also code taken from jas.hist.TwoDOverlay for normalizing data values.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class RainbowPaintScale extends BasicPaintScale {

    boolean logScale = false;

    // Parameters for ctor.
    double zmin;
    double zmax;
    double zlogmin;

    // Derived parameters.
    double dispZmin;
    double dispZmax;
    double zrange;

    private static final boolean DEBUG = false;

    // TODO: Add alpha argument here which should come from AIDA style setting.
    public RainbowPaintScale(double zmin, double zmax, double zlogmin, boolean logScale) {

        this.logScale = logScale;
        setZParameters(zmin, zmax, zlogmin);

        if (DEBUG) {
            System.out.println("zmin = " + zmin);
            System.out.println("zmax = " + zmax);
            System.out.println("zlogmin = " + zlogmin);
            System.out.println("dispZmin = " + dispZmin);
            System.out.println("dispZmax = " + dispZmax);
            System.out.println("zrange = " + zrange);
        }
    }

    public boolean isLogScale() {
        return logScale;
    }

    public double getLowerBound() {
        return zmin;
    }

    public double getUpperBound() {
        return zmax;
    }

    public void setZParameters(double[] zlimits) {
        setZParameters(zlimits[0], zlimits[1], zlimits[2]);
    }

    public void setZParameters(double zmin, double zmax, double zlogmin) {
        this.zmin = zmin;
        this.zmax = zmax;
        this.zlogmin = zlogmin;

        dispZmin = zmin;
        dispZmax = zmax;

        if (logScale) {
            dispZmin = Math.log(zlogmin);
            dispZmax = Math.log(zmax);
        } else {
            if ((dispZmin > 0) && (dispZmax > 0)) {
                dispZmin = 0;
            }
            if ((dispZmin < 0) && (dispZmax < 0)) {
                dispZmax = 0;
            }
        }

        zrange = dispZmax - dispZmin;
    }

    private double getNormalizedValue(double value) {
        return (logScale ? ((Math.log(value) - dispZmin) / zrange) : ((value - dispZmin) / zrange));
    }

    private static final double alpha = 1.0f;

    private Color getRainbowColor(double value) {
        if (DEBUG)
            System.out.println("value = " + value);
        if (value == 0.)
            return Color.white;
        if (value < 0)
            throw new IllegalArgumentException("value is less than zero: " + value);
        value = getNormalizedValue(value);
        if (DEBUG)
            System.out.println("normalized value = " + value);
        int bin = (int) Math.floor(value * red.length);
        if (bin >= red.length)
            bin = red.length - 1;
        if (DEBUG) {
            System.out.println("bin = " + bin);
        }
        int a = (int) (alpha * 256);
        if (a < 0) {
            a = 0;
        }
        if (a > 255) {
            a = 255;
        }
        if (DEBUG) {
            System.out.println("alpha = " + a);
            System.out.println("rgba = " + red[bin] + ", " + green[bin] + ", " + blue[bin] + ", " + a);
            System.out.println("----------------");
        }
        // DEBUG
        if (bin < 0) {
            System.err.println("WARNING: bin value < 0 is being ignored");
            return Color.white;
        }
        return new Color(red[bin], green[bin], blue[bin], a);
    }

    public Paint getPaint(double value) {
        return getRainbowColor(value);
    }

    /** The red values. */
    private final static int[] red = { 120, 115, 111, 106, 102, 97, 93, 88, 84,
	    79, 75, 70, 66, 61, 57, 52, 48, 43, 39, 34, 30, 25, 21, 16, 12, 7,
	    3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 1, 5, 10, 14, 19, 23, 28, 32, 37, 41, 46, 50, 55, 59,
	    64, 68, 73, 77, 82, 86, 91, 95, 100, 104, 109, 113, 118, 123, 127,
	    132, 136, 141, 145, 150, 154, 159, 163, 168, 172, 177, 181, 186,
	    190, 195, 199, 204, 208, 213, 217, 222, 226, 231, 235, 240, 244,
	    249, 253, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255 };

    /** The green values. */
    private final static int[] green = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 6, 11, 15, 20, 24, 29,
	    33, 38, 42, 47, 51, 56, 60, 65, 69, 74, 78, 83, 87, 92, 96, 101,
	    105, 110, 114, 119, 123, 128, 132, 137, 141, 146, 150, 155, 159,
	    164, 168, 173, 177, 182, 186, 191, 195, 200, 204, 209, 213, 218,
	    222, 227, 231, 236, 241, 245, 250, 254, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 252, 248, 243, 239, 234, 230, 225, 221, 216,
	    212, 207, 203, 198, 194, 189, 185, 180, 176, 171, 167, 162, 158,
	    153, 149, 144, 140, 135, 131, 126, 122, 117, 113, 108, 104, 99, 95,
	    90, 86, 81, 77, 72, 68, 63, 59, 54, 50, 45, 41, 36, 32, 27, 23, 18,
	    14, 9, 5, 0 };

    /** The blue values. */
    private final static int[] blue = { 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
	    255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 251, 247,
	    242, 238, 233, 229, 224, 220, 215, 211, 206, 202, 197, 193, 188,
	    184, 179, 175, 170, 166, 161, 157, 152, 148, 143, 139, 134, 130,
	    125, 121, 116, 112, 107, 103, 98, 94, 89, 85, 80, 76, 71, 67, 62,
	    58, 53, 49, 44, 40, 35, 31, 26, 22, 17, 13, 8, 4, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
}
