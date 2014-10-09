package hep.aida.jfree.renderer;

import java.awt.BasicStroke;
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
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.IntervalXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

public class XYVariableBinWidthBlockRenderer extends AbstractXYItemRenderer implements HasPaintScale {

    AbstractPaintScale paintScale;

    public void setPaintScale(AbstractPaintScale paintScale) {
        this.paintScale = paintScale;
    }
    
    public AbstractPaintScale getPaintScale() {
        return paintScale;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        
        if (!this.isSeriesVisible(series))
            return;

        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double z = 0.0;
        if (dataset instanceof XYZDataset) {
            z = ((XYZDataset) dataset).getZValue(series, item);
        }

        double binWidth;
        double binHeight;
        if (dataset instanceof IntervalXYZDataset) {
            IntervalXYZDataset intervalDataset = (IntervalXYZDataset) dataset;
            double startX = intervalDataset.getStartXValue(series, item).doubleValue();
            double endX = intervalDataset.getEndXValue(series, item).doubleValue();
            binWidth = endX - startX;
            double startY = intervalDataset.getStartYValue(series, item).doubleValue();
            double endY = intervalDataset.getEndYValue(series, item).doubleValue();
            binHeight = endY - startY;
        } else {
            throw new IllegalArgumentException("Dataset is not an IntervalXYZDataset.");
        }

        if (z == 0)
            return;

        double xx0 = domainAxis.valueToJava2D(x, dataArea, plot.getDomainAxisEdge());
        double yy0 = rangeAxis.valueToJava2D(y, dataArea, plot.getRangeAxisEdge());
        double xx1 = domainAxis.valueToJava2D(x + binWidth, dataArea, plot.getDomainAxisEdge());
        double yy1 = rangeAxis.valueToJava2D(y + binHeight, dataArea, plot.getRangeAxisEdge());

        Rectangle2D box;
        PlotOrientation orientation = plot.getOrientation();

        double widthDraw = Math.abs(xx1 - xx0);
        double heightDraw = Math.abs(yy1 - yy0);

        if (orientation.equals(PlotOrientation.HORIZONTAL)) {
            box = new Rectangle2D.Double(Math.min(yy0, yy1) + heightDraw / 2, Math.min(xx0, xx1) - widthDraw / 2, Math.abs(yy1 - yy0), Math.abs(xx0 - xx1));
        } else {
            box = new Rectangle2D.Double(Math.min(xx0, xx1) - widthDraw / 2, Math.min(yy0, yy1) + heightDraw / 2, Math.abs(xx1 - xx0), Math.abs(yy1 - yy0));
        }
        Paint paint = paintScale.getPaint(z);
        g2.setPaint(paint);
        Stroke stroke = this.getSeriesStroke(series);
        if (stroke != null) {
            g2.setStroke(stroke);
        } else {
            g2.setStroke(new BasicStroke(1.0f));
        }
        g2.draw(box);
        if (paint != null) {
            g2.fill(box);
        }
        EntityCollection entities = state.getEntityCollection();
        if (entities != null) {
            addEntity(entities, box, dataset, series, item, 0.0, 0.0);
        }
    }

}
