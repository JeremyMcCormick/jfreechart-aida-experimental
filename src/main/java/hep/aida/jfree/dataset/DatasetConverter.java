package hep.aida.jfree.dataset;

import hep.aida.ICloud2D;
import hep.aida.IFunction;
import hep.aida.IHistogram2D;
import hep.aida.jfree.function.Function2DAdapter;

import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;

/**
 * This class converts data from AIDA objects into JFreeChart datasets. It is
 * used by the individual histogram converters to setup the backing data for
 * JFreeChart plots.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class DatasetConverter {

    public static XYZRangedDataset toXYZRangedDataset(IHistogram2D h2d) {
        XYZRangedDataset dataset = new XYZRangedDataset();
        int xbins = h2d.xAxis().bins();
        int ybins = h2d.yAxis().bins();
        int nvalues = xbins * ybins;
        double data[][] = new double[3][nvalues];
        int curr = 0;
        for (int i = 0; i < xbins; i++) {
            for (int j = 0; j < ybins; j++) {
                data[0][curr] = h2d.xAxis().binCenter(i);
                data[1][curr] = h2d.yAxis().binCenter(j);
                data[2][curr] = h2d.binHeight(i, j);
                ++curr;
            }
        }
        dataset.addSeries("data", data);
        return dataset;
    }

    public static XYZDataset convert(IHistogram2D h2d) {
        return new Histogram2DAdapter(h2d);
    }

    public static XYSeriesCollection convert(ICloud2D c2d) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries(c2d.title());
        for (int i = 0; i < c2d.entries(); i++) {
            series.add(c2d.valueX(i), c2d.valueY(i));
        }
        dataset.addSeries(series);
        return dataset;
    }
    
    public static XYDataset toXYDataset(IFunction function, double start, double end, int samples) {
        return DatasetUtilities.sampleFunction2D(new Function2DAdapter(function), start, end, samples, "functionData");
    }
}
