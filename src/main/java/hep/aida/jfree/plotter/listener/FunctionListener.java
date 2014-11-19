package hep.aida.jfree.plotter.listener;

import hep.aida.IFunction;
import hep.aida.jfree.converter.FunctionConverter;
import hep.aida.ref.function.FunctionChangedEvent;
import hep.aida.ref.function.FunctionDispatcher;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

public class FunctionListener extends PlotListener<IFunction> implements hep.aida.ref.function.FunctionListener, ChartProgressListener {
    
    double lowerBound;
    double upperBound;
    int nSamples;    
    
    public FunctionListener(IFunction function, JFreeChart chart, XYDataset dataset) {
        this.plot = function;
        this.chart = chart;
        this.dataset = dataset;
        if (function instanceof FunctionDispatcher) {
            ((FunctionDispatcher)function).addFunctionListener(this);
        }
        lowerBound = chart.getXYPlot().getDomainAxis().getLowerBound();
        upperBound = chart.getXYPlot().getDomainAxis().getUpperBound();
        nSamples = dataset.getItemCount(0) * FunctionConverter.SAMPLES_FACTOR;
        chart.addProgressListener(this);
    }
    
    @Override
    public void functionChanged(FunctionChangedEvent event) {
        // Is the plot valid for updating?
        if (isValid) {
            chart.setNotify(false);
            XYSeriesCollection functionData = (XYSeriesCollection)FunctionConverter.createXYDataset( 
                    plot, 
                    lowerBound,
                    upperBound,
                    nSamples);
            ((XYSeriesCollection)dataset).removeAllSeries();
            ((XYSeriesCollection)dataset).addSeries(functionData.getSeries(0));
            chart.setNotify(true);
            isValid = false;
        }
    }

    @Override
    public void chartProgress(ChartProgressEvent event) {
        if (event.getType() == ChartProgressEvent.DRAWING_FINISHED) {
            // Set valid for updating after drawing is done.
            isValid = true;
        } 
    }    
}
