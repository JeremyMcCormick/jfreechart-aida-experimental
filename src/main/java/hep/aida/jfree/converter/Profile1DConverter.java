package hep.aida.jfree.converter;

import hep.aida.IPlotterStyle;
import hep.aida.IProfile1D;

import org.jfree.chart.JFreeChart;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Profile1DConverter implements HistogramConverter<IProfile1D>
{
    public Class<IProfile1D> convertsType()
    {
        return IProfile1D.class;
    }

    public JFreeChart convert(IProfile1D obj, IPlotterStyle style)
    {
        Profile1DAdapter adapter = new Profile1DAdapter(obj);
        JFreeChart chart = Histogram1DConverter.convertFullDatasets(adapter);
        return chart;
    }


}
