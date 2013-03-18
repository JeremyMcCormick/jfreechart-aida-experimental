package hep.aida.jfree;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.xy.DefaultXYZDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class XYZRangedDataset extends DefaultXYZDataset {

    public static final class ZRange {

        double zmin = Double.MAX_VALUE;
        double zmax = Double.MIN_VALUE;
        double range = 0;

        public double getZMin() {
            return zmin;
        }

        public double getZMax() {
            return zmax;
        }

        public double getRange() {
            return range;
        }
    }

    Map<Comparable, ZRange> ranges = new HashMap<Comparable, ZRange>();

    private ZRange computeZRange(double[][] data) {
        ZRange range = new ZRange();
        int n = data[2].length;
        for (int i = 0; i < n; i++) {
            double z = data[2][i];
            if (z < range.zmin)
                range.zmin = z;
            if (z > range.zmax)
                range.zmax = z;
        }
        range.range = range.zmax - range.zmin;
        return range;
    }

    public XYZRangedDataset() {
    }

    public void addSeries(Comparable seriesKey, double[][] data) {
        super.addSeries(seriesKey, data);
        ZRange range = computeZRange(data);
        ranges.put(seriesKey, range);
    }

    public ZRange getZRange(int series) {
        Comparable seriesKey = this.getSeriesKey(series);
        return ranges.get(seriesKey);
    }
}
