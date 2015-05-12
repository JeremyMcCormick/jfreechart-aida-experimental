package hep.aida.jfree.test;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.AnalysisFactory;
import hep.aida.jfree.plotter.PlotterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

/**
 * Tests to make sure that various combinations of style settings are interpretted correctly. The style objects can be
 * set on the plotter, individual regions, and when calling the <code>plot</code> methods on the region. The styles
 * should cascade from plot to region to plotter. Default styles should not override those that have been set explicitly
 * by user code.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class PlotterStyleTest extends TestCase {

    static {
        AnalysisFactory.register();
        AnalysisFactory.configure();
    }

    /**
     * Use plotter's style without alteration.
     */
    public void testPlotterStyle() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.style().xAxisStyle().setLabel("Dummy Label");
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        plotter.region(1).plot(histogram);
        assertEquals("Label is not right for region 0.", "Dummy Label", plotter.region(0).style().xAxisStyle().label());
        assertEquals("Label is not right for region 1.", "Dummy Label", plotter.region(1).style().xAxisStyle().label());
        writeToFile(plotter, new PlotFile(this.getClass(), "testPlotterStyle"));
    }

    /**
     * Set a style on the plotter.
     */
    public void testDefaultPlotterStyle() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.setStyle(IAnalysisFactory.create().createPlotterFactory().createPlotterStyle());
        plotter.style().xAxisStyle().setLabel("Dummy Label");
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        plotter.region(1).plot(histogram);
        assertEquals("Label is not right for region 0.", "Dummy Label", plotter.region(0).style().xAxisStyle().label());
        assertEquals("Label is not right for region 1.", "Dummy Label", plotter.region(1).style().xAxisStyle().label());
        writeToFile(plotter, new PlotFile(this.getClass(), "testSetPlotterStyle"));
    }

    /**
     * Set a style on a region.
     */
    public void testRegionStyle() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.style().xAxisStyle().setLabel("Dummy Label");
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        plotter.region(1).style().xAxisStyle().setLabel("Dummy Region Label");
        plotter.region(1).plot(histogram);
        assertEquals("Label is not right for region 0.", "Dummy Label", plotter.region(0).style().xAxisStyle().label());
        assertEquals("Label is not right for region 1.", "Dummy Region Label", plotter.region(1).style().xAxisStyle()
                .label());
        writeToFile(plotter, new PlotFile(this.getClass(), "testRegionStyle"));
    }

    public void testHistogramStyle() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.style().xAxisStyle().setLabel("Dummy Label");
        plotter.createRegions(2);
        plotter.region(0).plot(histogram);
        IPlotterStyle style = ((PlotterFactory) IAnalysisFactory.create().createPlotterFactory())
                .createDefaultHistogram1DStyle();
        style.xAxisStyle().setLabel("Dummy Histogram Label");
        plotter.region(1).plot(histogram, style);
        // ObjectStyle objectStyle = ((PlotterRegion)plotter.region(1)).getObjectStyles(histogram).get(0);
        assertEquals("Label is not right for region 0.", "Dummy Label", plotter.region(0).style().xAxisStyle().label());
        // assertEquals("Label is not right for region 1.", "Dummy Histogram Label",
        // objectStyle.style().xAxisStyle().label());
        writeToFile(plotter, new PlotFile(this.getClass(), "testHistogramStyle"));
    }

    public void testRegionApplyStyle() {
        IHistogram1D histogram = createHistogram();
        IPlotter plotter = createPlotter();
        plotter.createRegion();
        plotter.region(0).plot(histogram);
        IPlotterStyle style = ((PlotterFactory) IAnalysisFactory.create().createPlotterFactory())
                .createDefaultHistogram1DStyle();
        style.regionBoxStyle().borderStyle().setColor("green");
        style.regionBoxStyle().borderStyle().setThickness(10);
        style.regionBoxStyle().backgroundStyle().setColor("black");
        plotter.region(0).applyStyle(style);
        writeToFile(plotter, new PlotFile(this.getClass(), "ApplyStyle"));
    }

    /* ----------------------------------------------------------------------------------------- */

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
            System.out.println(this.getClass().getSimpleName() + " - saved plots to " + outputFile.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class PlotFile extends File {
        PlotFile(Class<?> klass, String extra) {
            super("./target/test-output/" + klass.getSimpleName() + "_" + extra + ".png");
        }
    }
}
