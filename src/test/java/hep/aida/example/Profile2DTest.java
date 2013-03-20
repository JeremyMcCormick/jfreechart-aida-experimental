package hep.aida.example;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterStyle;
import hep.aida.IProfile2D;
import hep.aida.ITree;

import java.util.Random;

import junit.framework.TestCase;

public class Profile2DTest extends TestCase {
    
    public void test() throws Exception {
	IAnalysisFactory af = IAnalysisFactory.create();
	ITree tree = af.createTreeFactory().create();
	IHistogramFactory hf = af.createHistogramFactory(tree);

	// Create 1D and 2D IProfile with fixed bin width
	IProfile2D prof2DFixedBinWidth = hf.createProfile2D(
		"prof2DFixedBinWidth", "Fixed bin width 2D", 10, 0, 1, 10, -5,
		5);

	double[] xBinEdges = { 0, 0.1, 0.21, 0.35, 0.48, 0.52, 0.65, 0.75,
		0.83, 0.94, 1.0 };
	double[] yBinEdges = { -5.0, -4.1, -3.2, -2.0, -1.1, -0.4, 1.2, 2.3,
		3.5, 4.2, 5.0 };

	// Create 1D and 2D IProfile with variable bin width
	IProfile2D prof2DVariableBinWidth = hf.createProfile2D(
		"prof2DVariableBinWidth", "Variable bin width 2D", xBinEdges,
		yBinEdges);

	Random r = new Random();

	for (int i = 0; i < 100; i++) {
	    double x = r.nextDouble();
	    double y = x + 0.1 * r.nextGaussian();

	    // Fill the IProfiles with default weight.
	    prof2DFixedBinWidth.fill(x, y, r.nextDouble());

	    prof2DVariableBinWidth.fill(x, y, r.nextDouble());
	}

	// Display the results
	IPlotter plotter = af.createPlotterFactory().create(
		"Fit and Plot an IProfile");
	IPlotterStyle pstyle = plotter.style();
	pstyle.gridStyle().setVisible(true);
	pstyle.gridStyle().setThickness(1);
	plotter.createRegions(2, 1);
	plotter.region(0).plot(prof2DFixedBinWidth, pstyle);
	plotter.region(1).plot(prof2DVariableBinWidth, pstyle);
	plotter.show();

	plotter.writeToFile(this.getClass().getSimpleName() + ".png");
    }

    //public static void main(String[] argv) {
    //new Profile2DTest().test();
    //}
}