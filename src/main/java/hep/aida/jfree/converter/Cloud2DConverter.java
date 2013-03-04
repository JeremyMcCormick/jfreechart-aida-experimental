package hep.aida.jfree.converter;

import hep.aida.ICloud2D;
import hep.aida.IPlotterStyle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class Cloud2DConverter implements HistogramConverter<ICloud2D>
{
    
    //final static public int POINT_DATA = 0;

    public Class<ICloud2D> convertsType()
    {
        return ICloud2D.class;
    }

    // Convert 2D cloud to chart.
    public JFreeChart convert(ICloud2D c2d, IPlotterStyle style)
    {
        // Create dataset.
        XYSeriesCollection dataset = DatasetConverter.convert(c2d);

        // Create chart.
        JFreeChart chart = ChartFactory.createScatterPlot(
                c2d.title(), 
                null, 
                null, 
                dataset, 
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false);

        return chart;
    }
}