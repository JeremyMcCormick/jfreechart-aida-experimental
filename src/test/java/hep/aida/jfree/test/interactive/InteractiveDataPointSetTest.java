package hep.aida.jfree.test.interactive;

import hep.aida.IDataPointSet;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * <p>
 * Based on this AIDA example:
 * <p>
 * <a href="http://aida.freehep.org/doc/v3.3.0/UsersGuide/dataPointSets.jsp#DataPointSetCreateAndFill">
 * DataPointSetCreateAndFill</a>
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveDataPointSetTest extends AbstractPlotTest {

    public void testDataPointSet() {

        this.setBatchMode(false);
        this.setWaitTime(100000);

        // Create a one dimensional IDataPointSet.
        IDataPointSet dps1D = analysisFactory.createDataPointSetFactory(null).create("dps1D",
                "one dimensional IDataPointSet", 1);

        plotter.createRegion();
        IPlotterStyle style = plotterFactory.createPlotterStyle();
        style.gridStyle().setVisible(false);
        style.dataStyle().markerStyle().setVisible(true);
        style.dataStyle().markerStyle().setColor("blue");
        style.dataStyle().markerStyle().setShape("diamond");
        style.dataStyle().markerStyle().setSize(5);
        style.dataStyle().outlineStyle().setVisible(true);
        style.dataStyle().outlineStyle().setColor("red");
        style.dataStyle().outlineStyle().setLineType("dashed");
        style.dataStyle().outlineStyle().setThickness(2);
        style.dataStyle().errorBarStyle().setVisible(true);
        plotter.region(0).plot(dps1D, style);
        plotter.show();

        Random random = new Random();
        int nextPointIndex = 0;
        for (int i = 0; i < 1000; i++) {
            dps1D.addPoint();
            dps1D.point(nextPointIndex).coordinate(0).setValue(random.nextDouble() * 1000);
            dps1D.point(nextPointIndex).coordinate(0).setErrorPlus(random.nextDouble() * 100);
            dps1D.point(nextPointIndex).coordinate(0).setErrorMinus(random.nextDouble() * 100);
            ++nextPointIndex;
            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        mode();
    }
}
