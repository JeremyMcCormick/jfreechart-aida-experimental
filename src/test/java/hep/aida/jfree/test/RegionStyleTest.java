package hep.aida.jfree.test;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.AnalysisFactory;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

public class RegionStyleTest extends TestCase {

    static {
        AnalysisFactory.register();
        AnalysisFactory.configure();
    }

    public void testNotVisible() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        plotter.region(1).plot(histogram);
        IPlotterStyle style = IAnalysisFactory.create().createPlotterFactory().createPlotterStyle();
        style.regionBoxStyle().borderStyle().setVisible(false);
        style.regionBoxStyle().backgroundStyle().setVisible(false);
        plotter.region(0).applyStyle(style);
        plotter.region(1).applyStyle(style);
        writeToFile(plotter, new PlotFile(this.getClass(), "NotVisible"));
    }

    public void testApplyStyle() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        plotter.region(1).plot(histogram);
        IPlotterStyle style = IAnalysisFactory.create().createPlotterFactory().createPlotterStyle();
        style.regionBoxStyle().borderStyle().setColor("green");
        style.regionBoxStyle().borderStyle().setThickness(10);
        style.regionBoxStyle().backgroundStyle().setColor("black");
        plotter.region(0).applyStyle(style);
        plotter.region(1).applyStyle(style);
        writeToFile(plotter, new PlotFile(this.getClass(), "ApplyStyle"));
    }

    public void testDefaultStyle() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        plotter.region(1).plot(histogram);
        IPlotterStyle style = IAnalysisFactory.create().createPlotterFactory().createPlotterStyle();
        plotter.region(0).applyStyle(style);
        plotter.region(1).applyStyle(style);
        writeToFile(plotter, new PlotFile(this.getClass(), "DefaultStyle"));
    }

    public void testNoBorder() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        plotter.region(1).plot(histogram);
        IPlotterStyle style = IAnalysisFactory.create().createPlotterFactory().createPlotterStyle();
        style.regionBoxStyle().borderStyle().setVisible(false);
        plotter.region(0).applyStyle(style);
        plotter.region(1).applyStyle(style);
        writeToFile(plotter, new PlotFile(this.getClass(), "NoBorder"));
    }

    private IHistogramFactory createHistogramFactory() {
        IAnalysisFactory analysisFactory = IAnalysisFactory.create();
        IHistogramFactory histogramFactory = analysisFactory.createHistogramFactory(null);
        return histogramFactory;
    }

    private IPlotter createPlotter() {
        return IAnalysisFactory.create().createPlotterFactory().create();
    }

    private IHistogram1D createHistogram() {
        IHistogram1D histogram = createHistogramFactory().createHistogram1D("h1d", 50, 0, 50.0);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            histogram.fill(rand.nextInt(50));
        }
        return histogram;
    }

    private void writeToFile(IPlotter plotter, File outputFile) {
        outputFile.getParentFile().mkdirs();
        try {
            plotter.writeToFile(outputFile.getPath());
            //System.out.println(this.getClass().getSimpleName() + " - saved plots to " + outputFile.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("serial")
    static class PlotFile extends File {
        PlotFile(Class<?> klass, String extra) {
            super("./target/test-output/" + klass.getSimpleName() + "_" + extra + ".png");
        }
    }
}
