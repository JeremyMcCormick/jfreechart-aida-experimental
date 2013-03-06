package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;
import hep.aida.ICloud2D;
import hep.aida.IHistogram1D;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class StyleConverterFactory
{
    private static final Histogram1DStyleConverter histogram1DConverter = new Histogram1DStyleConverter();
    private static final Cloud2DStyleConverter cloud2DConverter = new Cloud2DStyleConverter();    
    
    public static StyleConverter getStyleConverter(IBaseHistogram hist) 
    {
        if (hist instanceof IHistogram1D) {
            return histogram1DConverter;
        } else if (hist instanceof ICloud2D) {
            return cloud2DConverter;
        } else {
            return null;
        }
    }
}