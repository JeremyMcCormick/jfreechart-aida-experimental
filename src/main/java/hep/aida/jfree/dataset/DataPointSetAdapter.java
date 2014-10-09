package hep.aida.jfree.dataset;

import hep.aida.IDataPointSet;

import org.jfree.data.xy.AbstractIntervalXYDataset;

/**
 * Adapt an AIDA <code>IDataPointSet</code> to an <code>AbstractIntervalXYDataset</code>
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class DataPointSetAdapter extends AbstractIntervalXYDataset {

    IDataPointSet dataPointSet;
    
    public static final int VALUES = 0;
    public static final int LINES = 1;
    public static final int ERRORS = 2;
    
    int coordinate = 0;
        
    static final String[] KEYS = new String[] { 
        "values", 
        "lines",
        "errors",
    };
    
    public DataPointSetAdapter(IDataPointSet dataPointSet, int coordinate) {
        this.dataPointSet = dataPointSet;
        this.coordinate = coordinate;
    }
    
    public DataPointSetAdapter(IDataPointSet dataPointSet) {
        this.dataPointSet = dataPointSet;
    }

    @Override
    public Number getEndX(int series, int item) {
        return item;
    }

    @Override
    public Number getEndY(int series, int item) {
        if (series == VALUES || series == LINES) {
            return dataPointSet.point(item).coordinate(coordinate).value();
        } else if (series == ERRORS) {
            return dataPointSet.point(item).coordinate(coordinate).value() + dataPointSet.point(item).coordinate(coordinate).errorPlus(); 
        } else {
            throw new IllegalArgumentException("Unknown series: " + series);
        }
    }

    @Override
    public Number getStartX(int series, int item) {
        return item;
    }

    @Override
    public Number getStartY(int series, int item) {
        if (series == VALUES || series == LINES) {
            return dataPointSet.point(item).coordinate(coordinate).value();
        } else if (series == ERRORS) {
            return dataPointSet.point(item).coordinate(coordinate).value() - dataPointSet.point(item).coordinate(coordinate).errorMinus(); 
        } else {
            throw new IllegalArgumentException("Unknown series: " + series);
        }
    }

    @Override
    public int getItemCount(int series) {
        return this.dataPointSet.size();
    }

    @Override
    public Number getX(int series, int item) {
        return item;
    }

    @Override
    public Number getY(int series, int item) {
        return dataPointSet.point(item).coordinate(coordinate).value();
    }

    @Override
    public int getSeriesCount() {
        return KEYS.length;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return KEYS[series];
    }           
    
    public double getMaxY() {
        double maxY = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < this.getItemCount(VALUES); i++) {
            if (getEndY(ERRORS, i).doubleValue() > maxY) {
                maxY = getEndY(ERRORS, i).doubleValue();
            }
        }
        return maxY;
    }
    
    public double getMinY() {
        double minY = Double.POSITIVE_INFINITY;
        for (int i = 0; i < this.getItemCount(VALUES); i++) {
            if (getEndY(ERRORS, i).doubleValue() < minY) {
                minY = getStartY(ERRORS, i).doubleValue();
            }
        }
        return minY;
    }
    
    public IDataPointSet getDataPointSet() {
        return this.dataPointSet;
    }
}
