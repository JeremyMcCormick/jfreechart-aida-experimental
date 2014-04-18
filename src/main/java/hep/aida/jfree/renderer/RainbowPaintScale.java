package hep.aida.jfree.renderer;

/**
 *   
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class RainbowPaintScale extends AbstractPaintScale {
       
    public RainbowPaintScale(double minimum, double maximum) {
        colorScale = MultiGradientScale.makeRainbowScale(minimum, maximum);
    }
}
