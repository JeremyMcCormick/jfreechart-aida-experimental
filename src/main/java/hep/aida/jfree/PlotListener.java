package hep.aida.jfree;

import hep.aida.IBaseHistogram;
import hep.aida.ref.event.AIDAListener;
import hep.aida.ref.event.AIDAObservable;

import java.util.EventObject;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.chart.JFreeChart;

/**
 * This listener class is assigned to histograms so that the JFreeChart
 * backend can update its plots on the fly when the fill method is called.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
abstract class PlotListener implements AIDAListener
{
    JFreeChart chart;
    IBaseHistogram hist;
    boolean updating = false;
    private final int DEFAULT_INTERVAL = 100; // in ms
    int updateInterval = DEFAULT_INTERVAL;
    int[] datasetIndices; 
    
    /**
     * 
     * @param hist The backing histogram.
     * @param chart The corresponding chart for the histogram.
     * @param datasetIndices The indices of the datasets corresponding to the histogram in the chart.
     */
    PlotListener(IBaseHistogram hist, JFreeChart chart, int[] datasetIndices) 
    {
        this.chart = chart;
        this.hist = hist;
        this.datasetIndices = datasetIndices;
    }
        
    public void stateChanged(EventObject e)
    {        
        // Start task to update the plot graphics.
        Timer updateTimer = new Timer();
        updateTimer.schedule(new UpdateTask(this), updateInterval);
    }
    
    class UpdateTask extends TimerTask 
    {
        PlotListener listener;
        
        UpdateTask(PlotListener listener) {
            this.listener = listener;
        }
        
        public void run() {
            // Update the plot.
            update();
            
            // Set valid for next callback.
            ((AIDAObservable)hist).setValid(listener);
        }        
    }
    
    // This method should update the JFreeChart plot from the AIDA object.
    abstract void update();
}
