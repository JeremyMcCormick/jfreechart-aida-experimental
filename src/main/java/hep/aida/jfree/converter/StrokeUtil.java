package hep.aida.jfree.converter;

import hep.aida.IGridStyle;
import hep.aida.ILineStyle;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
final class StrokeUtil
{
    private StrokeUtil()
    {}

    static float lineThickness(int thickness)
    {
        return ((float) thickness) / (float) 2.;
    }
    
    static Stroke toStroke(IGridStyle style)
    {        
        // Line thickness.
        float lineThickness = lineThickness(style.thickness());

        // The line type, e.g. solid, dashed, etc.
        LineType lineType = LineType.getLineType(style.lineType());

        // Create the stroke for the line.
        BasicStroke stroke = new BasicStroke(lineThickness, 
                                             BasicStroke.CAP_SQUARE, 
                                             BasicStroke.JOIN_MITER, 
                                             10.0f, 
                                             lineType.getDashArray(), 
                                             0.0f);
        
        return stroke;
    }
    
    static Stroke toStroke(ILineStyle style)
    {        
        // Line thickness.
        float lineThickness = lineThickness(style.thickness());

        // The line type, e.g. solid, dashed, etc.
        LineType lineType = LineType.getLineType(style.lineType());

        // Create the stroke for the line.
        BasicStroke stroke = new BasicStroke(lineThickness, 
                                             BasicStroke.CAP_SQUARE, 
                                             BasicStroke.JOIN_MITER, 
                                             10.0f, 
                                             lineType.getDashArray(), 
                                             0.0f);
        
        return stroke;
    }    
}
