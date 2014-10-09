package hep.aida.jfree.plotter.style.util;

import hep.aida.IAxisStyle;
import hep.aida.IBaseStyle;
import hep.aida.IBorderStyle;
import hep.aida.IBoxStyle;
import hep.aida.IBrushStyle;
import hep.aida.IDataStyle;
import hep.aida.IFillStyle;
import hep.aida.IGridStyle;
import hep.aida.ILegendBoxStyle;
import hep.aida.ILineStyle;
import hep.aida.IMarkerStyle;
import hep.aida.IPlotterStyle;
import hep.aida.IStatisticsBoxStyle;
import hep.aida.ITextStyle;
import hep.aida.ITitleStyle;

import java.io.PrintStream;

/**
 * 
 * This class dumps all information from an IPlotterStyle to a PrintStream.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
// TODO: add print out of these constants
//       http://aida.freehep.org/doc/v3.3.0/api/constant-values.html
// TODO: conversion of raw int values to constants where applicable (units, placement, etc.)
public class PlotterStylePrinter {
    
    private IPlotterStyle style;
    private PrintStream ps = System.out;
    private int indentLevel = 0;
    private static final String indentString = "    ";
    
    public PlotterStylePrinter(IPlotterStyle style, PrintStream ps) {
        if (style == null) {
            throw new IllegalArgumentException("styles points to null.");
        }
        this.style = style;
        if (ps != null) {
            this.ps = ps;
        }
    }
    
    void indent() {
        indentLevel += 1;
    }
    
    void deindent() {
        indentLevel -= 1;
    }
    
    void resetIndent() {
        indentLevel = 0;
    }
    
    /**
     * Top-level print method.
     */
    public void print() {  
        
        ps.println("---- Printing IPlotterStyle: " + style + " ----");
        
        printBaseStyle(style, true);
        
        printDataStyle(style.dataStyle());
        printBoxStyle(style.dataBoxStyle());
        printGridStyle(style.gridStyle());
        printLegendBoxStyle(style.legendBoxStyle());
        printBoxStyle(style.regionBoxStyle());
        printStatisticsBoxStyle(style.statisticsBoxStyle());
        printTitleStyle(style.titleStyle());      
        printAxisStyle(style.xAxisStyle());
        printAxisStyle(style.yAxisStyle());
        printAxisStyle(style.zAxisStyle());        
        
        ps.println("---- DONE printing IPlotterStyle: " + style + " ----");
    }
    
    void printAxisStyle(IAxisStyle axisStyle) {
        printField(axisStyle.name(), axisStyle);
        
        indent();
        
        printBaseStyle(axisStyle, true);
        
        printField("label", axisStyle.label());
        printField("scaling", axisStyle.scaling());
        
        printTextStyle(axisStyle.labelStyle());
        printLineStyle(axisStyle.lineStyle());
        printTextStyle(axisStyle.tickLabelStyle());
        
        deindent();        
    }
    
    void printTitleStyle(ITitleStyle titleStyle) {
        printField(titleStyle.name(), titleStyle);
        
        indent();
        
        printBoxStyle(titleStyle.boxStyle());
        printTextStyle(titleStyle.textStyle());
        
        deindent();
    }
    
    void printStatisticsBoxStyle(IStatisticsBoxStyle statStyle) {
        printField(statStyle.name(), statStyle);
        
        indent();
        
        printBaseStyle(statStyle);
        printBoxStyle(statStyle.boxStyle());
        printTextStyle(statStyle.textStyle());
        
        printField("visibleStatistics", statStyle.visibleStatistics());        
        
        deindent();
    }
        
    void printLegendBoxStyle(ILegendBoxStyle legendStyle) {
        printField(legendStyle.name(), legendStyle);
        
        indent();
        
        printBaseStyle(legendStyle);
        printBoxStyle(legendStyle.boxStyle());
        printTextStyle(legendStyle.textStyle());
        
        deindent();
    }
    
    void printTextStyle(ITextStyle textStyle) {
        printField(textStyle.name(), textStyle);
        
        indent();
        
        printBaseStyle(textStyle);
        printBrushStyle(textStyle);
        
        printFieldArray("availableFonts", textStyle.availableFonts());
        printField("font", textStyle.font());
        printField("fontSize", textStyle.fontSize());
        printField("isBold", textStyle.isBold());
        printField("isItalic", textStyle.isItalic());
        printField("isUnderlined", textStyle.isUnderlined());
        
        deindent();        
    }
    
    void printGridStyle(IGridStyle gridStyle) {
        printField(gridStyle.name(), gridStyle);
        
        indent();
        
        printLineStyle(gridStyle, false, false);
        
        printField("cellSize", gridStyle.cellSize());
        printField("units", gridStyle.units());
        
        deindent();
    }
       
    void printBoxStyle(IBoxStyle boxStyle) {
        
        printField(boxStyle.name(), boxStyle);
        
        indent();
        
        printBaseStyle(boxStyle);
        
        printField("height", boxStyle.height());
        printField("placement", boxStyle.placement());
        printField("units", boxStyle.units());
        printField("width", boxStyle.width());
        printField("x", boxStyle.x());
        printField("y", boxStyle.y());
        
        printFillStyle(boxStyle.backgroundStyle());
        printBorderStyle(boxStyle.borderStyle());
        printFillStyle(boxStyle.foregroundStyle());
        
        deindent();        
    }
    
    void printBorderStyle(IBorderStyle borderStyle) {
        
        printField(borderStyle.name(), borderStyle);
        
        indent();
        
        printLineStyle(borderStyle, false, false);
        printFieldArray("availableBorderTypes", borderStyle.availableBorderTypes());
        printField("borderType", borderStyle.borderType());
        
        deindent();
    }
      
    void printDataStyle(IDataStyle dataStyle) {
        
        printField(dataStyle.name(), dataStyle);
        
        indent();

        printBaseStyle(dataStyle);
        
        printField("isShowInLegendBox", dataStyle.isShownInLegendBox());
        printField("isShownInStatisticsBox", dataStyle.isShownInStatisticsBox());
        printField("isVisible", dataStyle.isVisible());
                        
        printLineStyle(dataStyle.errorBarStyle());
        printFillStyle(dataStyle.fillStyle());
        printMarkerStyle(dataStyle.markerStyle());                
        printLineStyle(dataStyle.outlineStyle());
                       
        deindent();
    }
    
    void printArray(String[] strings) {
        int lastIndex = strings.length - 1;
        for (int i=0; i<strings.length; i++) {
            ps.print(strings[i]);
            if (i != lastIndex)
                ps.print(", ");
        }
    }
    
    void printMarkerStyle(IMarkerStyle markerStyle) {
        
        printField(markerStyle.name(), markerStyle);
        
        indent();
        
        printBaseStyle(markerStyle);
        printBrushStyle(markerStyle);
        
        printFieldArray("availableShapes", markerStyle.availableShapes());
        printField("shape", markerStyle.shape());
        printField("size", markerStyle.size());
        
        deindent();        
    }

    void printLineStyle(ILineStyle lineStyle) {
        printLineStyle(lineStyle, true, true);
    }
    
    void printLineStyle(ILineStyle lineStyle, boolean indent, boolean header) {        
        
        if (header)
            printField(lineStyle.name(), lineStyle);
        
        if (indent)
            indent();
        
        printBaseStyle(lineStyle);
        
        printFieldArray("availableLineTypes", lineStyle.availableLineTypes());
        printField("lineType", lineStyle.lineType());
        printField("thickness", lineStyle.thickness());          
        
        if (indent)
            deindent();
    }
    
    void printFillStyle(IFillStyle fillStyle) {
        printField(fillStyle.name(), fillStyle);
        
        indent();
        
        printBaseStyle(fillStyle);
        printBrushStyle(fillStyle);
        
        printFieldArray("availablePatterns", fillStyle.availablePatterns());
        printField("pattern", fillStyle.pattern());
        
        deindent();        
    }
    
    void printBrushStyle(IBrushStyle brushStyle) {
        printFieldArray("availableColors", brushStyle.availableColors());
        printField("color", brushStyle.color());
        printField("opacity", brushStyle.opacity());
    }
    
    void printBaseStyle(IBaseStyle baseStyle) {
        printBaseStyle(baseStyle, false);
    }
    
    void printBaseStyle(IBaseStyle baseStyle, boolean detailedParameters) {
        printField("name", baseStyle.name());
        printField("isVisible", baseStyle.isVisible());
        printFieldArray("availableParameters", baseStyle.availableParameters());
        
        if (detailedParameters) {
            indent();
            for (String parameter : baseStyle.availableParameters()) {
                printFieldArray(parameter + " options", baseStyle.availableParameterOptions(parameter));
            }
            deindent();
        }
    }
        
    void printIndent() {
        for (int i=0; i<indentLevel; i++) {
            ps.print(indentString);
        }
    }    
    
    void printField(String field, Object value) {
        printIndent();
        ps.println(field + ": " + value);
    }       
    
    void printFieldArray(String field, String[] values) {
        printIndent();
        ps.print(field + ": ");
        printArray(values);
        ps.println();
    }    
}