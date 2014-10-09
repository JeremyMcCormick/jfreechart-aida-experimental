package hep.aida.jfree.converter;

import hep.aida.ICloud2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.Cloud2DAdapter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class Cloud2DConverter implements Converter<ICloud2D> {

    public Class<ICloud2D> convertsType() {
        return ICloud2D.class;
    }

    // Convert 2D cloud to chart.
    public JFreeChart convert(JFreeChart chart, ICloud2D cloud, IPlotterStyle style) {

        // Create dataset.
        Cloud2DAdapter adapter = new Cloud2DAdapter(cloud);

        // Create chart.
        return ChartFactory.createScatterPlot(
                cloud.title(), null, null, adapter, 
                PlotOrientation.VERTICAL, true, true, false);
    }
}