package hep.aida.jfree.plot.listener;

import hep.aida.IFunction;

import org.jfree.chart.JFreeChart;


public class FunctionListener extends PlotListener<IFunction> {

    IFunction function;
    
    public FunctionListener(IFunction function, JFreeChart chart, int[] datasetIndices) {
        super(function, chart, datasetIndices);
        this.function = function;
    }
    
    public synchronized void update() {
        chart.fireChartChanged();
    }    
}
