package hep.aida.jfree;

import hep.aida.IAxisStyle;
import hep.aida.IBaseHistogram;
import hep.aida.ICloud1D;
import hep.aida.ICloud2D;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.ITextStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is basically an integration test. It converts AIDA objects to JFreeChart
 * representations and plots them.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class AIDAToJFreeTest extends AbstractPlotTest {

    // Create a 1D cloud with random data
    private final ICloud1D cloud1D() {
        Random rand = new Random();
        ICloud1D c1d = histogramFactory.createCloud1D("c1d");
        for (int i = 0; i < 1000; i++) {
            c1d.fill(rand.nextDouble() * 100.);
        }
        return c1d;
    }

    // Create a 1D histogram with random Gaussian distribution
    private final IHistogram1D histogram1D() {
        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 50, -5.0, 5.0);
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            h1d.fill(rand.nextGaussian());
        }
        return h1d;
    }

    // Create a 2D cloud with randomly distributed points
    private final ICloud2D cloud2D() {
        ICloud2D c2d = histogramFactory.createCloud2D("c2d");
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            c2d.fill(Math.abs(rand.nextDouble()) * 100, Math.abs(rand.nextDouble()) * 100);
        }
        return c2d;
    }

    private final IHistogram2D histogram2D() {
        IHistogram2D h2d = histogramFactory.createHistogram2D("h2d", 100, 0.0, 100.0, 100, 0.0, 100.0);
        Random rand = new Random();
        for (int i = 0; i < 100000; i++) {
            h2d.fill(Math.abs(rand.nextDouble()) * 100, Math.abs(rand.nextDouble()) * 100);
        }
        return h2d;
    }

    private void examples() {

        // Create a list with various types of histograms
        List<IBaseHistogram> histos = new ArrayList<IBaseHistogram>();
        histos.add(histogram1D());
        histos.add(histogram2D());
        histos.add(cloud1D());
        histos.add(cloud2D());
        histos.add(histogram1D());

        // Set labels for axes automatically based on title
        for (IBaseHistogram histo : histos) {
            histo.annotation().addItem("xAxisLabel", histo.title() + " X");
            histo.annotation().addItem("yAxisLabel", histo.title() + " Y");
        }

        // Create 3x3 regions for showing plots
        plotter.createRegions(3, 3, 0);

        IPlotterStyle pstyle = plotter.style();

        // data fill color
        // pstyle.dataStyle().fillStyle().setColor("white");
        pstyle.dataStyle().fillStyle().setVisible(false);

        pstyle.dataStyle().outlineStyle().setVisible(true);
        // pstyle.dataStyle().outlineStyle().setColor("black");
        // pstyle.dataStyle().outlineStyle().setVisible(false);

        pstyle.dataStyle().lineStyle().setVisible(false);

        // title style
        ITextStyle titleStyle = pstyle.titleStyle().textStyle();
        titleStyle.setBold(true);
        // titleStyle.setItalic(true);
        titleStyle.setFontSize(30.);
        titleStyle.setFont("Arial");
        titleStyle.setColor("black");

        // axis style
        List<IAxisStyle> axes = new ArrayList<IAxisStyle>();
        axes.add(pstyle.xAxisStyle());
        axes.add(pstyle.yAxisStyle());
        for (IAxisStyle axisStyle : axes) {
            axisStyle.labelStyle().setBold(true);
            // axisStyle.labelStyle().setItalic(true);
            axisStyle.labelStyle().setFont("Helvetica");
            axisStyle.labelStyle().setFontSize(15);
            axisStyle.labelStyle().setColor("black");
            axisStyle.lineStyle().setColor("black");
            axisStyle.lineStyle().setThickness(2);
            axisStyle.tickLabelStyle().setColor("black");
            axisStyle.tickLabelStyle().setFont("Helvetica");
            axisStyle.tickLabelStyle().setBold(true);
            axisStyle.tickLabelStyle().setFontSize(10);
        }

        // background color
        pstyle.regionBoxStyle().backgroundStyle().setColor("white");

        // Plot histograms into regions
        for (int i = 0; i < histos.size(); i++) {
            plotter.region(i).plot(histos.get(i), pstyle);
        }
    }
    
    public void test() {
        examples();
        mode();
    }
}
