package hep.aida.jfree.plot.listener;

import hep.aida.ICloud1D;
import hep.aida.IHistogram1D;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;

public class Cloud1DListener extends PlotListener<ICloud1D> {

    ICloud1D cloud;
    boolean configuredRange = false;

    Cloud1DListener(ICloud1D cloud, JFreeChart chart, int[] datasetIndices) {
        super(cloud, chart, datasetIndices);
        this.cloud = (ICloud1D) cloud;
    }

    public void update() {
        if (cloud.isConverted()) {
            reconfigureAxes(chart, cloud.histogram());
        }
        super.update();
    }

    void reconfigureAxes(JFreeChart chart, IHistogram1D histogram) {
       
        // The X axis only needs to be configured once.
        if (!configuredRange) {
            ValueAxis xAxis = chart.getXYPlot().getDomainAxis();
            xAxis.setAutoRange(false);
            xAxis.setRange(histogram.axis().lowerEdge(), histogram.axis().upperEdge());
            configuredRange = true;
        }

        // Make sure the Y axis has the correct range everytime.
        ValueAxis yAxis = chart.getXYPlot().getRangeAxis();
        yAxis.setAutoRange(false);
        yAxis.setRange(0, histogram.maxBinHeight() + (histogram.maxBinHeight() * 0.1));
    }
}
