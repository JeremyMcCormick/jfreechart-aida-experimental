package hep.aida.jfree.renderer;

/**
 *   
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class GreyPaintScale extends AbstractPaintScale {
       
    public GreyPaintScale(double minimum, double maximum) {
        colorScale = GradientScale.makeGreyScale(minimum, maximum);
    }    
}
