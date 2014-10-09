package hep.aida.jfree.converter;

import hep.aida.IPlotterStyle;

import org.jfree.chart.JFreeChart;

/**
 * The interface for converting AIDA objects into JFreeChart.
 * Because AIDA does not define a parent class covering all of its 
 * data types, the type argument must be completely generic.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public interface Converter<T> {
    
    /**
     * Return the type handled by this converter.
     * @return
     */
    Class<T> convertsType();
    
    /**
     * Return a chart by converting the object using the given style.
     * @param object The object to be converted.
     * @param style The style settings.
     * @return The chart.
     */
    JFreeChart convert(JFreeChart chart, T object, IPlotterStyle style);
}
