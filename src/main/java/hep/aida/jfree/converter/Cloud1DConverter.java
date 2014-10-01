package hep.aida.jfree.converter;

import hep.aida.ICloud1D;
import hep.aida.IPlotterStyle;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYDataset;

/**
 * Convert an AIDA <code>IHistogram1D</code> to a <code>JFreeChart</code> object.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Cloud1DConverter implements Converter<ICloud1D> {

    static final double DEFAULT_X_AXIS_MARGIN = 0.05;
    static final double DEFAULT_Y_AXIS_MARGIN = 0.1;
    
    public Class<ICloud1D> convertsType() {
        return ICloud1D.class;
    }

    public JFreeChart convert(ICloud1D cloud, IPlotterStyle style) {
        
        // Create the backing datasets, which are actually adapters.
        XYDataset[] datasets = ConverterUtil.createDatasets(cloud);
        
        // Create the renderers.
        XYItemRenderer[] renderers = ConverterUtil.createHistogramRenderers(datasets);
                       
        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(cloud);
        
        // Configure X axis.
        NumberAxis xAxis = new NumberAxis(labels[0]);
        xAxis.setAutoRange(true);
        xAxis.setUpperMargin(DEFAULT_X_AXIS_MARGIN);
        
        // Configure Y axis.
        NumberAxis yAxis = new NumberAxis(labels[1]);
        yAxis.setAutoRange(true);
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setUpperMargin(DEFAULT_Y_AXIS_MARGIN);
        yAxis.setRangeType(RangeType.POSITIVE);

        // Create the chart with the histogram data.
        return ConverterUtil.createHistogramChart(cloud.title(), xAxis, yAxis, datasets, renderers);        
    }
}
