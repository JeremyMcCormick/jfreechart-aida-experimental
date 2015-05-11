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
    
    private double lowerBound;
    private double upperBound;
    private int nSamples;    
    private boolean isValid = true;
    
    public FunctionListener(IFunction function, JFreeChart chart, XYDataset dataset) {
    	super(function, chart, dataset);
        if (function instanceof FunctionDispatcher) {
            ((FunctionDispatcher)function).addFunctionListener(this);
        }
        this.lowerBound = chart.getXYPlot().getDomainAxis().getLowerBound();
        this.upperBound = chart.getXYPlot().getDomainAxis().getUpperBound();
        this.nSamples = dataset.getItemCount(0) * FunctionConverter.SAMPLES_FACTOR;
        chart.addProgressListener(this);
    }
    
    @Override
    public void functionChanged(FunctionChangedEvent event) {
        // Is the plot valid for updating?
        if (this.isValid) {
        	this.chart.setNotify(false);
            XYSeriesCollection functionData = (XYSeriesCollection)FunctionConverter.createXYDataset( 
            		this.plot, 
            		this.lowerBound,
            		this.upperBound,
            		this.nSamples);
            ((XYSeriesCollection)this.dataset).removeAllSeries();
            ((XYSeriesCollection)this.dataset).addSeries(functionData.getSeries(0));
            this.chart.setNotify(true);
            this.isValid = false;
        }
    }

    @Override
    public void chartProgress(ChartProgressEvent event) {
        if (event.getType() == ChartProgressEvent.DRAWING_FINISHED) {
            // Set valid for updating after drawing is done.
        	this.isValid = true;
        } 
    }    
}
