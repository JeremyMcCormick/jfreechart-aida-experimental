package hep.aida.jfree.plot.listener;

import hep.aida.ref.event.AIDAListener;
import hep.aida.ref.event.AIDAObservable;

import java.util.EventObject;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.chart.JFreeChart;

/**
 * This listener class is assigned to histograms so that the JFreeChart backend
 * can update its plots on the fly when the fill method is called.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public abstract class PlotListener<T> implements AIDAListener {

    JFreeChart chart;
    T plot;
    private final int DEFAULT_INTERVAL = 100; // in ms
    int updateInterval = DEFAULT_INTERVAL;
    int[] datasetIndices;

    /**
     * 
     * @param hist The backing histogram.
     * @param chart The corresponding chart for the histogram.
     * @param datasetIndices The indices of the datasets corresponding to the histogram in the chart.
     */
    PlotListener(T plot, JFreeChart chart, int[] datasetIndices) {
        if (!(plot instanceof AIDAObservable))
            throw new IllegalArgumentException("The plot object is not an instance of AIDAObservable.");
        this.chart = chart;
        this.plot = plot;
        this.datasetIndices = datasetIndices;
    }

    /**
     * 
     * @param hist The backing histogram.
     * @param chart The corresponding chart for the histogram.
     * @param datasetIndices The indices of the datasets corresponding to the histogram in the chart.
     */
    PlotListener(T plot, JFreeChart chart, int[] datasetIndices, int updateInterval) {
        if (!(plot instanceof AIDAObservable))
            throw new IllegalArgumentException("The plot object is not an instance of AIDAObservable.");
        this.chart = chart;
        this.plot = plot;
        this.datasetIndices = datasetIndices;
        this.updateInterval = updateInterval;
    }

    /** 
     * Start the task to update the plot graphics.
     * @param e The EventObject, which is unused.
     */
    public void stateChanged(EventObject e) {
        Timer updateTimer = new Timer();
        updateTimer.schedule(new UpdateTask(this), updateInterval);
    }

    /**
     * The <tt>TimerTask</tt> for updating the plot graphics.
     */
    class UpdateTask extends TimerTask {

        PlotListener<T> listener;

        UpdateTask(PlotListener<T> listener) {
            this.listener = listener;
        }

        public void run() {
            // Update the plot.
            update();

            // Set valid for next callback.
            ((AIDAObservable) plot).setValid(listener);
        }
    }

    /**
     * This method updates the JFreeChart plot based on changes to the AIDA object.
     * Sub-classes should override this method with specific implementations.
     */
    public abstract void update();
}
