package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IFunction;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

/**
 * This class represents combined chart information between AIDA and JFreeChart, 
 * as created and/or used by the plotter regions.  This information is used 
 * primarily to apply styles from IPlotterStyle to JFreeChart objects.  It 
 * simplifies the external API of the style converter classes, which can get 
 * their information from this state object.
 *  
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 *
 */
public final class ChartState {

    private ChartPanel panel;
    private XYPlot plot;
    private JFreeChart chart;
    private IBaseHistogram histogram;    
    private IFunction function;
    private int datasetIndex = 0;

    ChartState(ChartPanel panel, JFreeChart chart, IBaseHistogram histogram, IFunction function, int datasetIndex) {
        this.panel = panel;
        this.chart = chart;
        this.plot = chart.getXYPlot();
        this.histogram = histogram;
        this.function = function;   
        this.datasetIndex = datasetIndex;
    }
    
    ChartState(ChartPanel panel, JFreeChart chart, IBaseHistogram histogram, IFunction function) {
        this.panel = panel;
        this.chart = chart;
        this.plot = chart.getXYPlot();
        this.histogram = histogram;
        this.function = function;   
    }
    
    ChartState(ChartPanel panel, JFreeChart chart, IFunction function, int datasetIndex) {
        this.panel = panel;
        this.chart = chart;
        this.plot = chart.getXYPlot();
        this.function = function;
        this.datasetIndex = datasetIndex;
    }
    
    ChartState(ChartPanel panel, JFreeChart chart, IBaseHistogram histogram, int datasetIndex) {
        this.panel = panel;
        this.chart = chart;
        this.plot = chart.getXYPlot();
        this.histogram = histogram;
        this.datasetIndex = datasetIndex;
    }
    
    ChartState(ChartPanel panel, JFreeChart chart, IBaseHistogram histogram) {
        this.panel = panel;
        this.chart = chart;
        this.plot = chart.getXYPlot();
        this.histogram = histogram;
    }               
    
    public int getDatasetIndex() {
        return datasetIndex;
    }

    public void setPanel(ChartPanel panel) {
        this.panel = panel;
    }

    public ChartPanel panel() {
        return panel;
    }

    public XYPlot plot() {
        return plot;
    }

    public JFreeChart chart() {
        return chart;
    }

    public IBaseHistogram histogram() {
        return histogram;
    }
    
    public IFunction function() {
       return function; 
    }
}