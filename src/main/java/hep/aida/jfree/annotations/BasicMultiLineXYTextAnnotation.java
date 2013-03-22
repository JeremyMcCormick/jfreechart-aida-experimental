package hep.aida.jfree.annotations;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.border.Border;

import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;


/**
 * Basic multi-line text annotation for XYPlot.
 * 
 * Currently does NOT support any of the following:
 * 
 * -rotation
 * -horizontal chart
 * -multiple fonts
 * -TextAnchor settings
 * -tooltips
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class BasicMultiLineXYTextAnnotation extends XYTextAnnotation {
    
    List<String> lines = new ArrayList<String>();
    boolean fontMetricsCalculated = false;
    int lineHeight;
    int lineSpacing;
    int width;
    int height;
    int paddingLeft;
    int paddingRight;
    int paddingBottom;
    Border border;
    
    public BasicMultiLineXYTextAnnotation(String text, double x, double y) {
        super(text, x, y);
        splitLines(text);        
    }
    
    private void splitLines(String text) {
        StringTokenizer st = new StringTokenizer(text, "\n");
        while (st.hasMoreTokens()) {
            lines.add(st.nextToken());
        }
    }
            
    public void setFont(Font font) {
        super.setFont(font);
        fontMetricsCalculated = false;
    }
    
    public void setBorder(Border border) {
        this.border = border;
    }
    
    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }
    
    private void calculateFontMetrics(Graphics2D graphics) {
        if (!fontMetricsCalculated) {
            FontMetrics metrics = graphics.getFontMetrics(this.getFont());
            lineHeight = metrics.getHeight();            
            for (String line : lines) {
                int lineWidth = metrics.stringWidth(line);
                if (lineWidth > width) { 
                    width = lineWidth;
                } 
            }
            paddingLeft = metrics.charWidth(' ');
            paddingRight = metrics.charWidth(' ');
            paddingBottom = metrics.charWidth(' ');
            width += (paddingLeft + paddingRight);
            height = (lineHeight * lines.size()) + paddingBottom;
            fontMetricsCalculated = true;
        }
    }
    
    public void addLine(String line) {
        lines.add(line);
    }
    
    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea,
            ValueAxis domainAxis, ValueAxis rangeAxis,
            int rendererIndex,
            PlotRenderingInfo info) {
        
        calculateFontMetrics(g2);
                
        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
                plot.getDomainAxisLocation(), orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(
                plot.getRangeAxisLocation(), orientation);        

        float textAnchorX = (float) domainAxis.valueToJava2D(
                this.getX(), dataArea, domainEdge);
        float textAnchorY = (float) rangeAxis.valueToJava2D(
                this.getY(), dataArea, rangeEdge);
        
        float rectangleX = textAnchorX;
        float rectangleY = textAnchorY;
        
        textAnchorX += paddingLeft;
                
        // Draw outline box.
        Rectangle2D rectangle = new Rectangle2D.Float(rectangleX, rectangleY, width, height);
        if (this.isOutlineVisible()) {
            g2.setStroke(this.getOutlineStroke());
            g2.setPaint(this.getBackgroundPaint());
            g2.draw(rectangle);
        }
       
        // Draw text lines.
        g2.setFont(getFont());
        g2.setPaint(getPaint());       
        textAnchorY += lineHeight;
        for (String line : lines) {
            g2.drawString(line, textAnchorX, textAnchorY);
            textAnchorY += lineHeight;
        }
    }
}
