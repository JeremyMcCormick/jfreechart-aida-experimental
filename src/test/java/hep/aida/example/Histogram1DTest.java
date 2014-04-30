package hep.aida.example;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.ITree;

import java.util.Random;

import junit.framework.TestCase;

public class Histogram1DTest extends TestCase {

    public void testFixedBin() throws Exception {
        IAnalysisFactory af = IAnalysisFactory.create();
        ITree tree = af.createTreeFactory().create();
        IHistogramFactory hf = af.createHistogramFactory(tree);

        IHistogram1D h1 = hf.createHistogram1D("Hist 1", 10, 0., 10.);
        h1.annotation().addItem("xAxisLabel", "The X Axis");
        h1.annotation().addItem("yAxisLabel", "The Y Axis");

        IHistogram1D h2 = hf.createHistogram1D("Hist 2", 10, 0., 10.);
        h2.annotation().addItem("xAxisLabel", "The X Axis");
        h2.annotation().addItem("yAxisLabel", "The Y Axis");

        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            h1.fill(rand.nextInt(10));
            h2.fill(rand.nextInt(10));
        }

        IPlotter plotter = af.createPlotterFactory().create("Hist Plotter");
        plotter.createRegion();
        plotter.region(0).plot(h1);
        plotter.region(0).plot(h2);

        plotter.style().statisticsBoxStyle().setVisible(true);

        plotter.writeToFile(this.getClass().getSimpleName() + ".png");
        // plotter.show();
    }
    
    public void testVariableBin() throws Exception {
        
        IAnalysisFactory analysisFactory = IAnalysisFactory.create();
        ITree tree = analysisFactory.createTreeFactory().create();
        IHistogramFactory histogramFactory = analysisFactory.createHistogramFactory(tree);

        double[] binEdges = { 1.0, 5.0, 10.0, 20.0, 40.0, 80.0, 100.0 };
        IHistogram1D histogram = histogramFactory.createHistogram1D(
                "/Hist1DVariableBinWidths", 
                "Variable Bin Histogram 1D",
                binEdges);
                        
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            histogram.fill(rand.nextDouble() * 100);
        }

        IPlotter plotter = analysisFactory.createPlotterFactory().create();
        plotter.createRegion();
        plotter.region(0).plot(histogram);
        
        plotter.style().statisticsBoxStyle().setVisible(true);
        plotter.show();
                   
        Object object = new Boolean(true);
        synchronized(object) {
            object.wait(5000);
        }
    }
}