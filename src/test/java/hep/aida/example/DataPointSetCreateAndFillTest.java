package hep.aida.example;

import hep.aida.IAnalysisFactory;
import hep.aida.IDataPointSet;
import hep.aida.IDataPointSetFactory;
import hep.aida.IPlotter;
import hep.aida.ITree;
import junit.framework.TestCase;

public class DataPointSetCreateAndFillTest extends TestCase {

    public void test() throws Exception {
        IAnalysisFactory af = IAnalysisFactory.create();
        ITree tree = af.createTreeFactory().create();
        IDataPointSetFactory dpsf = af.createDataPointSetFactory(tree);

        // Create a one dimensional IDataPointSet.
        IDataPointSet dps1D = dpsf.create("dps1D", "one dimensional IDataPointSet", 1);

        // Fill the one dimensional IDataPointSet
        double[] yVals1D = { 0.32, 0.45, 0.36, 0.29, 0.34 };
        double[] yErrP1D = { 0.06, 0.07, 0.03, 0.07, 0.04 };

        for (int i = 0; i < yVals1D.length; i++) {
            dps1D.addPoint();
            dps1D.point(i).coordinate(0).setValue(yVals1D[i]);
            dps1D.point(i).coordinate(0).setErrorPlus(yErrP1D[i]);
        }

        // Create a two dimensional IDataPointSet.
        IDataPointSet dps2D = dpsf.create("dps2D", "two dimensional IDataPointSet", 2);

        // Fill the two dimensional IDataPointSet
        double[] yVals2D = { 0.12, 0.22, 0.35, 0.42, 0.54, 0.61 };
        double[] yErrP2D = { 0.01, 0.02, 0.03, 0.03, 0.04, 0.04 };
        double[] yErrM2D = { 0.02, 0.02, 0.02, 0.04, 0.06, 0.05 };
        double[] xVals2D = { 1.5, 2.6, 3.4, 4.6, 5.5, 6.4 };
        double[] xErrP2D = { 0.5, 0.5, 0.4, 0.4, 0.5, 0.5 };

        for (int i = 0; i < yVals2D.length; i++) {
            dps2D.addPoint();
            dps2D.point(i).coordinate(0).setValue(xVals2D[i]);
            dps2D.point(i).coordinate(0).setErrorPlus(xErrP2D[i]);
            dps2D.point(i).coordinate(1).setValue(yVals2D[i]);
            dps2D.point(i).coordinate(1).setErrorPlus(yErrP2D[i]);
            dps2D.point(i).coordinate(1).setErrorMinus(yErrM2D[i]);
        }

        // Display the results
        IPlotter plotter = af.createPlotterFactory().create("Plot IDataPointSets");
        plotter.createRegions(2, 1);
        plotter.region(0).plot(dps1D);
        plotter.region(1).plot(dps2D);
        plotter.writeToFile("target/" + this.getClass().getSimpleName() + ".png");
        //plotter.show();

        //Thread.sleep(10000);
    }
}