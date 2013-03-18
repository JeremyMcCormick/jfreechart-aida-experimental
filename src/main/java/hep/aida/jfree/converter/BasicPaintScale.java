package hep.aida.jfree.converter;

import org.jfree.chart.renderer.GrayPaintScale;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public abstract class BasicPaintScale extends GrayPaintScale {

    public BasicPaintScale(double upperBound, double lowerBound) {
        super(upperBound, lowerBound);
    }

    public BasicPaintScale() {
    }
}
