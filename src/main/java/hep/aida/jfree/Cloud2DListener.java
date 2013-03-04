package hep.aida.jfree;

import hep.aida.IBaseHistogram;
import hep.aida.ICloud2D;
import hep.aida.IHistogram1D;
import hep.aida.jfree.converter.DatasetConverter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DListener extends PlotListener
{
    ICloud2D cloud;
    
    Cloud2DListener(IBaseHistogram cloud, JFreeChart chart, int[] datasetIndices)
    {
        super(cloud, chart, datasetIndices);
        if (!(hist instanceof ICloud2D)) {
            throw new IllegalArgumentException("hist is not an instance of ICloud2D");
        }
        this.cloud = (ICloud2D) cloud;
    }

    synchronized void update()
    {
        long startTime = System.nanoTime();
        XYPlot plot = (XYPlot) chart.getPlot();
        XYSeriesCollection dataset = DatasetConverter.convert(cloud);
        //for (int i=0; i<datasetIndices.length; i++) {
            //System.out.println("updating ds @ " + datasetIndices[i]);
        plot.setDataset(datasetIndices[0], dataset);
        //}
        long endTime = System.nanoTime() - startTime;
        System.out.println("updated plot " + cloud.title() + " in " + endTime/1e6 + " ms");
    }
}