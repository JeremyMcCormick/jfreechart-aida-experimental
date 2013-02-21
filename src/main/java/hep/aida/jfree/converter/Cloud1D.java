package hep.aida.jfree.converter;

import hep.aida.ICloud1D;
import hep.aida.IPlotterStyle;

import org.jfree.chart.JFreeChart;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class Cloud1D implements Histogram<ICloud1D>
{

    public Class<ICloud1D> convertsType()
    {
        return ICloud1D.class;
    }

    // Convert 1D cloud to chart by first converting to histogram.
    public JFreeChart convert(ICloud1D c1d, IPlotterStyle style)
    {
        c1d.convertToHistogram();
        if (c1d.histogram() == null)
            throw new RuntimeException("Cloud did not convert to histogram successfully!");
        if (style != null && !style.dataStyle().lineStyle().isVisible()) {
            return Histogram1D.convertOutlineOnly(c1d.histogram());
        } else {
            return Histogram1D.convertDefault(c1d.histogram());
        }
    }
}
