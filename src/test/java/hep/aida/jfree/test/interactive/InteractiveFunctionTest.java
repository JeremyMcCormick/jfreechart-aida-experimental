package hep.aida.jfree.test.interactive;

import hep.aida.IFitResult;
import hep.aida.IFitter;
import hep.aida.IFunction;
import hep.aida.IFunctionFactory;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

public class InteractiveFunctionTest extends AbstractPlotTest {
    
    public void test() {

        setBatchMode(false);
        setWaitTime(100000);
        
        // Create a simple histogram.
        //IHistogram1D histogram = histogramFactory.createHistogram1D("histogram", 50, 0, 10);
        IHistogram1D histogram = histogramFactory.createHistogram1D("histogram", 100, -6, 6);        
        Random rand = new Random();
        for (int i = 0; i < 500; i++) {
            histogram.fill(rand.nextGaussian());
        }

        // Create Gaussian fitting function
        IFunctionFactory functionFactory = analysisFactory.createFunctionFactory(tree);
        IFunction function = functionFactory.createFunctionByName("Gaussian", "G");
        
        // Create the Fitter and fit the histogram
        // "leastsquares"
        // "cleverchisquared"
        // "binnedmaximumlikelihood"
        // "chi2"
        IFitter fitter = fitFactory.createFitter("chi2", "jminuit");
        double[] parameters = new double[3];
        parameters[0] = histogram.maxBinHeight();
        parameters[1] = histogram.mean();
        parameters[2] = histogram.rms();
        function.setParameters(parameters);
        IFitResult fitResult = fitter.fit(histogram, function);

        // Display the results
        plotter.createRegion();
        style.gridStyle().setVisible(false);
        plotter.region(0).plot(histogram, style);

        // Set function style.
        IPlotterStyle functionStyle = plotterFactory.createPlotterStyle();
        functionStyle.dataStyle().lineStyle().setColor("red");
        functionStyle.dataStyle().markerStyle().setVisible(true);
        functionStyle.dataStyle().markerStyle().setColor("black");
        functionStyle.dataStyle().markerStyle().setShape("dot");
        functionStyle.dataStyle().markerStyle().setSize(2);

        // Plot function.
        IFunction originalFunction = fitResult.fittedFunction();
        plotter.region(0).plot(fitResult.fittedFunction(), functionStyle);
        plotter.show();

        // Perform a bunch of fills.
        for (int i = 0; i < 10000; i++) {
            
            histogram.fill(rand.nextGaussian());                       
            if (i % 25 == 0) {
                System.out.println("refitting on fill #" + i);
                
                // Perform another fit.
                IFitResult refitResult = doExampleFit(functionFactory, histogram);
                
                // Copy fit results into original function that was plotted.  
                // This will trigger an update in the plotter region.
                originalFunction.setParameters(refitResult.fittedFunction().parameters());
                
                // Print refitted params.
                for (String parameterName : originalFunction.parameterNames()) {
                    System.out.println("  " + parameterName + " = " + originalFunction.parameter(parameterName));
                }
            }
            
            // Wait a bit.
            this.pause(1);
        }
        

        mode();
    }   
    
    IFitResult doExampleFit(IFunctionFactory functionFactory, IHistogram1D histogram) {        
        IFunction function = functionFactory.createFunctionByName("Gaussian", "G");        
        IFitter fitter = fitFactory.createFitter("chi2", "jminuit");
        double[] parameters = new double[3];
        parameters[0] = histogram.maxBinHeight();
        parameters[1] = histogram.mean();
        parameters[2] = histogram.rms();
        function.setParameters(parameters);
        IFitResult fitResult = fitter.fit(histogram, function);        
        return fitResult;
    }
}