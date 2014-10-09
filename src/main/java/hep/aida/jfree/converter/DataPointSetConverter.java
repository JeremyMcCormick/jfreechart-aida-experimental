package hep.aida.jfree.converter;

import static hep.aida.jfree.dataset.DataPointSetAdapter.ERRORS;
import static hep.aida.jfree.dataset.DataPointSetAdapter.LINES;
import static hep.aida.jfree.dataset.DataPointSetAdapter.VALUES;
import hep.aida.IDataPointSet;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.DataPointSetAdapter;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.RangeType;


public class DataPointSetConverter implements Converter<IDataPointSet> {

    @Override
    public Class<IDataPointSet> convertsType() {
        return IDataPointSet.class;
    }

    @Override
    public JFreeChart convert(JFreeChart chart, IDataPointSet dataPointSet, IPlotterStyle style) {
        
        // Create the dataset adapter.
        DataPointSetAdapter adapter = new DataPointSetAdapter(dataPointSet);
        
        // Create the error bar renderer.
        XYErrorRenderer errorRenderer = new XYErrorRenderer();
        errorRenderer.setBaseShapesVisible(false);
        errorRenderer.setSeriesPaint(ERRORS, Color.black);
        
        // Create the point renderer.
        XYLineAndShapeRenderer pointRenderer = new XYLineAndShapeRenderer();
        pointRenderer.setBaseShapesVisible(true);
        pointRenderer.setBaseLinesVisible(false);
        
        // Create the line renderer.
        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
        lineRenderer.setBaseShapesVisible(false);
        lineRenderer.setBaseLinesVisible(true);
                
        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(dataPointSet);
        
        // Configure the X axis.
        NumberAxis xAxis = new NumberAxis(labels[0]);
        xAxis.setTickUnit(new NumberTickUnit(1.0));
        xAxis.setRangeType(RangeType.POSITIVE);
        xAxis.setMinorTickMarksVisible(false);
        
        // Configure the Y axis.
        NumberAxis yAxis = new NumberAxis(labels[1]);
        yAxis.setMinorTickMarksVisible(false);
        yAxis.setAutoRange(true);
        yAxis.setUpperMargin(0.25);
        if (adapter.getDataPointSet().size() > 0) {
            yAxis.setAutoRangeMinimumSize(adapter.getMaxY());           
        }
        yAxis.setRangeType(RangeType.FULL);
        
        // Create new plot.
        XYPlot plot = new XYPlot(null, xAxis, yAxis, null);
        
        // Setup the renderers and datasets on the plot.
        plot.setRenderer(VALUES, pointRenderer);
        plot.setDataset(VALUES, adapter);
        plot.setRenderer(LINES, lineRenderer);
        plot.setDataset(LINES, adapter);
        plot.setRenderer(ERRORS, errorRenderer);
        plot.setDataset(ERRORS, adapter);
        
        // Turn off the renderering of all series.  
        // These will be activated later in the style code.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                plot.getRenderer(i).setSeriesVisible(j, false);
            }
        }        
               
        // Set proper rendering order.
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        // Create the chart.
        JFreeChart newChart = new JFreeChart(dataPointSet.title(), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        
        //configureAxes(chart, adapter);

        // Apply the default chart theme.
        ChartFactory.getChartTheme().apply(newChart);
        
        return chart;
    }       
}
