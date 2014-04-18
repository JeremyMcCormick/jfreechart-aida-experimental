package hep.aida.jfree.dataset;

import static hep.aida.jfree.dataset.Histogram1DAdapter.ERRORS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.POINTS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.STEPS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.VALUES;
import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.jfree.AnalysisFactory;
import hep.aida.jfree.chart.DefaultChartTheme;
import hep.aida.jfree.converter.ConverterUtil;

import java.awt.Color;
import java.util.Random;

import junit.framework.TestCase;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Histogram1DAdapterTest extends TestCase {
    
    IAnalysisFactory analysisFactory;
    IHistogramFactory histogramFactory;
    
    protected void setUp() {
        AnalysisFactory.register();
        analysisFactory = IAnalysisFactory.create();
        histogramFactory = analysisFactory.createHistogramFactory(null);
    }
    
    public void testHistogram1DAdapter() {
        
        IHistogram1D histogram = histogramFactory.createHistogram1D("h1d", 100, 0, 100);
        Random random = new Random();
        for (int i=0; i<100000; i++) {
            histogram.fill(random.nextInt(100));
        }
        
        Histogram1DAdapter adapter = new Histogram1DAdapter(histogram);
        
        //int getSeriesCount() 
        int seriesCount = adapter.getSeriesCount();
        System.out.println("seriesCount: " + seriesCount);
        
        //int getItemCount(int series)
        int itemCount = adapter.getItemCount(VALUES);
        System.out.println("itemCount: " + itemCount);

        //Comparable getSeriesKey(int series)
        Comparable<?> seriesKey = adapter.getSeriesKey(VALUES);
        System.out.println("data series key: " + seriesKey);
        
        seriesKey = adapter.getSeriesKey(ERRORS);
        System.out.println("error series key: " + seriesKey);
        
        System.out.println();
        
        for (int item = 0; item < itemCount; item++) {
            
            System.out.println("item " + item);
            
            double x = adapter.getXValue(VALUES, item);
            double xLow = adapter.getStartXValue(VALUES, item);            
            double xHigh = adapter.getEndXValue(VALUES, item);
                        
            double y = adapter.getYValue(VALUES, item);
            double yLow = adapter.getStartYValue(VALUES, item);
            double yHigh = adapter.getEndYValue(VALUES, item);
            
            System.out.format("  bin data (x,xLow,xHigh,y,yLow,yHigh) = %f,%f,%f,%f,%f,%f%n",
                    x, xLow, xHigh, y, yLow, yHigh);
            
            x = adapter.getXValue(ERRORS, item);
            xLow = adapter.getStartXValue(ERRORS, item);            
            xHigh = adapter.getEndXValue(ERRORS, item);
                        
            y = adapter.getYValue(ERRORS, item);
            yLow = adapter.getStartYValue(ERRORS, item);
            yHigh = adapter.getEndYValue(ERRORS, item);
            
            System.out.format("  error data (x,xLow,xHigh,y,yLow,yHigh) = %f,%f,%f,%f,%f,%f%n",
                    x, xLow, xHigh, y, yLow, yHigh);
            
            x = adapter.getXValue(POINTS, item);
            y = adapter.getYValue(POINTS, item);
            System.out.format("  point data (x,y) = %f,%f%n", x, y);            
        }        
        System.out.println();
        System.out.println();
        
        System.out.println("printing step data ...");
        itemCount = adapter.getItemCount(STEPS);
        System.out.println("step itemCount: " + itemCount);
        for (int item = 0; item < itemCount; item++) {
            System.out.println("item " + item);
            double x = adapter.getXValue(STEPS, item);
            double y = adapter.getYValue(STEPS, item);
            //double y = adapter.getYValue(STEPS, item);        
            System.out.format("  step data (x,y) = %f,%f%n", x, y);
        }        
        
        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(histogram);
        NumberAxis xAxis = new NumberAxis(labels[0]);
        NumberAxis yAxis = new NumberAxis(labels[1]);                
        
        // Create the plot without any data.
        XYPlot plot = new XYPlot(null, xAxis, yAxis, null);
        
        // Bar chart
        XYDataset barData = new Histogram1DAdapter(histogram);
        XYBarRenderer barRenderer = new XYBarRenderer();
        barRenderer.setSeriesVisible(ERRORS, false);
        barRenderer.setSeriesVisible(POINTS, false);
        barRenderer.setSeriesVisible(STEPS, false);
        //barRenderer.setSeriesVisible(VALUES, false); // DEBUG
        barRenderer.setDrawBarOutline(true);
        plot.setDataset(VALUES, barData);
        plot.setRenderer(VALUES, barRenderer);
                
        // Errors
        XYDataset errors = new Histogram1DAdapter(histogram);
        XYErrorRenderer errorRenderer = new XYErrorRenderer();
        errorRenderer.setSeriesVisible(VALUES, false);
        errorRenderer.setSeriesVisible(POINTS, false);
        errorRenderer.setSeriesVisible(STEPS, false);
        //errorRenderer.setSeriesVisible(ERRORS, false); // DEBUG
        errorRenderer.setBaseShapesVisible(false);
        errorRenderer.setSeriesPaint(0, Color.black);
        plot.setDataset(ERRORS, errors);
        plot.setRenderer(ERRORS, errorRenderer);
        
        // Points
        XYDataset points = new Histogram1DAdapter(histogram);
        XYItemRenderer pointRenderer = new XYLineAndShapeRenderer();
        pointRenderer.setSeriesVisible(VALUES, false);
        pointRenderer.setSeriesVisible(ERRORS, false);
        pointRenderer.setSeriesVisible(STEPS, false);
        //pointRenderer.setSeriesVisible(POINTS, false); // DEBUG
        plot.setDataset(POINTS, points);
        plot.setRenderer(POINTS, pointRenderer);
        
        // Steps
        XYDataset steps = new Histogram1DAdapter(histogram);
        XYItemRenderer stepRenderer = new XYStepRenderer();
        stepRenderer.setSeriesVisible(VALUES, false);
        stepRenderer.setSeriesVisible(ERRORS, false);
        stepRenderer.setSeriesVisible(POINTS, false);
        //stepRenderer.setSeriesVisible(STEPS, false);
        plot.setDataset(STEPS, steps);
        plot.setRenderer(STEPS, stepRenderer);
        
        // Set proper rendering order.
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Set style.
        ChartFactory.setChartTheme(new DefaultChartTheme());
        XYBarRenderer.setDefaultShadowsVisible(false);
        
        // Create the chart.
        JFreeChart chart = new JFreeChart(histogram.title(), JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        
        // Show the chart in an application frame.
        ChartPanel panel = new ChartPanel(chart);        
        ApplicationFrame frame = new ApplicationFrame("Histogram1DAdapterTest");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        
        while (true) {
        }        
    }

}
