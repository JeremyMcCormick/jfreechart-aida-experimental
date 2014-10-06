package hep.aida.jfree.converter;

import hep.aida.ICloud1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.Cloud1DAdapter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYDataset;

/**
 * Convert an AIDA <code>ICloud1D</code> to a <code>JFreeChart</code> object.
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
        XYDataset[] datasets = createDatasets(cloud);
        
        // Create the renderers.
        XYItemRenderer[] renderers = Histogram1DConverter.createHistogramRenderers(datasets);
                       
        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(cloud);
        
        // Configure the X axis.
        NumberAxis xAxis = new NumberAxis(labels[0]);
        xAxis.setAutoRange(true);
        xAxis.setUpperMargin(0.05);
        xAxis.setLowerMargin(0.05);
        
        // Configure the Y axis.
        NumberAxis yAxis = new NumberAxis(labels[1]);
        yAxis.setRangeType(RangeType.POSITIVE);
        yAxis.setAutoRange(true);
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setUpperMargin(0.1);
        
        // Create the chart with the data.
        return Histogram1DConverter.createHistogramChart(
                cloud.title(), 
                xAxis, 
                yAxis, 
                datasets, 
                renderers);        
    }
    
    static XYDataset[] createDatasets(ICloud1D cloud) {
        XYDataset[] datasets = new XYDataset[4];
        Cloud1DAdapter adapter = new Cloud1DAdapter(cloud);
        for (int i=0; i<=3; i++) {
            datasets[i] = adapter;
        }
        return datasets;
    }    
}
