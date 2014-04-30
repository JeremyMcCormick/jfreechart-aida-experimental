package hep.aida.jfree.test;

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
public class LogScaleTest extends AbstractPlotTest {

    public void test() {

        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 50, 0.0, 5.0);
        Random rand = new Random();
        for (int i = 0; i < 1000000; i++) {
            h1d.fill(Math.abs(rand.nextGaussian()));
        }

        h1d.annotation().addItem("xAxisLabel", h1d.title() + " X");
        h1d.annotation().addItem("yAxisLabel", h1d.title() + " Y");

        // Create 3x3 regions for showing plots
        // plotter.createRegions(3, 3, 0);
        plotter.createRegion();

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

        // Set log scale.
        // pstyle.xAxisStyle().setScaling("log");
        pstyle.yAxisStyle().setScaling("log");

        plotter.region(0).plot(h1d, pstyle);
        
        mode();
    }
}
