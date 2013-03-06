package hep.aida.jfree;

import hep.aida.jfree.XYZRangedDataset.ZRange;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
// FIXME: This will only work with data that has fixed sized binning.  Another
//        renderer and probably dataset type is needed for displaying variable bin data.
public class XYBoxRenderer extends XYBlockRenderer implements XYItemRenderer 
{
    double boxWidth;
    double boxHeight;
    
    Color transparent = new Color(255, 255, 255, 0);
               
    public XYBoxRenderer(double boxWidth, double boxHeight)
    {
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }
    
    private double getHeightScaled(double z, ZRange range)
    {
        return (z / range.zmax) * boxHeight;
    }
    
    private double getWidthScaled(double z, ZRange range)
    {
        return (z / range.zmax) * boxWidth;
    }
        
    public void drawItem(
            Graphics2D g2, 
            XYItemRendererState state, 
            Rectangle2D dataArea, 
            PlotRenderingInfo info, 
            XYPlot plot, 
            ValueAxis domainAxis, 
            ValueAxis rangeAxis, 
            XYDataset dataset, 
            int series, 
            int item, 
            CrosshairState crosshairState, 
            int pass)
    {        
        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double z = 0.0;
        if (dataset instanceof XYZDataset) {
            z = ((XYZDataset) dataset).getZValue(series, item);
        }
        
        if (z == 0)
            return;
        
        //System.out.println("XYBoxRenderer.drawItem");
        //System.out.println("  series = " + item);
        //System.out.println("  item = " + item);
        //System.out.println("  boxWidth = " + boxWidth);
        //System.out.println("  boxHeight = " + boxHeight);
        
        ZRange zrange = null;
        if (dataset instanceof XYZRangedDataset) {
            //System.out.println("  getting zrange");
            zrange = ((XYZRangedDataset)dataset).getZRange(series);
            //System.out.println("  zmin = " + zrange.zmin);
            //System.out.println("  zmax = " + zrange.zmax);
            //System.out.println("  range = " + zrange.range);
        } else {
            throw new IllegalArgumentException("Dataset is wrong type: " + dataset.getClass().getCanonicalName());
        }
        
        //System.out.println("  x = " + x);
        //System.out.println("  y = " + y);
        //System.out.println("  z = " + z);
                
        double heightScaled = this.getHeightScaled(z, zrange);
        double widthScaled = this.getWidthScaled(z, zrange);
                
        double xx0 = domainAxis.valueToJava2D(x, dataArea, plot.getDomainAxisEdge());
        double yy0 = rangeAxis.valueToJava2D(y, dataArea, plot.getRangeAxisEdge());
        double xx1 = domainAxis.valueToJava2D(x + widthScaled, dataArea, plot.getDomainAxisEdge());
        double yy1 = rangeAxis.valueToJava2D(y + heightScaled, dataArea, plot.getRangeAxisEdge());
        
        //System.out.println("  xx0 = " + xx0);
        //System.out.println("  yy0 = " + yy0);
        //System.out.println("  xx1 = " + xx1);
        //System.out.println("  yy1 = " + yy1);  
                                
        Rectangle2D box;
        PlotOrientation orientation = plot.getOrientation();
        
        double widthDraw =  Math.abs(xx1 - xx0);
        double heightDraw = Math.abs(yy1 - yy0);
        
        //System.out.println("  widthDraw = " + widthDraw);
        //System.out.println("  heightDraw = " + heightDraw);
        
        if (orientation.equals(PlotOrientation.HORIZONTAL)) {
            // FIXME: Have not checked this.
            box = new Rectangle2D.Double(
                    Math.min(yy0, yy1) + heightDraw/2,
                    Math.min(xx0, xx1) - widthDraw/2,
                    Math.abs(yy1 - yy0),
                    Math.abs(xx0 - xx1));
        }
        else {
            // Don't know if this adjustment to XY works in all generality
            // but seems okay for test case.
            box = new Rectangle2D.Double(
                    Math.min(xx0, xx1) - widthDraw/2,
                    Math.min(yy0, yy1) + heightDraw/2, 
                    Math.abs(xx1 - xx0),
                    Math.abs(yy1 - yy0));
                       
        }
        g2.setPaint(this.getSeriesOutlinePaint(series));                
        Stroke stroke = this.getSeriesOutlineStroke(series);
        if (stroke != null)
            g2.setStroke(stroke);
        else
            g2.setStroke(new BasicStroke(1.0f));
        g2.draw(box);        
        Paint paint = this.getSeriesFillPaint(series);
        if (paint != null) {
            g2.fill(box);
        }
        
        // TEST TEST TEST
        //Shape centerDot = new Ellipse2D.Double(xx0-4./2., yy0-4./2., 4., 4.);
        //g2.draw(centerDot);
        /////

        EntityCollection entities = state.getEntityCollection();
        if (entities != null) {
           addEntity(entities, box, dataset, series, item, 0.0, 0.0);
        }
    }    
}
