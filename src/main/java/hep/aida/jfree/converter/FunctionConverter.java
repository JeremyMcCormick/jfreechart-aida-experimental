package hep.aida.jfree.converter;

import hep.aida.IFunction;
import hep.aida.jfree.dataset.DatasetConverter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class FunctionConverter {
    
    public static void addFunction(JFreeChart chart, IFunction function) {
        if (chart == null) {
            throw new RuntimeException("Cannot add function.  Chart is null.");
        }
        
        //System.out.println("fitting...");
        //System.out.println("lowerBound = " + chart.getXYPlot().getDomainAxis().getLowerBound());
        //System.out.println("upperBound = " + chart.getXYPlot().getDomainAxis().getUpperBound());
        //System.out.println("samples = " + chart.getXYPlot().getDataset().getItemCount(0) * 3);
        
        XYDataset functionData = DatasetConverter.toXYDataset(
                function, 
                chart.getXYPlot().getDomainAxis().getLowerBound(),
                chart.getXYPlot().getDomainAxis().getUpperBound(),
                chart.getXYPlot().getDataset().getItemCount(0) * 3);
        
        //System.out.println("functionData size = " + functionData.getItemCount(0));
                        
        int index = chart.getXYPlot().getDatasetCount();
        
        chart.getXYPlot().setDataset(index, functionData);
        chart.getXYPlot().setRenderer(index, new XYLineAndShapeRenderer());
    }
}
