package hep.aida.jfree.dataset;

import java.util.Random;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram2D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.AnalysisFactory;
import junit.framework.TestCase;

public class Histogram2DAdapterTest extends TestCase {

    protected IAnalysisFactory analysisFactory;
    protected IPlotterFactory plotterFactory;
    protected IHistogramFactory histogramFactory;
    protected IPlotter plotter;
    protected IPlotterStyle style;
    
    protected void setUp() {
        AnalysisFactory.register();
        analysisFactory = IAnalysisFactory.create();
        plotterFactory = analysisFactory.createPlotterFactory();
        histogramFactory = analysisFactory.createHistogramFactory(null);
        plotter = plotterFactory.create();
        style = plotter.style();
    }
    
    public void testHistogram2DAdapter() {
        
        IHistogram2D histogram = histogramFactory.createHistogram2D("an h2d", 100, 0., 100., 100, 0., 100.);
        
        Random rand = new Random();
        for (int i = 0; i < 100000; i++) {
            histogram.fill(rand.nextInt(100), rand.nextInt(100));
        }
        Histogram2DAdapter adapter = new Histogram2DAdapter(histogram);
        
        //int getSeriesCount() 
        int seriesCount = adapter.getSeriesCount();
        System.out.println("seriesCount: " + seriesCount);
        
        //int getItemCount(int series)
        int itemCount = adapter.getItemCount(0);
        System.out.println("itemCount: " + itemCount);

        //Comparable getSeriesKey(int series)
        Comparable seriesKey = adapter.getSeriesKey(0);
        System.out.println("seriesKey: " + seriesKey);
        
        System.out.println();
        
        for (int i = 0; i < itemCount; i++) {
            
            System.out.println("getting item " + i);
            int x = adapter.getX(0, i).intValue();
            int y = adapter.getY(0, i).intValue();
            double z = adapter.getZ(0, i).doubleValue();
            System.out.println("X,Y,Z = " + x + "," + y + "," + z);
            
            double binHeight = histogram.binHeight(x, y);
            System.out.println("binHeight = " + binHeight);
            
            System.out.println("----");
        }        
    }    
}
