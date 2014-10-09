package hep.aida.jfree.plotter.style.converter;

import hep.aida.ICloud1D;
import hep.aida.ICloud2D;
import hep.aida.IDataPointSet;
import hep.aida.IFunction;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;
import hep.aida.IProfile1D;
import hep.aida.jfree.converter.DataPointSetConverter;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class StyleConverterFactory {

    public static StyleConverter create(Object object) {
        if (object instanceof IHistogram1D || object instanceof ICloud1D || object instanceof IProfile1D) {
            return new Histogram1DStyleConverter();
        } else if (object instanceof ICloud2D) {
            return new Cloud2DStyleConverter();
        } else if (object instanceof IHistogram2D) {
            return new Histogram2DStyleConverter();
        } else if (object instanceof IFunction) {
            return new FunctionStyleConverter();
        } else if (object instanceof IDataPointSet) {
            return new DataPointSetStyleConverter();
        } else {
            return new BaseStyleConverter();
        }
    }
}
