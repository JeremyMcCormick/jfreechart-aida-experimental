package hep.aida.jfree.converter;

import hep.aida.IFunction;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.function.Function2DAdapter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class FunctionConverter implements Converter<IFunction> { 
    
    private static int SAMPLES_FACTOR = 3;
    
    @Override
    public Class<IFunction> convertsType() {
        return IFunction.class;
    }
    
    public JFreeChart convert(JFreeChart chart, IFunction function, IPlotterStyle style) {
        
        if (chart == null)
            throw new RuntimeException("The chart is null.");
                
        XYDataset functionData = toXYDataset(
                function, 
                chart.getXYPlot().getDomainAxis().getLowerBound(),
                chart.getXYPlot().getDomainAxis().getUpperBound(),
                chart.getXYPlot().getDataset().getItemCount(0) * SAMPLES_FACTOR);
                        
        int index = chart.getXYPlot().getDatasetCount();
                
        chart.getXYPlot().setDataset(index, functionData);
        chart.getXYPlot().setRenderer(index, new XYLineAndShapeRenderer());
        
        return chart;
    }
      
    private static XYDataset toXYDataset(IFunction function, double start, double end, int samples) {
        return DatasetUtilities.sampleFunction2D(new Function2DAdapter(function), start, end, samples, "functionData");
    }
}
