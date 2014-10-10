package hep.aida.jfree.plotter.listener;

import hep.aida.ref.event.AIDAListener;
import hep.aida.ref.event.IsObservable;

import java.util.EventObject;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

/**
 * This listener class is used to update the JFreeChart backend 
 * on the fly as AIDA objects are changed.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public abstract class PlotListener<T> implements AIDAListener {

    JFreeChart chart;
    T plot;
    XYDataset dataset;
    private final int DEFAULT_INTERVAL = 1000;
    int updateInterval = DEFAULT_INTERVAL;
    Timer updateTimer = new Timer();

    protected PlotListener() {
    }
    
    /**
     * 
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
    }

    /**
     * 
     * @param hist The backing histogram.
     * @param chart The corresponding chart for the histogram.
     * @param datasetIndices The indices of the datasets corresponding to the histogram in the chart.
     */
    PlotListener(T plot, JFreeChart chart, XYDataset dataset, int updateInterval) {        
        this.chart = chart;
        this.plot = plot;
        this.dataset = dataset;
        this.updateInterval = updateInterval;
        if (!(plot instanceof IsObservable))
            throw new IllegalArgumentException("The object does not implement IsObservable.");
        ((IsObservable) plot).addListener(this);
    }
       
    /** 
     * Start the task to update the plot graphics.
     * @param e The EventObject, which is unused.
     */
    public void stateChanged(EventObject e) {
        synchronized(this) {
            // Boot other pending draw tasks off this timer.
            updateTimer.purge();
            
            // Schedule a draw task.
            updateTimer.schedule(new UpdateTask(this), updateInterval);
        }
    }

    /**
     * The <tt>TimerTask</tt> for updating the plot graphics.
     */
    class UpdateTask extends TimerTask {

        PlotListener<T> listener;

        protected UpdateTask() {
        }
        
        UpdateTask(PlotListener<T> listener) {
            this.listener = listener;
        }

        public void run() {
                        
            // Update the plot.
            update();

            // Set valid for next callback.
            ((IsObservable) plot).setValid(listener);
        }
    }

    /**
     * This method updates the JFreeChart plot based on changes to the AIDA object.
     * Sub-classes should override this method with specific implementations.
     */
    public synchronized void update() {
        chart.fireChartChanged();
    }
}
