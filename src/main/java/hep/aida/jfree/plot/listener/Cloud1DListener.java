package hep.aida.jfree.plot.listener;

import hep.aida.ICloud1D;
import hep.aida.IHistogram1D;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;

public class Cloud1DListener extends PlotListener<ICloud1D> {
    
    ICloud1D cloud;
        
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
    
    static void reconfigureAxes(JFreeChart chart, IHistogram1D histogram) {
        
        ValueAxis xAxis = chart.getXYPlot().getRangeAxis();
        xAxis.setDefaultAutoRange(new Range(histogram.axis().lowerEdge(), histogram.axis().upperEdge()));
        xAxis.setUpperMargin(0.1);
        
        ValueAxis yAxis = chart.getXYPlot().getDomainAxis();
        yAxis.setUpperMargin(0.25);
        if (histogram.maxBinHeight() > 0.0)
            yAxis.setAutoRangeMinimumSize(histogram.maxBinHeight());        
    }       
}
