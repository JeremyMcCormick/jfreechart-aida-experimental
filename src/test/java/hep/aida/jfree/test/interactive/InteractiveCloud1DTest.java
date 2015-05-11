package hep.aida.jfree.test.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hep.aida.IAxisStyle;
import hep.aida.ICloud1D;
import hep.aida.IPlotterStyle;
import hep.aida.ITextStyle;
import hep.aida.jfree.test.Cloud1DTest;

public class InteractiveCloud1DTest extends Cloud1DTest {

    public void test() {

        setBatchMode(false);

        // ICloud1D c1d = histogramFactory.createCloud1D("c1d");
        ICloud1D cloud = histogramFactory.createCloud1D("cloud", "cloud", 100, "autoconvert=true");

        // Set labels for axes automatically based on title
        cloud.annotation().addItem("xAxisLabel", cloud.title() + " X");
        cloud.annotation().addItem("yAxisLabel", cloud.title() + " Y");

        // Create region for showing plots
        plotter.createRegion();

        IPlotterStyle pstyle = plotter.style();

        // data fill color
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().outlineStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(false);

        pstyle.gridStyle().setVisible(false);

        // title style
        ITextStyle titleStyle = pstyle.titleStyle().textStyle();
        titleStyle.setBold(true);
        titleStyle.setFontSize(30.);
        titleStyle.setFont("Arial");
        titleStyle.setColor("black");

        // axis style
        List<IAxisStyle> axes = new ArrayList<IAxisStyle>();
        axes.add(pstyle.xAxisStyle());
        axes.add(pstyle.yAxisStyle());
        for (IAxisStyle axisStyle : axes) {
            axisStyle.labelStyle().setBold(true);
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

        // Plot histograms into region.
        plotter.region(0).plot(cloud);
        plotter.show();

        Random rand = new Random();
        for (int i = 0; i < 100000; i++) {
            double value = rand.nextDouble() * 100.;
            System.out.println("fill - " + value);
            cloud.fill(value);
            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}