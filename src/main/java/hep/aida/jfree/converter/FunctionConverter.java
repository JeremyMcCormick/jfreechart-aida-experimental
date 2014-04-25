package hep.aida.jfree.converter;

import hep.aida.IFunction;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.function.Function2DAdapter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class FunctionConverter { 
//implements Converter<IFunction> {
    
    private static int SAMPLES_FACTOR = 3;
    
    public static void addFunction(JFreeChart chart, IFunction function) {
        if (chart == null) {
            throw new RuntimeException("Cannot add function.  Chart is null.");
        }
                
        XYDataset functionData = toXYDataset(
                function, 
                chart.getXYPlot().getDomainAxis().getLowerBound(),
                chart.getXYPlot().getDomainAxis().getUpperBound(),
                chart.getXYPlot().getDataset().getItemCount(0) * SAMPLES_FACTOR);
                        
        int index = chart.getXYPlot().getDatasetCount();
        
        chart.getXYPlot().setDataset(index, functionData);
        chart.getXYPlot().setRenderer(index, new XYLineAndShapeRenderer());
    }
    
    //@Override
    //public Class<IFunction> convertsType() {
    //    return IFunction.class;
    //}

    //@Override
    /*
    public JFreeChart convert(IFunction object, IPlotterStyle style) {        
        
        // Create the chart.
        JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        
        if (chart == null) {
            throw new RuntimeException("Cannot add function.  Chart is null.");
        }
                
        XYDataset functionData = toXYDataset(
                function, 
                chart.getXYPlot().getDomainAxis().getLowerBound(),
                chart.getXYPlot().getDomainAxis().getUpperBound(),
                chart.getXYPlot().getDataset().getItemCount(0) * SAMPLES_FACTOR);
                        
        int index = chart.getXYPlot().getDatasetCount();
        
        chart.getXYPlot().setDataset(index, functionData);
        chart.getXYPlot().setRenderer(index, new XYLineAndShapeRenderer());
    }
    */

    private static XYDataset toXYDataset(IFunction function, double start, double end, int samples) {
        return DatasetUtilities.sampleFunction2D(new Function2DAdapter(function), start, end, samples, "functionData");
    }
}
