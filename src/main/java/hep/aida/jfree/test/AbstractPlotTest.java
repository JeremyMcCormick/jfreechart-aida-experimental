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

import java.io.File;
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
    protected int waitTime = 5000;
    
    protected void setUp() {
        AnalysisFactory.register();
        analysisFactory = IAnalysisFactory.create();
        plotterFactory = analysisFactory.createPlotterFactory();
        histogramFactory = analysisFactory.createHistogramFactory(null);
        treeFactory = analysisFactory.createTreeFactory();
        tree = treeFactory.createTree(this.getClass().getSimpleName());
        plotter = plotterFactory.create();
        fitFactory = analysisFactory.createFitFactory();
        style = plotter.style();
    }
    
    protected void mode() {        
        if (batchMode) {
            writeToFile();
        } else {
            show();
            pause();
        }
    }
    
    protected void writeToFile() {
        File outputFile = new File("./target/test-output/" + this.getClass().getSimpleName() + "." + outputFormat);
        outputFile.getParentFile().mkdirs();
        try {
            plotter.writeToFile(outputFile.getPath());
            System.out.println(this.getClass().getSimpleName() + " - saved plots to " + outputFile.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void show() {
        plotter.show();
    }
    
    protected void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
    
    protected void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }
    
    protected void pause() {
        Boolean object = new Boolean(true);
        synchronized(object) {
            try {
                object.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }    
    
    protected void pause(int waitTime) {
        Boolean object = new Boolean(true);
        synchronized(object) {
            try {
                object.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }    
}