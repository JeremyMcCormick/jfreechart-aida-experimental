package hep.aida.jfree.plotter.listener;

import hep.aida.ref.event.AIDAListener;
import hep.aida.ref.event.IsObservable;

import java.util.EventObject;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.data.xy.XYDataset;

/**
 * This listener class is used to update the JFreeChart graphics on the fly as AIDA objects are changed
 * by their fill methods being called.
 * 
 * @author <a href="mailto:jeremym@slac.stanford.edu">Jeremy McCormick</a>
 */
public abstract class PlotListener<T> implements AIDAListener, ChartProgressListener {

    protected final JFreeChart chart;
    protected final T plot;
    protected final XYDataset dataset;
        
    /**
     * Create a new plot listener.
     * 
     * @param hist the backing histogram
     * @param chart the corresponding chart for the histogram
     * @param datase the dataset corresponding to the histogram in the chart
     */
    PlotListener(T plot, JFreeChart chart, XYDataset dataset) {
    	if (plot == null) {
    		throw new IllegalArgumentException("The plot is null.");
    	}
    	if (chart == null) {
    		throw new IllegalArgumentException("The chart is null.");
    	}
    	if (dataset == null) {
    		throw new IllegalArgumentException("The dataset is null.");
    	}
    	
        this.chart = chart;        
        this.chart.addProgressListener(this); 
                
        this.dataset = dataset;
        
        this.plot = plot;
        if (!(plot instanceof IsObservable)) {
            throw new IllegalArgumentException("The plot object does not implement the AIDA IsObservable interface.");
        }
        ((IsObservable) plot).addListener(this);       
    }
       
    /** 
     * Start the task to update the plot graphics.
     * 
     * @param e The EventObject, which is unused.
     */
    public void stateChanged(EventObject e) {
    	update();    	
    }   

    /**
     * This method updates the JFreeChart plot based on changes to the AIDA object.
     * Sub-classes should override this method with specific implementations.
     */
    public synchronized void update() {
    	// This triggers a redraw of the chart.
        chart.fireChartChanged();
    }
    
    /**
     * Receive notification of a <code>ChartProgressEvent</code> which is used to 
     * set this object as valid for updates after a chart redraw has finished.
     * 
     * @param event the <code>ChartProgressEvent</code> to handle
     */
    public void chartProgress(ChartProgressEvent event) {
    	if (event.getType() == ChartProgressEvent.DRAWING_FINISHED) {
    		// Set this object as valid for the next callback from AIDA observable interface.
    		((IsObservable) plot).setValid(this);
    	}
    }
    
    /**
     * Get the <code>JFreeChart</code> associated with this listener.
     * 
     * @return the <code>JFreeChart</code> associated with this listener
     */
    protected JFreeChart getChart() {
    	return this.chart;
    }
    
    /**
     * Get the chart's dataset.
     * 
     * @return the chart's dataset
     */
    protected XYDataset getDataset() {
    	return this.dataset;
    }       
    
    protected T getPlot() {
    	return this.plot;
    }
}
