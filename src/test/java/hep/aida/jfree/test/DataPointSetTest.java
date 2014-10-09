package hep.aida.jfree.test;

import hep.aida.IDataPointSet;
import hep.aida.IPlotterStyle;

import java.util.Random;

/**
 * <p>
 * Based on this AIDA example:
 * <p>
 * <a href="http://aida.freehep.org/doc/v3.3.0/UsersGuide/dataPointSets.jsp#DataPointSetCreateAndFill">DataPointSetCreateAndFill</a>
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 *
 */
public class DataPointSetTest extends AbstractPlotTest {
    
    public void testDataPointSet() {
        
        this.setBatchMode(false);
        this.setWaitTime(100000);
        
        // Create a one dimensional IDataPointSet.
        IDataPointSet dps1D = 
                analysisFactory.createDataPointSetFactory(null).create("dps1D", "one dimensional IDataPointSet", 1);
        
        // Fill the one dimensional IDataPointSet
        double[] yVals1D = { 0.32, 0.45, 0.36, 0.29, 0.34 };
        double[] yErrP1D = { 0.06, 0.07, 0.03, 0.07, 0.04 };

        for (int i = 0; i < yVals1D.length; i++) {
            dps1D.addPoint();
            dps1D.point(i).coordinate(0).setValue(yVals1D[i]);
            dps1D.point(i).coordinate(0).setErrorPlus(yErrP1D[i]);
            dps1D.point(i).coordinate(0).setErrorMinus(yErrP1D[i]);
        }
        
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
        plotter.region(0).plot(dps1D, style);
        plotter.show();
        
        Random random = new Random();
        int nextPointIndex = yVals1D.length;
        for (int i = 0; i < 1000; i++) {
            dps1D.addPoint();
            dps1D.point(nextPointIndex).coordinate(0).setValue(random.nextDouble());
            dps1D.point(nextPointIndex).coordinate(0).setErrorPlus(random.nextDouble() / 10.);
            dps1D.point(nextPointIndex).coordinate(0).setErrorMinus(random.nextDouble() / 10.);
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
