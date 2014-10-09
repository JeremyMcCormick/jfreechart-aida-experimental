package hep.aida.jfree.plot.listener;

import hep.aida.IDataPointSet;
import hep.aida.jfree.dataset.DataPointSetAdapter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.general.Dataset;


public class DataPointSetListener extends PlotListener<IDataPointSet> {

    DataPointSetListener(IDataPointSet object, JFreeChart chart, Dataset dataset) {
        super(object, chart, dataset);
    }
    
    public void update() {
        double maxY = ((DataPointSetAdapter)dataset).getMaxY();
        ValueAxis yAxis = chart.getXYPlot().getRangeAxis();
        if (maxY > yAxis.getAutoRangeMinimumSize()) {
            yAxis.setAutoRangeMinimumSize(maxY);
        }
        ValueAxis xAxis = chart.getXYPlot().getDomainAxis();
        double maxX = ((DataPointSetAdapter)dataset).getItemCount(DataPointSetAdapter.VALUES);
        if (maxX > xAxis.getAutoRangeMinimumSize())
            xAxis.setAutoRangeMinimumSize(maxX);
    }
}
