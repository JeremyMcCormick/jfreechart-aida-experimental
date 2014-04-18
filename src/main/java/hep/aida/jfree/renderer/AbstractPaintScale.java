package hep.aida.jfree.renderer;

import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

/**
 *   
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public abstract class AbstractPaintScale implements PaintScale {

    ColorScale colorScale;
    
    protected AbstractPaintScale() {
    }
        
    public void setLogScale() {
        colorScale.setScalingLogarithmic();
    }
        
    public void setLinearScale() {
        colorScale.setScalingLinear();
    }
    
    public double getLowerBound() {
        return colorScale.getMinimum();
    }

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

    public Paint getPaint(double value) {
        return colorScale.getColor(value);
    }
}

