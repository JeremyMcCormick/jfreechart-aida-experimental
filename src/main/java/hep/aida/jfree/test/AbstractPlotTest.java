package hep.aida.jfree.test;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.AnalysisFactory;

import java.io.IOException;

import junit.framework.TestCase;

public abstract class AbstractPlotTest extends TestCase {
    
    protected IAnalysisFactory analysisFactory;
    protected IPlotterFactory plotterFactory;
    protected IHistogramFactory histogramFactory;
    protected IPlotter plotter;
    protected IPlotterStyle style;
    protected String outputFormat = "png";
    protected boolean batchMode = true;
    
    protected void setUp() {
        AnalysisFactory.register();
        analysisFactory = IAnalysisFactory.create();
        plotterFactory = analysisFactory.createPlotterFactory();
        histogramFactory = analysisFactory.createHistogramFactory(null);
        plotter = plotterFactory.create();
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
}