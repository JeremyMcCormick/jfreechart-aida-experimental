package hep.aida.jfree.renderer;

import java.awt.Color;

public class CustomPaintScale extends AbstractPaintScale {
        
    public CustomPaintScale(Color coldColor, Color hotColor, double lowerBound, double upperBound) {
        GradientScale gradientScale = new GradientScale();
        gradientScale.setColdColor(coldColor);
        gradientScale.setHotColor(hotColor);
        colorScale = gradientScale;
        colorScale.setMinimum(lowerBound);
        colorScale.setMaximum(upperBound);
    }
}
