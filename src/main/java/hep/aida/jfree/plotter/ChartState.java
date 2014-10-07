package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IFunction;
import hep.aida.IPlotterStyle;

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
    //private XYPlot plot;
    private JFreeChart chart;
    private IBaseHistogram histogram;    
    private IFunction function;
    private int datasetIndex = 0; /* FIXME: This should be an array of indices. */
    private IPlotterStyle style;
    
    public void setHistogram(IBaseHistogram histogram) {
        this.histogram = histogram;
    }
    
    public void setPlotterStyle(IPlotterStyle style) {
        this.style = style;
    }
    
    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }
    
    public void setDatasetIndex(int datasetIndex) {
        this.datasetIndex = datasetIndex;
    }
                
    public int getDatasetIndex() {
        return datasetIndex;
    }

    public void setPanel(ChartPanel panel) {
        this.panel = panel;
    }

    public void setFunction(IFunction function) {
        this.function = function;
    }
    
    public ChartPanel getPanel() {
        return panel;
    }

    public JFreeChart getChart() {
        return chart;
    }

    public IBaseHistogram getHistogram() {
        return histogram;
    }
    
    public IFunction getFunction() {
       return function; 
    }
}