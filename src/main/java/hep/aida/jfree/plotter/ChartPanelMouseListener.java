package hep.aida.jfree.plotter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;

public class ChartPanelMouseListener implements MouseListener {

    ChartPanel chartPanel;
    PlotterRegion plotterRegion;
    List<ActionListener> actionListeners = new ArrayList<ActionListener>();
    
    String REGION_SELECTED = "regionSelected";
    
    public ChartPanelMouseListener(ChartPanel chartPanel, PlotterRegion plotterRegion) {
        this.plotterRegion = plotterRegion;
        this.chartPanel = chartPanel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("ChartPanelMouseListener.mouseClicked");
        for (ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(new ActionEvent(chartPanel, 1, REGION_SELECTED));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {        
    }

    @Override
    public void mouseEntered(MouseEvent e) {        
    }

    @Override
    public void mouseExited(MouseEvent e) {        
    }
    
    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }
}