package hep.aida.jfree.dataset;

import hep.aida.IAxis;
import hep.aida.ICloud2D;
import hep.aida.IFunction;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;
import hep.aida.jfree.function.Function2DAdapter;

import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
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
        DefaultXYZDataset dataset = new DefaultXYZDataset();
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
                // System.out.println("binHeight[" + i + "][" + j + "]  = " +
                // h2d.binHeight(i, j));
                ++curr;
            }
        }
        dataset.addSeries("data", data);
        return dataset;
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

    /**
     * 
     * @param h1d
     * @return An array of datasets; index 0 = values; index 1 = errors
     */

    public static XYIntervalSeriesCollection[] forBarChart(IHistogram1D h1d) {
        XYIntervalSeriesCollection valuesDataset = new XYIntervalSeriesCollection();
        XYIntervalSeries values = new XYIntervalSeries("values");

        XYIntervalSeriesCollection errorsDataset = new XYIntervalSeriesCollection();
        XYIntervalSeries errors = new XYIntervalSeries("errors");

        IAxis axis = h1d.axis();
        int nbins = axis.bins();
        for (int i = 0; i < nbins; i++) {
            values.add(axis.binCenter(i), axis.binLowerEdge(i), axis.binUpperEdge(i), h1d.binHeight(i), 0.0, h1d.binHeight(i));
            double error = h1d.binError(i);
            errors.add(axis.binCenter(i), axis.binCenter(i), axis.binCenter(i), h1d.binHeight(i), h1d.binHeight(i) - error, h1d.binHeight(i) + error);
        }
        valuesDataset.addSeries(values);
        errorsDataset.addSeries(errors);

        XYIntervalSeriesCollection[] datasets = new XYIntervalSeriesCollection[2];
        datasets[0] = valuesDataset;
        datasets[1] = errorsDataset;
        return datasets;
    }

    public static XYDataset[] forStepChart(IHistogram1D h1d) {
        XYDataset[] datasets = new XYDataset[2];

        // Create two datasets, one for values, and one for errors.
        XYSeries values = new XYSeries("values");
        XYIntervalSeries errors = new XYIntervalSeries("errors");
        IAxis axis = h1d.axis();
        int nbins = axis.bins();
        values.add(axis.binLowerEdge(0), 0); // left-most line
        for (int i = 0; i < nbins; i++) {
            values.add(axis.binLowerEdge(i), h1d.binHeight(i));
            double error = h1d.binError(i);
            errors.add(axis.binCenter(i), axis.binCenter(i), axis.binCenter(i), h1d.binHeight(i), h1d.binHeight(i) - error, h1d.binHeight(i) + error);
            if (i == (nbins - 1)) {
                values.add(axis.binUpperEdge(i), h1d.binHeight(i));
            }
        }
        values.add(axis.binUpperEdge(nbins - 1), 0); // right-most line

        XYSeriesCollection valuesDataset = new XYSeriesCollection();
        valuesDataset.addSeries(values);
        XYIntervalSeriesCollection errorsDataset = new XYIntervalSeriesCollection();
        errorsDataset.addSeries(errors);

        datasets[0] = valuesDataset;
        datasets[1] = errorsDataset;

        return datasets;
    }

    public static XYDataset forPoints(IHistogram1D h1d) {
        DefaultXYDataset ds = new DefaultXYDataset();

        IAxis axis = h1d.axis();
        int nbins = axis.bins();

        double data[][] = new double[2][nbins];

        for (int i = 0; i < nbins; i++) {
            data[0][i] = axis.binCenter(i);
            data[1][i] = h1d.binHeight(i);
        }

        ds.addSeries("data", data);

        return ds;
    }
    
    public static XYDataset toXYDataset(IFunction function, double start, double end, int samples) {
        return DatasetUtilities.sampleFunction2D(new Function2DAdapter(function), start, end, samples, "functionData");
    }
}
