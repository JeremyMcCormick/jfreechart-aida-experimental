package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class OverlayTest extends AbstractPlotTest {

    // Create a 1D histogram with random Gaussian distribution
    private final IHistogram1D histogram1D() {
        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 50, -5.0, 5.0);
        return h1d;
    }

    private void overlay() {

        // Create a list with various types of histograms
        IHistogram1D h1d = histogram1D();

        h1d.annotation().addItem("xAxisLabel", h1d.title() + " X");
        h1d.annotation().addItem("yAxisLabel", h1d.title() + " Y");

        // Create 3x3 regions for showing plots
        // plotter.createRegions(3, 3, 0);
        plotter.createRegion();

        IPlotterStyle pstyle = plotter.style();

        /*
         * // data fill color
         * //pstyle.dataStyle().fillStyle().setColor("white");
         * pstyle.dataStyle().fillStyle().setVisible(false);
         * 
         * pstyle.dataStyle().outlineStyle().setVisible(true);
         * pstyle.dataStyle().outlineStyle().setColor("blue");
         * //pstyle.dataStyle().outlineStyle().setVisible(false);
         * 
         * pstyle.dataStyle().lineStyle().setVisible(false);
         * pstyle.dataStyle().fillStyle().setVisible(false);
         * 
         * // title style ITextStyle titleStyle =
         * pstyle.titleStyle().textStyle(); titleStyle.setBold(true);
         * //titleStyle.setItalic(true); titleStyle.setFontSize(30.);
         * titleStyle.setFont("Arial"); titleStyle.setColor("black");
         * 
         * // axis style List<IAxisStyle> axes = new ArrayList<IAxisStyle>();
         * axes.add(pstyle.xAxisStyle()); axes.add(pstyle.yAxisStyle()); for
         * (IAxisStyle axisStyle : axes) { axisStyle.labelStyle().setBold(true);
         * //axisStyle.labelStyle().setItalic(true);
         * axisStyle.labelStyle().setFont("Helvetica");
         * axisStyle.labelStyle().setFontSize(15);
         * axisStyle.labelStyle().setColor("black");
         * axisStyle.lineStyle().setColor("black");
         * axisStyle.lineStyle().setThickness(2);
         * axisStyle.tickLabelStyle().setColor("black");
         * axisStyle.tickLabelStyle().setFont("Helvetica");
         * axisStyle.tickLabelStyle().setBold(true);
         * axisStyle.tickLabelStyle().setFontSize(10); }
         * 
         * // background color
         * pstyle.regionBoxStyle().backgroundStyle().setColor("white");
         * 
         * // Error bars and other styles.
         * pstyle.dataStyle().errorBarStyle().setVisible(true);
         * pstyle.dataStyle().errorBarStyle().setColor("blue");
         * pstyle.dataStyle().errorBarStyle().setLineType("1");
         * pstyle.dataStyle().errorBarStyle().setThickness(1);
         * pstyle.dataStyle().errorBarStyle().setParameter("errorBarDecoration",
         * "0.0"); pstyle.dataStyle().lineStyle().setLineType("1");
         * pstyle.dataStyle().lineStyle().setColor("blue");
         */

        // pstyle.dataStyle().errorBarStyle().setVisible(false);
        pstyle.dataStyle().errorBarStyle().setVisible(true);
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);

        // Plot first histogram.
        pstyle.dataStyle().lineStyle().setColor("blue");
        plotter.region(0).plot(h1d, pstyle);

        // Overlay histogram with style settings.
        IHistogram1D overlayHist = histogram1D();
        /*
         * pstyle.dataStyle().outlineStyle().setColor("red");
         * pstyle.dataStyle().errorBarStyle().setColor("red");
         * pstyle.dataStyle().errorBarStyle().setLineType("2");
         * pstyle.dataStyle().errorBarStyle().setThickness(2);
         * pstyle.dataStyle().lineStyle().setLineType("2");
         * pstyle.dataStyle().lineStyle().setColor("red");
         */
        pstyle.dataStyle().lineStyle().setColor("red");
        plotter.region(0).plot(overlayHist, pstyle);

        // Another Overlay histogram with style settings.
        /*
         * IHistogram1D overlayHist2 = histogram1D();
         * pstyle.dataStyle().outlineStyle().setColor("green");
         * pstyle.dataStyle().errorBarStyle().setColor("green");
         * pstyle.dataStyle().errorBarStyle().setLineType("3");
         * pstyle.dataStyle().errorBarStyle().setThickness(3);
         * pstyle.dataStyle().lineStyle().setLineType("3");
         * pstyle.dataStyle().lineStyle().setColor("green");
         * plotter.region(0).plot(overlayHist2, pstyle);
         */

        // Show time
        //plotter.show();

        // Overlay histograms in real time.
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            h1d.fill(rand.nextGaussian());
            overlayHist.fill(rand.nextGaussian());
            // overlayHist2.fill(rand.nextGaussian());
            //Thread.sleep(200);
        }
    }
    
    public void testOverlay() {
        overlay();
        mode();
    }
}
