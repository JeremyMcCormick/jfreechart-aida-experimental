package hep.aida.jfree;

import hep.aida.IAxisStyle;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.ITextStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DTest extends AbstractPlotTest {

    // Create a 1D histogram with random Gaussian distribution
    private final IHistogram1D histogram1D() {
        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 50, -5.0, 5.0);
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            h1d.fill(rand.nextGaussian());
        }
        return h1d;
    }

    public void histogramExample() {

        // Create a list with various types of histograms
        IHistogram1D h1d = histogram1D();

        h1d.annotation().addItem("xAxisLabel", h1d.title() + " X");
        h1d.annotation().addItem("yAxisLabel", h1d.title() + " Y");

        // Create 3x3 regions for showing plots
        // plotter.createRegions(2, 2, 0);
        // plotter.createRegion();

        IPlotterStyle pstyle = plotter.style();

        // data fill color
        // pstyle.dataStyle().fillStyle().setColor("white");
        pstyle.dataStyle().fillStyle().setVisible(false);

        pstyle.dataStyle().outlineStyle().setVisible(true);
        pstyle.dataStyle().outlineStyle().setColor("blue");
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

        pstyle.gridStyle().setVisible(true);

        plotter.createRegions(1, 2, 0);

        plotter.region(0).plot(h1d, pstyle);

        plotter.region(1).plot(h1d, pstyle);
    }

    public void testBatch() {
        histogramExample();
        mode();
    }
}