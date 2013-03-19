package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;
import hep.aida.IPlotterStyle;

import org.jfree.chart.JFreeChart;

/**
 * The interface for converting AIDA plots to JFreeChart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public interface HistogramConverter<T extends IBaseHistogram> {
    Class<T> convertsType();
    JFreeChart convert(T obj, IPlotterStyle style);
}
