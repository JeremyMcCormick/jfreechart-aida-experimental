package hep.aida.jfree.converter;

import hep.aida.IPlotterStyle;
import hep.aida.IProfile2D;
import hep.aida.jfree.dataset.Profile2DAdapter;

import org.jfree.chart.JFreeChart;

final class Profile2DConverter implements Converter<IProfile2D> {

    @Override
    public Class<IProfile2D> convertsType() {
        return IProfile2D.class;
    }

    public JFreeChart convert(JFreeChart chart, IProfile2D p2D, IPlotterStyle style) {
        return new Histogram2DConverter().convert(chart, new Profile2DAdapter(p2D), style);
    }
}
