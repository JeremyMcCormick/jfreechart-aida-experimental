package hep.aida.jfree.test.interactive;

import hep.aida.IFitResult;
import hep.aida.IFitter;
import hep.aida.IFunction;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
// http://aida.freehep.org/doc/v3.3.0/UsersGuide/fitter.jsp#Chi2FitToHistogram        
public class InteractiveHistogramFitTest extends AbstractPlotTest{
    
    String[] fitTypes = new String[] { 
            "ChiSquared", 
            "CleverChiSquared", 
            "BinnedMaximumLikelihood", 
            "LeastSquares"/*, 
            "UnbinnedMaximumLikelihood"*/ };
    
    /*
    public static String[] defaultNames = { "g2", "g", "e", 
        "moyal", "lorentzian", 
        "p0", "p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9" };
    
    public String fitStatus(int fitStatus) {
        switch ( fitStatus ) {
            case 1 :
                return "Diagonal approximation";
            case 2 :
                return "Converged with non positive definite matrix";
            case 3 :
                return "Converged";
            case 4 :
                return "Converged with small gradient";
            case 5 :
                return "Converged with small step size";
            case 6 :
                return "Not Converged";
            case 7 :
                return "Stopped, reached max iterations";
            case 8 :
                return "Stopped, too many large steps. Function might be unbound.";
            default :
                return "Undefined";                
        }
    }
            */
    
    public void plot() {
             
       // Create 1D histogram
       IHistogram1D h1d = histogramFactory.createHistogram1D("Gaussian Distribution",100,-5,5);
       
       // Fill 1D histogram with Gaussian
       Random r = new Random();
       for (int i=0; i<5000; i++) {
          h1d.fill(r.nextGaussian());
       }
       
       style.gridStyle().setVisible(false);
       
       style.statisticsBoxStyle().setVisible(true);
       style.statisticsBoxStyle().boxStyle().setX(1.5);
       style.statisticsBoxStyle().boxStyle().setY(220);
       style.statisticsBoxStyle().boxStyle().borderStyle().setColor("black");
       
       IPlotterStyle functionStyle = plotterFactory.createPlotterStyle();
       functionStyle.dataStyle().lineStyle().setThickness(5);
       functionStyle.dataStyle().lineStyle().setColor("red");
       
       plotter.createRegions(2, 2);
       
       for (int i=0; i<plotter.numberOfRegions(); i++) {           
           plotter.region(i).plot(h1d, style);
           plotter.region(i).setTitle(fitTypes[i]);
           IFunction fit = doFit(fitTypes[i], h1d);
           plotter.region(i).plot(fit, functionStyle);
           System.out.println();
       }              
    }
    
    private IFunction doFit(String fitType, IHistogram1D h) {
        //System.out.println("fitType = " + fitType);
        IFitter fitter = analysisFactory.createFitFactory().createFitter(fitType);        
        if (fitter == null)
            throw new RuntimeException("failed to create fitter with type " + fitType);
        //System.out.println("created fitter with type = " + fitter.getClass().getCanonicalName());
        IFitResult fitResult = fitter.fit(h, "g");
        //System.out.println("fitStatus = " + fitStatus(fitResult.fitStatus()));
        //System.out.println("function type = " + fitResult.fittedFunction().getClass().getCanonicalName());
        //System.out.println("fit method name = " + fitResult.fitMethodName());
        //System.out.println("values...");
        //IFunction function = fitResult.fittedFunction();
        //for (int i=0, n=h.axis().bins(); i<n; i++) {
        //    System.out.println("bin[" + i + "] => f(" + h.axis().binCenter(i) + ") = " + function.value(new double[] {h.axis().binCenter(i)}));
        //}
        return fitResult.fittedFunction();
    }
    
    public void test() {
        setBatchMode(false);
        plot();
        mode();
    }

}
