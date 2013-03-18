package hep.aida.jfree;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

public class XYZToolTipDemo {

    public static void main(String[] args) {
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        dataset.addSeries("Series1", new double[][] { { 2.0, 1.0, 3.0 }, { 10.0, 20.0, 30.0 }, { 10.0, 50.0, 90.0 } });
        JFreeChart chart = createFreeChart(dataset, "Chart Title", "y axis", 0.0, 100.0);
        ChartPanel chartPanel = new ChartPanel(chart);
        chart.getXYPlot().getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {

            public String generateToolTip(XYDataset dataset, int series, int item) {
                XYZDataset xyzDataset = (XYZDataset) dataset;
                double x = xyzDataset.getXValue(series, item);
                double y = xyzDataset.getYValue(series, item);
                double z = xyzDataset.getZValue(series, item);
                System.out.println("Tooltip: x =" + x + " | " + y + " | " + z);
                return ("" + x + " | " + y + " | " + z);
            }
        });
        chartPanel.setDisplayToolTips(true);
        JFrame frame = new JFrame("XYZToolTipDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static JFreeChart createFreeChart(XYZDataset dataset, String title, String yAxisNaming, double zMin, double zMax) {
        NumberAxis xAxis = new NumberAxis("X");
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        NumberAxis yAxis = new NumberAxis(yAxisNaming);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        GrayPaintScale scale = new GrayPaintScale(zMin, zMax);
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setPaintScale(scale);

        // -------------------------------------------------------------------
        // paint legend
        // -------------------------------------------------------------------
        NumberAxis numberaxis2 = new NumberAxis("scale");
        numberaxis2.setAxisLinePaint(Color.white);
        numberaxis2.setTickMarkPaint(Color.white);
        numberaxis2.setTickLabelFont(new Font("Dialog", 0, 7));
        numberaxis2.setRange(new Range(zMin, zMax));
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        JFreeChart chart = new JFreeChart(title, plot);

        PaintScaleLegend paintscalelegend = new PaintScaleLegend(scale, numberaxis2);
        paintscalelegend.setSubdivisionCount(20);
        paintscalelegend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        paintscalelegend.setAxisOffset(5D);
        paintscalelegend.setMargin(new RectangleInsets(5D, 5D, 5D, 5D));
        paintscalelegend.setFrame(new BlockBorder(Color.red));
        paintscalelegend.setPadding(new RectangleInsets(10D, 10D, 10D, 10D));
        paintscalelegend.setStripWidth(10D);
        paintscalelegend.setPosition(RectangleEdge.RIGHT);
        chart.addSubtitle(paintscalelegend);
        // -------------------------------------------------------------------

        return chart;
    }
}