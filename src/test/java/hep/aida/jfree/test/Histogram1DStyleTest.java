package hep.aida.jfree.test;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DStyleTest extends AbstractPlotTest {

    public void test() {

        IHistogram1D h = histogramFactory.createHistogram1D("h1d", 50, 0, 50.0);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            h.fill(rand.nextInt(50));
        }
        
        // Set labels for axes automatically based on title
        h.annotation().addItem("xAxisLabel", h.title() + " X");
        h.annotation().addItem("yAxisLabel", h.title() + " Y");

        // Create 3x3 regions for showing plots
        plotter.createRegions(3, 3, 0);

        // Get the plotter style.
        IPlotterStyle pstyle = plotter.style();

        // Axis appearence.
        pstyle.xAxisStyle().labelStyle().setBold(true);
        pstyle.yAxisStyle().labelStyle().setBold(true);
        pstyle.xAxisStyle().tickLabelStyle().setBold(true);
        pstyle.yAxisStyle().tickLabelStyle().setBold(true);
        pstyle.xAxisStyle().lineStyle().setColor("black");
        pstyle.yAxisStyle().lineStyle().setColor("black");
        pstyle.xAxisStyle().lineStyle().setThickness(2);
        pstyle.yAxisStyle().lineStyle().setThickness(2);

        // Force auto range to zero.
        pstyle.yAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.xAxisStyle().setParameter("allowZeroSuppression", "false");

        // Title style.
        pstyle.titleStyle().textStyle().setFontSize(20);

        // Draw caps on error bars.
        pstyle.dataStyle().errorBarStyle().setParameter("errorBarDecoration", (new Float(1.0f)).toString());

        // Turn off grid lines until explicitly enabled.
        pstyle.gridStyle().setVisible(false);

        // 0) Filled with bars.
        plotter.region(0).plot(h, pstyle);
        plotter.region(0).setTitle("0) filled and bars");

        // 1) Filled plus outline.
        pstyle.dataStyle().lineStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setColor("purple");
        plotter.region(1).plot(h, pstyle);
        plotter.region(1).setTitle("1) filled with no bars");

        // 2) No fill with bars drawn.
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().lineStyle().setColor("green");
        pstyle.dataStyle().fillStyle().setVisible(false);
        plotter.region(2).plot(h, pstyle);
        plotter.region(2).setTitle("2) bars with no fill");

        // 3) No fill with outline only.
        pstyle.dataStyle().lineStyle().setColor("blue");
        pstyle.dataStyle().lineStyle().setVisible(false);
        plotter.region(3).plot(h, pstyle);
        plotter.region(3).setTitle("3) contour of histogram only");

        // 4) Show errors only.
        pstyle.dataStyle().setVisible(false);
        plotter.region(4).plot(h, pstyle);
        plotter.region(4).setTitle("4) errors only");

        // 5) Show data only in outline style.
        pstyle.dataStyle().setVisible(true);
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        plotter.region(5).plot(h, pstyle);
        plotter.region(5).setTitle("5) data only");

        // 6) Show grid.
        pstyle.setVisible(false);
        pstyle.gridStyle().setVisible(true);
        pstyle.gridStyle().setLineType("dashed");
        pstyle.gridStyle().setColor("red");
        plotter.region(6).plot(h, pstyle);
        plotter.region(6).setTitle("6) grid");

        // 7) Show data marker.
        pstyle.gridStyle().setVisible(false);
        pstyle.setVisible(true);
        pstyle.dataStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setShape("dot");
        pstyle.dataStyle().markerStyle().setSize(5);
        pstyle.dataStyle().errorBarStyle().setVisible(true);
        plotter.region(7).plot(h, pstyle);
        plotter.region(7).setTitle("7) data marker and errors");

        // 8) Show lines between points.
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(false);
        pstyle.dataStyle().outlineStyle().setVisible(true);
        pstyle.dataStyle().outlineStyle().setColor("blue");
        pstyle.dataStyle().outlineStyle().setLineType("dotted");
        pstyle.dataStyle().outlineStyle().setThickness(5);

        // pstyle.xAxisStyle().setParameter(Style.AXIS_LOWER_LIMIT, "15.0");
        // pstyle.xAxisStyle().setParameter(Style.AXIS_UPPER_LIMIT, "40.0");

        // pstyle.yAxisStyle().setParameter(Style.AXIS_LOWER_LIMIT, "100.0");
        // pstyle.yAxisStyle().setParameter(Style.AXIS_UPPER_LIMIT, "300.0");

        plotter.region(8).plot(h, pstyle);
        plotter.region(8).setTitle("8) lines between points");
        
        mode();
    }
}
