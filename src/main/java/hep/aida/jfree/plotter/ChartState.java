package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

public final class ChartState {

    private ChartPanel panel = null;
    private XYPlot plot = null;
    private JFreeChart chart = null;
    private IBaseHistogram histogram = null;

    ChartState(
            ChartPanel panel, 
            JFreeChart chart, 
            IBaseHistogram histogram) {
        this.panel = panel;
        this.chart = chart;
        this.plot = chart.getXYPlot();
        this.histogram = histogram;
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
}