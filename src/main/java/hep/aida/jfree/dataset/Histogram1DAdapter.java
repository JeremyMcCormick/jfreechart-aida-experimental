package hep.aida.jfree.dataset;

import hep.aida.IHistogram1D;

import org.jfree.data.xy.AbstractIntervalXYDataset;

/**
 * This is a Dataset adapter for 1D histograms.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DAdapter extends AbstractIntervalXYDataset {
    
    static int VALUES_SERIES = 0;
    static int ERRORS_SERIES = 1;
    
    static String VALUES_KEY = "values";
    static String ERRORS_KEY = "errors";
    
    static String[] KEYS = new String[] {VALUES_KEY, ERRORS_KEY};
    
    IHistogram1D histogram;
    
    Histogram1DAdapter(IHistogram1D histogram) {
        this.histogram = histogram;
    }

    // DATA
    //axis.binCenter(i), axis.binLowerEdge(i), axis.binUpperEdge(i), h1d.binHeight(i), 0.0, h1d.binHeight(i)
    //add(double x, double xLow, double xHigh, double y, double yLow, double yHigh)
    
    // ERRORS
    //double error = h1d.binError(i);
    //errors.add(axis.binCenter(i), axis.binCenter(i), axis.binCenter(i), h1d.binHeight(i), h1d.binHeight(i) - error, h1d.binHeight(i) + error);
    
    @Override
    public Number getStartX(int series, int item) {
        // data
        if (series == VALUES_SERIES)
            return histogram.axis().binLowerEdge(series);
        else if (series == ERRORS_SERIES)
            return histogram.axis().binCenter(item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public Number getEndX(int series, int item) {
        // data
        if (series == VALUES_SERIES)
            return histogram.axis().binUpperEdge(item);
        // errors
        else if (series == ERRORS_SERIES)
            return histogram.axis().binCenter(item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public Number getStartY(int series, int item) {
        // for data Y always at zero
        if (series == VALUES_SERIES)
            return 0;
        // errors
        else if (series == ERRORS_SERIES)
            return histogram.binHeight(item) - histogram.binError(item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public Number getEndY(int series, int item) {
        // data
        if (series == VALUES_SERIES)
            return histogram.binHeight(item);
        else if (series == ERRORS_SERIES)
            return histogram.binHeight(item) + histogram.binError(item);
        else 
            throw new IllegalArgumentException("Unknown series " + series);
    }

    @Override
    public int getItemCount(int series) {
        return histogram.entries();
    }

    @Override
    public Number getX(int series, int item) {
        return histogram.axis().binCenter(item);
    }

    @Override
    public Number getY(int series, int item) {
        return histogram.binHeight(item);
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