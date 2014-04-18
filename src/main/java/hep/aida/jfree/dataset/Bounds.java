package hep.aida.jfree.dataset;

import org.jfree.data.xy.XYZDataset;

public class Bounds {

    double minimum = Double.POSITIVE_INFINITY;
    double maximum = Double.NEGATIVE_INFINITY;
    double range = Double.NaN;

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getRange() {
        return range;
    }
    
    public Bounds computeBounds(double[][] data, int index) {        
        if (index < 0)
            throw new IllegalArgumentException("Invalid index.");
        if (data[index].length == 0)
            throw new IllegalArgumentException("No data.");                                 
        reset();
        int n = data[index].length;
        for (int i = 0; i < n; i++) {
            double value = data[index][i];
            processValue(value);
        }
        range = maximum - minimum;
        return this;
    }
    
    public Bounds computeZBounds(XYZDataset dataset, int series) {
        if (series < 0)
            throw new IllegalArgumentException("The series is invalid.");
        if (dataset == null)
            throw new IllegalArgumentException("The dataset is null.");
        reset();
        int itemCount = dataset.getItemCount(series);
        for (int item=0; item<itemCount; item++) {
            double value = dataset.getZValue(series, item);
            processValue(value);
        }
        return this;
    }
    
    public boolean isValid() {
        return minimum < maximum;
    }
    
    private void reset() {
        minimum = Double.POSITIVE_INFINITY;
        maximum = Double.NEGATIVE_INFINITY;
        range = Double.NaN;
    }
    
    private void processValue(double value) {
        if (value < minimum)
            minimum = value;
        if (value > maximum)
            maximum = value;
    }
}