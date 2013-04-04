package hep.aida.jfree.plot.style.converter;

import hep.aida.ICloud1D;
import hep.aida.ICloud2D;
import hep.aida.IFunction;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;
import hep.aida.IProfile1D;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class StyleConverterFactory {

    private static final Histogram1DStyleConverter histogram1DConverter = new Histogram1DStyleConverter();
    private static final Histogram2DStyleConverter histogram2DConverter = new Histogram2DStyleConverter();
    private static final Cloud2DStyleConverter cloud2DConverter = new Cloud2DStyleConverter();
    private static final FunctionStyleConverter functionConverter = new FunctionStyleConverter();

    // FIXME: Should this return a new object since the converters have some plot state?
    public static StyleConverter getStyleConverter(Object object) {
        if (object instanceof IHistogram1D || object instanceof ICloud1D || object instanceof IProfile1D) {
            return histogram1DConverter;
        } else if (object instanceof ICloud2D) {
            return cloud2DConverter;
        } else if (object instanceof IHistogram2D) {
            return histogram2DConverter;
        } else if (object instanceof IFunction) {
            return functionConverter;
        } else {
            return null;
        }
    }
}