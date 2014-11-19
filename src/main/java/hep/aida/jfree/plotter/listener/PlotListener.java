package hep.aida.jfree.plotter.listener;

import hep.aida.ref.event.AIDAListener;
import hep.aida.ref.event.IsObservable;

import java.util.EventObject;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.data.xy.XYDataset;

/**
 * This listener class is used to update the JFreeChart backend 
 * on the fly as AIDA objects are changed.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public abstract class PlotListener<T> implements AIDAListener, ChartProgressListener {

    JFreeChart chart;
    T plot;
    XYDataset dataset;
    boolean isValid;
    
    protected PlotListener() {
    }
    
    /**
     * Create a listener for the given plot with the associated JFree chart and dataset.
     * @param hist The backing histogram.
     * @param chart The corresponding chart for the histogram.
     * @param datasetIndices The indices of the datasets corresponding to the histogram in the chart.
     */
    PlotListener(T plot, JFreeChart chart, XYDataset dataset) {        
        this.chart = chart;
        this.plot = plot;
        this.dataset = dataset;
        if (!(plot instanceof IsObservable))
            throw new IllegalArgumentException("The object does not implement IsObservable.");
        ((IsObservable) plot).addListener(this);
        chart.addProgressListener(this);
    }
       
    /** 
     * Start the task to update the plot graphics.
     * @param e The EventObject, which is unused.
     */
    public void stateChanged(EventObject e) {
        update();
        isValid = false;
    }
    
    /**
     * This method updates the JFreeChart plot based on changes to the AIDA object.
     * The default implementation just fires a chart changed event to indicate
     * the chart should be redrawn.
     */
    public void update() {   
        chart.fireChartChanged();        
    }
    
    @Override
    public void chartProgress(ChartProgressEvent event) {
        if (event.getType() == ChartProgressEvent.DRAWING_FINISHED) {
            isValid = true;
            ((IsObservable) plot).setValid(this);
        }
    }           
}