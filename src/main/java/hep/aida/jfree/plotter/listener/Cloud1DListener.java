package hep.aida.jfree.plotter.listener;

import hep.aida.ICloud1D;
import hep.aida.IHistogram1D;
import hep.aida.jfree.converter.Histogram1DConverter;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

public class Cloud1DListener extends PlotListener<ICloud1D> {

    Cloud1DListener(ICloud1D cloud, JFreeChart chart, XYDataset dataset) {
        super(cloud, chart, dataset);
    }

    public void update() {                       
        if (this.plot.isConverted()) {            
            IHistogram1D histogram = this.plot.histogram();            
            this.chart.setNotify(false);
            double minBinSize = histogram.maxBinHeight() + Histogram1DConverter.findMaxError(histogram);
            if (minBinSize > 0.) {
                this.chart.getXYPlot().getRangeAxis().setAutoRangeMinimumSize(minBinSize);
                this.chart.getXYPlot().getRangeAxis().configure();
            }
            
            double xLower = histogram.axis().binLowerEdge(0);
            double xUpper = histogram.axis().binUpperEdge(histogram.axis().bins() - 1);
            if (xUpper > xLower) {
                this.chart.getXYPlot().getDomainAxis().setLowerBound(xLower);
                this.chart.getXYPlot().getDomainAxis().setUpperBound(xUpper);
            }
            this.chart.setNotify(true);
        }      
        super.update();
    }
}
