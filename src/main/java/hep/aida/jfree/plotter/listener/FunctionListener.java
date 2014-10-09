package hep.aida.jfree.plotter.listener;

import hep.aida.IFunction;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;


public class FunctionListener extends PlotListener<IFunction> {
    
    public FunctionListener(IFunction function, JFreeChart chart, XYDataset dataset) {
        super(function, chart, dataset);
    }
    
    public synchronized void update() {
        super.update();
    }    
}
