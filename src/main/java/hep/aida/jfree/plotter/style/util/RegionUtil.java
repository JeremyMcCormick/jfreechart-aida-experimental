package hep.aida.jfree.plotter.style.util;

import hep.aida.IBoxStyle;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.chart.JFreeChart;

public final class RegionUtil {

    /**
     * Do not allow class instantiation.
     */
    private RegionUtil() {
    }

    /**
     * Apply region style to the chart.
     * 
     * @param chart the chart
     * @param regionStyle the AIDA style
     */
    public static void applyRegionStyle(JFreeChart chart, IBoxStyle regionStyle) {

        if (regionStyle.isVisible()) {

            // Set background style.
            Color backgroundColor = ColorUtil.toColor(regionStyle.backgroundStyle(), Color.white);
            chart.setBackgroundPaint(backgroundColor);

            // Set border style.
            if (regionStyle.borderStyle().isVisible()) {

                // Set chart border to visible.
                chart.setBorderVisible(true);

                // Set the border color.
                chart.setBorderVisible(true);
                Color borderColor = ColorUtil.toColor(regionStyle.borderStyle(), null);
                if (borderColor != null) {
                    chart.setBorderPaint(borderColor);
                }

                // Set the border stroke.
                Stroke stroke = StrokeUtil.toStroke(regionStyle.borderStyle());
                chart.setBorderStroke(stroke);
            } else {
                // Border set to invisible.
                chart.setBorderVisible(false);
            }
        } else {
            // Default to white background and do not apply other styles.
            chart.setBackgroundPaint(Color.white);
        }

        // FIXME: None of these are currently handled. Some would require direct access to the ChartPanel.
        // state.getPlotterStyle().regionBoxStyle().foregroundStyle();
        // state.getPlotterStyle().regionBoxStyle().height();
        // state.getPlotterStyle().regionBoxStyle().width();
        // state.getPlotterStyle().regionBoxStyle().isVisible();
        // state.getPlotterStyle().regionBoxStyle().placement();
        // state.getPlotterStyle().regionBoxStyle().units();
        // state.getPlotterStyle().regionBoxStyle().x();
        // state.getPlotterStyle().regionBoxStyle().y();
    }
}
