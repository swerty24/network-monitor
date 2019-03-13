package com.WebPing;

//https://knowm.org/open-source/xchart/xchart-example-code/
//https://knowm.org/javadocs/xchart/index.html
//https://github.com/knowm/XChart


import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.knowm.xchart.*;
/**
 * Creates a simple Chart and saves it as a PNG and JPEG image file.
 */
public class ChartExporter {
 
 
  public ChartExporter(){
	  double[] yData = new double[] { 2.0, 1.0, 0.0 };
	  XYChart chart = new XYChart(500, 400);
	    chart.setTitle("Sample Chart");
	    chart.setXAxisTitle("X");
	    chart.setXAxisTitle("Y");
	    XYSeries series = chart.addSeries("y(x)", null, yData);
	    series.setMarker(SeriesMarkers.CIRCLE);
	 
	    try {
			BitmapEncoder.saveBitmap(chart, "./GraphOutput/Sample_Chart", BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  

  
  //Create a Chart, giving X and Y double array, chart title and X,Y title axes
  public void ChartExporterCustom(List<Date> xdata, List<Long> ydata, String chartTitle, String xAxisTitle, String yAxisTitle, String filename){
	 
	  
	  List<Date> xData = xdata;
	  List<Long> yData = ydata;
	  
	   
	    //XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
	 
	  //Dimensions of Plot (x,y)
	  XYChart chart = new XYChart(1080, 400, ChartTheme.GGPlot2);
	    chart.setTitle(chartTitle);
	    chart.setXAxisTitle(xAxisTitle);
	    chart.setYAxisTitle(yAxisTitle);
	    XYSeries series = chart.addSeries("y(x)", xData, yData);
	    series.setMarker(SeriesMarkers.CIRCLE);
	 
	    try {
			BitmapEncoder.saveBitmap(chart, "./GraphOutput/"+filename, BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
}
  
  /* 
   //public static void main(String[] args) throws Exception {
  double[] yData = new double[] { 2.0, 1.0, 0.0 };
 
    // Create Chart
  
    XYChart chart = new XYChart(500, 400);
    chart.setTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setXAxisTitle("Y");
    XYSeries series = chart.addSeries("y(x)", null, yData);
    series.setMarker(SeriesMarkers.CIRCLE);
 
    BitmapEncoder.saveBitmap(chart, "./GraphOutput/Sample_Chart", BitmapFormat.PNG);
    BitmapEncoder.saveBitmap(chart, "./GraphOutput/Sample_Chart", BitmapFormat.JPG);
    BitmapEncoder.saveJPGWithQuality(chart, "./Sample_Chart_With_Quality.jpg", 0.95f);
    BitmapEncoder.saveBitmap(chart, "./GraphOutput/Sample_Chart", BitmapFormat.BMP);
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.GIF);
 
    BitmapEncoder.saveBitmapWithDPI(chart, "./GraphOutput/Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
    BitmapEncoder.saveBitmapWithDPI(chart, "./GraphOutput/Sample_Chart_300_DPI", BitmapFormat.JPG, 300);
    BitmapEncoder.saveBitmapWithDPI(chart, "./GraphOutput/Sample_Chart_300_DPI", BitmapFormat.GIF, 300);
 
    VectorGraphicsEncoder.saveVectorGraphic(chart, "./Sample_Chart", VectorGraphicsFormat.EPS);
    VectorGraphicsEncoder.saveVectorGraphic(chart, "./Sample_Chart", VectorGraphicsFormat.PDF);
    VectorGraphicsEncoder.saveVectorGraphic(chart, "./Sample_Chart", VectorGraphicsFormat.SVG);
    
    */
 
  
  
  

