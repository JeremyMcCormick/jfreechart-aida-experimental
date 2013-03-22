package hep.aida.jfree.annotations;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;


/**
 * Basic multi-line text annotation for XYPlot.
 * 
 * Currently does NOT support any of the following:
 * 
 * -rotation
 * -horizontal chart
 * -multiple fonts
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
    
    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }
    
    private void calculateFontMetrics(Graphics2D graphics) {
        if (!fontMetricsCalculated) {
            System.out.println("calculating font metrics");
            FontMetrics metrics = graphics.getFontMetrics(this.getFont());
            lineHeight = metrics.getHeight();            
            for (String line : lines) {
                int lineWidth = metrics.stringWidth(line);
                if (lineWidth > width) { 
                    width = lineWidth;
                } 
            }
            height = lineHeight * lines.size();
            System.out.println("lineHeight = " + lineHeight);
            System.out.println("height = " + height);
            System.out.println("width = " + width);            
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

        float textAnchorY = (float) domainAxis.valueToJava2D(
                this.getX(), dataArea, domainEdge);
        float textAnchorX = (float) rangeAxis.valueToJava2D(
                this.getY(), dataArea, rangeEdge);
        
        float boxAnchorY = textAnchorY;
        float boxAnchorX = textAnchorX;
        
        /*
        if (orientation == PlotOrientation.HORIZONTAL) {           
            float tempAnchor = anchorX;
            anchorX = anchorY;
            anchorY = tempAnchor;
        }
        */
        
        // Draw box.
        Rectangle2D rectangle = new Rectangle2D.Float(height, width, boxAnchorX, boxAnchorY);
        if (this.isOutlineVisible()) {
            g2.setStroke(this.getOutlineStroke());
        }
        g2.setPaint(this.getBackgroundPaint());
        g2.draw(rectangle);
       
        // Draw text lines.
        g2.setFont(getFont());
        g2.setPaint(getPaint());       
        for (String line : lines) {
            //System.out.println("adding line: " + line);
            TextUtilities.drawRotatedString(line, g2, textAnchorY, textAnchorX,
                    getTextAnchor(), getRotationAngle(), getRotationAnchor());
            textAnchorX += lineHeight;
        }
        
        /*

        Shape hotspot = TextUtilities.calculateRotatedStringBounds(
                getText(), g2, anchorX, anchorY, getTextAnchor(),
                getRotationAngle(), getRotationAnchor());
        if (this.getBackgroundPaint() != null) {
            g2.setPaint(this.getBackgroundPaint());
            g2.fill(hotspot);
        }
        g2.setPaint(getPaint());
        TextUtilities.drawRotatedString(getText(), g2, anchorX, anchorY,
                getTextAnchor(), getRotationAngle(), getRotationAnchor());
        if (this.isOutlineVisible()) {
            g2.setStroke(this.getOutlineStroke());
            g2.setPaint(this.getOutlinePaint());
            g2.draw(hotspot);
        }

        String toolTip = getToolTipText();
        String url = getURL();
        if (toolTip != null || url != null) {
            addEntity(info, hotspot, rendererIndex, toolTip, url);
        }
        */
    }
}
