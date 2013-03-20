package hep.aida.jfree.plotter;

import hep.aida.IPlotter;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class PlotterFactory extends hep.aida.ref.plotter.PlotterFactory {

    String name;

    public PlotterFactory() {
        super();
    }

    public PlotterFactory(String name) {
        super();
        this.name = name;
    }

    public IPlotter create(String plotterName) {
        return new Plotter();
    }

    public IPlotter create() {
        return create((String) null);
    }

    /*
     public IPlotterStyle createPlotterStyle() {
         //System.out.println("createPlotterStyle"); IPlotterStyle style =
         super.createPlotterStyle(); applyDefaultPlotterStyle(style); 
         return style; }
         
     public IPlotterStyle createPlotterStyle(IPlotterStyle style) {
         //System.out.println("createPlotterStyle(style)");
         applyDefaultPlotterStyle(style); 
         return style; 
     }
      
     private static void applyDefaultPlotterStyle(IPlotterStyle style) {
         //System.out.println("applyDefaultPlotterStyle");
         style.xAxisStyle().setParameter("allowZeroSuppression", "false");
         style.yAxisStyle().setParameter("allowZeroSuppression", "false"); 
     }
     */
}