package hep.aida.jfree.dataset;

import hep.aida.IHistogram1D;

import org.jfree.data.xy.AbstractIntervalXYDataset;

/**
 * <p>
 * This is a Dataset adapter for 1D histograms that provides four 
 * series for different types of display:
 * </p>
 * <ul> 
 * <li>data series for use with a <code>XYBarRenderer</code></li>
 * <li>errors series for use with the <code>XYErrorRenderer</code></li>
 * <li>step series data displayable with a <code>XYStepRenderer</code></li> 
 * <li>points series usable with the <code>XYLineAndShapeRenderer</code></li>
 * </ul>
 * <p>
 * Since JFreeChart does not support multiple <code>Renderer</code> objects
 * assigned to the same dataset, the best way to support overlayed display
 * of the same histogram is by creating multiple adapters that are assigned 
 * to the same <code>IHistogram1D</code>.
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
    
    Histogram1DAdapter(IHistogram1D histogram) {
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