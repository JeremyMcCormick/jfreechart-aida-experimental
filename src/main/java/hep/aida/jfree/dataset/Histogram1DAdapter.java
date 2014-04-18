package hep.aida.jfree.dataset;

import hep.aida.IHistogram1D;

import org.jfree.data.xy.AbstractIntervalXYDataset;

/**
 * <p>
 * This class adapts an <code>IHistogram1D</code> to a <code>AbstractIntervalXYDataset</code>
 * and provides four data series for different types of plot display, which include:
 * </p>
 * <ul> 
 * <li>values like an <code>XYIntervalSeries</code> for an <code>XYBarRenderer</code></li>
 * <li>errors like an <code>XYIntervalSeries</code> for an <code>XYErrorRenderer</code></li>
 * <li>step data like an <code>XYSeries</code> for an <code>XYStepRenderer</code></li> 
 * <li>point data similar to a <code>DefaultXYDataset</code> for an <code>XYLineAndShapeRenderer</code></li>
 * </ul>
 * <p>
 * Since JFreeChart does not support multiple <code>XYItemRenderer</code> objects assigned to the same dataset, 
 * the best way to support concurrent display of the same histogram with different styles is by adding the 
 * same adapter to the plot more than once with a different renderer.  The series visibility can then be set 
 * via the <code>XYItemRenderer</code> interface for each one.
 * </p>
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DAdapter extends AbstractIntervalXYDataset {
    
    public static final int VALUES = 0;
    public static final int ERRORS = 1;
    public static final int STEPS = 2;
    public static final int POINTS = 3;
        
    static final String[] KEYS = new String[] { 
        "values", 
        "errors",
        "steps",
        "points"
    };
    
    IHistogram1D histogram;
    
    public Histogram1DAdapter(IHistogram1D histogram) {
        this.histogram = histogram;
    }
     
    @Override
    public Number getStartX(int series, int item) {
        if (series == VALUES)
            return histogram.axis().binLowerEdge(item);
        else if (series == ERRORS)
            return histogram.axis().binCenter(item);
        else if (series == POINTS || series == STEPS) 
            return getX(series, item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public Number getEndX(int series, int item) {
        if (series == VALUES)
            return histogram.axis().binUpperEdge(item);
        else if (series == ERRORS)
            return histogram.axis().binCenter(item);
        else if (series == POINTS || series == STEPS) 
            return getX(series, item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public Number getStartY(int series, int item) {
        if (series == VALUES)
            return 0;
        else if (series == ERRORS)
            return histogram.binHeight(item) - histogram.binError(item);
        else if (series == POINTS || series == STEPS) 
            return getY(series, item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public Number getEndY(int series, int item) {
        if (series == VALUES)
            return histogram.binHeight(item);
        else if (series == ERRORS)
            return histogram.binHeight(item) + histogram.binError(item);
        else if (series == POINTS || series == STEPS) 
            return getY(series, item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public int getItemCount(int series) {
        if (series == VALUES || series == ERRORS || series == POINTS)
            return histogram.axis().bins();
        else if (series == STEPS)
            return histogram.axis().bins() + 3;
        else
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public Number getX(int series, int item) {
        if (series == VALUES || series == ERRORS)
            return histogram.axis().binCenter(item);
        else if (series == STEPS)
            if (item == 0)
                return histogram.axis().binLowerEdge(0);
            else if (item >= (getItemCount(STEPS) - 1))
                return histogram.axis().binUpperEdge(histogram.axis().bins() - 1);
            else
                return histogram.axis().binLowerEdge(item - 1);
        else if (series == POINTS)
            return histogram.axis().binCenter(item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);            
    }

    @Override
    public Number getY(int series, int item) {
        if (series == VALUES || series == ERRORS)
            return histogram.binHeight(item);
        else if (series == STEPS)
            if (item == 0 || item == (getItemCount(STEPS) - 1))
                return 0;
            else if (item == (getItemCount(STEPS) - 2))
                return histogram.binHeight(histogram.axis().bins() - 1);
            else
                return histogram.binHeight(item - 1);
        else if (series == POINTS)
            return histogram.binHeight(item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public int getSeriesCount() {
        return KEYS.length;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return KEYS[series];
    }    
}