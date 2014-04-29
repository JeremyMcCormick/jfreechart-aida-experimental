package hep.aida.jfree.renderer;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

/**
 *   
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public abstract class AbstractPaintScale implements PaintScale {

    ColorScale colorScale;    
    static Color transparent = new Color(255, 255, 255, 0);
    boolean showZeroHeightBins = true;
    
    protected AbstractPaintScale() {
    }
        
    public void setLogScale() {
        colorScale.setScalingLogarithmic();
    }
        
    public void setLinearScale() {
        colorScale.setScalingLinear();
    }
    
    @Override
    public double getLowerBound() {
        return colorScale.getMinimum();
    }

    @Override
    public double getUpperBound() {
        return colorScale.getMaximum();
    }
    
    public void setLowerBound(double lowerBound) {
        colorScale.setMinimum(lowerBound);
    }
    
    public void setUpperBound(double upperBound) {
        colorScale.setMaximum(upperBound);
    }
    
    public void setBounds(double lowerBound, double upperBound) {
        colorScale.setMinimum(lowerBound);
        colorScale.setMaximum(upperBound);
    }
    
    public void setShowZeroHeightBins(boolean showZeroHeightBins) {
        this.showZeroHeightBins = showZeroHeightBins;
    }
    
    @Override
    public Paint getPaint(double value) { 
        if (!showZeroHeightBins && value == 0) {
            // Use a transparent Color when zero height bins are not to be drawn.
            return transparent;
        }
        return colorScale.getColor(value);
    }
}