package hep.aida.jfree.test;

import hep.aida.IAnalysisFactory;
import hep.aida.IFitFactory;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.ITree;
import hep.aida.ITreeFactory;
import hep.aida.jfree.AnalysisFactory;

import java.io.IOException;

import junit.framework.TestCase;

public abstract class AbstractPlotTest extends TestCase {
    
    protected IAnalysisFactory analysisFactory;
    protected IPlotterFactory plotterFactory;
    protected IHistogramFactory histogramFactory;
    protected ITreeFactory treeFactory;
    protected IFitFactory fitFactory;
    protected ITree tree;
    protected IPlotter plotter;
    protected IPlotterStyle style;
    protected String outputFormat = "png";
    protected boolean batchMode = true;
    
    protected void setUp() {
        AnalysisFactory.register();
        analysisFactory = IAnalysisFactory.create();
        plotterFactory = analysisFactory.createPlotterFactory();
        histogramFactory = analysisFactory.createHistogramFactory(null);
        treeFactory = analysisFactory.createTreeFactory();
        tree = treeFactory.createTree(this.getClass().getSimpleName());
        plotter = plotterFactory.create();
        fitFactory = analysisFactory.createFitFactory();
        //((Plotter)plotter).setEmbedded(false);
        style = plotter.style();
    }
    
    protected void mode() {        
        if (batchMode) {
            batch();
        } else {
            interactive();
        }
    }
    
    protected void batch() {
        try {
            plotter.writeToFile("./target/" + this.getClass().getSimpleName() + "." + outputFormat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void interactive() {
        plotter.show();
    }
    
    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }
    
    public void test() {
        plot();
        mode();
    }
    
    public void tearDown() {
        if (!batchMode) {
            System.out.println("Test is running in interactive mode.");
            System.out.println("Hit Ctrl + C to exit.");
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }    
    
    protected abstract void plot();
}