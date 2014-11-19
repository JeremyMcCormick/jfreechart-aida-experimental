package hep.aida.jfree.plotter.listener;

import hep.aida.IHistogram1D;
import hep.aida.jfree.converter.Histogram1DConverter;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

/**
 * This is a listener to update display of a 1D histogram in JFreeChart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram1DListener extends PlotListener<IHistogram1D> {

    Histogram1DListener(IHistogram1D histogram, JFreeChart chart, XYDataset dataset) {
        super(histogram, chart, dataset);
    }
    
    @Override
    public void update() {
        double xMinSize = plot.maxBinHeight() + Histogram1DConverter.findMaxError(plot);
        if (xMinSize > 0.) {
            chart.getXYPlot().getRangeAxis().setAutoRangeMinimumSize(xMinSize);
            chart.getXYPlot().getRangeAxis().configure();
        }
        super.update();
    }
}
