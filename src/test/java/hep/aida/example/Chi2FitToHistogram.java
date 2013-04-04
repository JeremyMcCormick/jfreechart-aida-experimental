package hep.aida.example;

import hep.aida.IAnalysisFactory;
import hep.aida.IFitFactory;
import hep.aida.IFitResult;
import hep.aida.IFitter;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;

import java.util.Random;

public class Chi2FitToHistogram {
   public static void main(String[] args) {
      // Create factories
      IAnalysisFactory analysisFactory = IAnalysisFactory.create();
      IHistogramFactory histogramFactory = analysisFactory.createHistogramFactory(analysisFactory.createTreeFactory().create());
      IPlotter plotter = analysisFactory.createPlotterFactory().create("Plot");
      IFitFactory fitFactory = analysisFactory.createFitFactory();
      
      // Create 1D histogram
      IHistogram1D h1d = histogramFactory.createHistogram1D("Gaussian Distribution",100,-5,5);
      
      // Fill 1D histogram with Gaussian
      Random r = new Random();
      for (int i=0; i<5000; i++)
         h1d.fill(r.nextGaussian());
      
      // Do Fit
      IFitter fitter = fitFactory.createFitter("chi2");
      IFitResult result = fitter.fit(h1d,"g");
      
      // Show results
      plotter.createRegions(1,1,0);
      plotter.region(0).plot(h1d);
      plotter.region(0).plot(result.fittedFunction());
      plotter.show();
      
      try {
          Thread.sleep(10000);
      } catch (InterruptedException e) {          
      }
   }
}