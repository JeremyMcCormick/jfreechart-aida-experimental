package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IFunction;
import hep.aida.IPlotterStyle;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * This class represents combined chart information between AIDA and JFreeChart, 
 * as created and/or used by the plotter regions.  This information is used 
 * primarily to apply styles from IPlotterStyle to JFreeChart objects.  It 
 * simplifies the external API of the style converter classes, which can get 
 * their information from this state object.
 *  
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
// TODO: Move to hep.aida.jfree.plot.style.converter.StyleConverterState and make only package accessible.
public final class ChartState {

    private ChartPanel panel;
    private JFreeChart chart;
    private Object plotObject;    
    private int[] datasetIndices;
    private IPlotterStyle style;
    
    public void setPlotObject(Object plotObject) {
        this.plotObject = plotObject;
    }
    
    public void setPlotterStyle(IPlotterStyle style) {
        this.style = style;
    }
    
    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }
    
    public void setDatasetIndices(int[] datasetIndices) {
        this.datasetIndices = datasetIndices;
    }
                
    public int[] getDatasetIndices() {
        return datasetIndices;
    }

    public void setPanel(ChartPanel panel) {
        this.panel = panel;
    }
    
    public ChartPanel getPanel() {
        return panel;
    }

    public JFreeChart getChart() {
        return chart;
    }
    
    public Object getPlotObject() {
        return plotObject;
    }

    public IBaseHistogram getHistogram() {
        return (IBaseHistogram)plotObject;
    }
    
    public IFunction getFunction() {
       return (IFunction)plotObject; 
    }
    
    public IPlotterStyle getPlotterStyle() {
        return style;
    }
}