package hep.aida.jfree.converter;

import hep.aida.IPlotterStyle;
import hep.aida.IProfile1D;
import hep.aida.jfree.dataset.Profile1DAdapter;

import org.jfree.chart.JFreeChart;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Profile1DConverter implements Converter<IProfile1D> {

    public Class<IProfile1D> convertsType() {
        return IProfile1D.class;
    }

    public JFreeChart convert(JFreeChart chart, IProfile1D obj, IPlotterStyle style) {
        Profile1DAdapter adapter = new Profile1DAdapter(obj);
        return new Histogram1DConverter().convert(chart, adapter, style);
    }
}
