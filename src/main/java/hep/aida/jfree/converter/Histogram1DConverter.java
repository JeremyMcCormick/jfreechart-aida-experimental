package hep.aida.jfree.converter;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYDataset;

/**
 * Convert an AIDA <code>IHistogram1D</code> to a <code>JFreeChart</code> object.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DConverter implements Converter<IHistogram1D> {

    static final double DEFAULT_X_AXIS_MARGIN = 0.05;
    static final double DEFAULT_Y_AXIS_MARGIN = 0.1;
    
    public Class<IHistogram1D> convertsType() {
        return IHistogram1D.class;
    }

    public JFreeChart convert(IHistogram1D histogram, IPlotterStyle style) {
        
        // Create the backing datasets, which are actually adapters.
        XYDataset[] datasets = ConverterUtil.createDatasets(histogram);

        // Create the renderers.
        XYItemRenderer[] renderers = ConverterUtil.createHistogramRenderers(datasets);
                        
        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(histogram);
        
        // Configure the X axis.
        NumberAxis xAxis = new NumberAxis(labels[0]);
        xAxis.setAutoRange(true);
        xAxis.setDefaultAutoRange(new Range(histogram.axis().lowerEdge(), histogram.axis().upperEdge()));
        xAxis.setUpperMargin(DEFAULT_X_AXIS_MARGIN);
        
        // Configure the Y axis.
        NumberAxis yAxis = new NumberAxis(labels[1]);
        yAxis.setAutoRange(true);
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setUpperMargin(DEFAULT_Y_AXIS_MARGIN);
        if (histogram.maxBinHeight() > 0.0)
            yAxis.setAutoRangeMinimumSize(histogram.maxBinHeight());
        yAxis.setRangeType(RangeType.POSITIVE);

        return ConverterUtil.createHistogramChart(histogram.title(), xAxis, yAxis, datasets, renderers);
    }   
}
