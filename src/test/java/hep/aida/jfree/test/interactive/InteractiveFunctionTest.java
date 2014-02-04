package hep.aida.jfree.test.interactive;

import hep.aida.IFitFactory;
import hep.aida.IFitResult;
import hep.aida.IFitter;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.IProfile1D;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

public class InteractiveFunctionTest extends AbstractPlotTest {

    public void plot() {

        // Create 1D and 2D IProfile with fixed bin width
        IProfile1D profile = histogramFactory.createProfile1D("prof1DFixedBinWidth", "Fixed bin width 1D", 10, 0, 1);

        Random r = new Random();

        for (int i = 0; i < 100; i++) {
            double x = r.nextDouble();
            double y = x + 0.1 * r.nextGaussian();

            // Fill the IProfiles with default weight.
            profile.fill(x, y);
        }

        // Create the Fitter and fit the profiles
        IFitFactory fitFactory = analysisFactory.createFitFactory();
        IFitter fitter = fitFactory.createFitter("Chi2", "jminuit");

        // Perform the fits
        IFitResult fitResult = fitter.fit(profile, "p1");
        //IFunction fitResTest = fitResult.fittedFunction();
        //for (int i = 0, n = profile.entries(); i < n; i++) {
        //    double v = fitResTest.value(new double[] { profile.axis().binCenter(i) });
        //    System.out.println("bin[" + i + "] = " + v);
        //}

        // Display the results        
        plotter.createRegions(2, 1);
        
        style.gridStyle().setVisible(false);
        
        // Plot profile.
        plotter.region(0).plot(profile, style);
        
        // Set function style.
        IPlotterStyle functionStyle = plotterFactory.createPlotterStyle();
        functionStyle.dataStyle().lineStyle().setColor("purple");
        functionStyle.dataStyle().markerStyle().setVisible(true);
        functionStyle.dataStyle().markerStyle().setColor("black");
        functionStyle.dataStyle().markerStyle().setShape("dot");
        functionStyle.dataStyle().markerStyle().setSize(6);
        
        // Plot function.
        plotter.region(0).plot(fitResult.fittedFunction(), functionStyle);
        
        IHistogram1D hist = histogramFactory.createHistogram1D("histo", 50, -5.0, 5);
        for (int i=0; i<1000; i++) {
            hist.fill(r.nextGaussian());
        }
        
        // Create the Fitter and fit the profiles
        System.out.println("fitting histo");
        fitResult = fitter.fit(hist, "h1");
        //System.out.println("DONE fitting histo");
        
        //System.out.println("fitStatus = " + fitResult.fitStatus());
        
        //plotter.region(1).plot(hist, style);
        //plotter.region(1).plot(fitResult.fittedFunction(), functionStyle);        
        
        plotter.show();
    }
    
    public void test() {
        setBatchMode(false);
        plot();
        mode();
    }
}