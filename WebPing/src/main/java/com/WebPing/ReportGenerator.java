package com.WebPing;


//https://developers.itextpdf.com/examples/image-examples-itext5
//https://www.concretepage.com/itext/add-image-in-pdf-using-itext-in-java
//https://stackoverflow.com/questions/30009847/java-mongodb-3-0-driver-query-distinct-without-filter



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class ReportGenerator {
	
	//Setup Database
	private  MongoDatabase db;
    private  MongoCollection<org.bson.Document> collection;
    private  MongoCollection<org.bson.Document> pingCollection;
    
 
    public void DBService(MongoDatabase mongo) {
    	
        
	}
	
      //provide a list of all the agency's - 
  	
  		public List<Device> findAllAgency(MongoDatabase mongo) {
  			this.db = mongo;
  			this.collection = mongo.getCollection("devices");
  	        this.pingCollection = mongo.getCollection("pingsv2");
  	        
  	        
  	        List<Device> allDeviceList = new ArrayList<>();
	        List<String> uniqueAgencyList = new ArrayList<>();
	        List<Device> agencySortedDeviceList = new ArrayList<>();
	        int connectedDevices = 0;
  	        
  	        
  	        MongoCursor<String> agencyCursor = collection.distinct("agency", String.class).iterator();
  	        while (agencyCursor.hasNext()) {
	        	String dbObject = agencyCursor.next();
	        	uniqueAgencyList.add(dbObject);
	         }
  	        uniqueAgencyList.sort(String::compareToIgnoreCase);
  	        System.out.println(uniqueAgencyList);
  	      
  	      
	  	    MongoCursor<org.bson.Document> deviceCursor = collection.find().iterator();
		        while (deviceCursor.hasNext()) {
		        	org.bson.Document dbObject = deviceCursor.next();
		        	allDeviceList.add(new Device((org.bson.Document) dbObject));
		        	
		         }
		        System.out.println(allDeviceList.size());
		        
		   //Count all the connected devices in the all device list.    
	       for(int i=0; i<allDeviceList.size();i++){
	    	   if(allDeviceList.get(i).getPingable()){
	    		   connectedDevices++;
	    	   }
	       }
	       System.out.println("Connected:"+connectedDevices);
		         
  	    
	        for(int j = 0;j<uniqueAgencyList.size();j++)
	  	      { for(int i = 0;i<allDeviceList.size();i++)
	  	        {
		  		  if(uniqueAgencyList.get(j) == allDeviceList.get(i).getAgency() ){
		  			agencySortedDeviceList.add(new Device( allDeviceList.get(i) ) );
		  		  }
			    	  
			      }
  	      
	  	      }
  	        return agencySortedDeviceList;
  	    }
	
	   //Create PDF report with table of devices and graphs of connectivity of devices overtime
    public void createNewReport(List<Device> devList) throws Throwable {
    	String pdfFileName = "C:/Users/mredmond/workspace/WebPing/src/main/resources/public/pdf/report.pdf";
    	com.itextpdf.text.Document document = new com.itextpdf.text.Document();
	    try {
			PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    document.open();
	    Font font = FontFactory.getFont(FontFactory.COURIER, 24, BaseColor.BLACK);
	    
	    //Document Header/ Title
	    Chunk chunk = new Chunk("Network Status Report", font);
	 
	    
	    LineSeparator ls = new LineSeparator();
	    document.add(new Chunk(ls));
	    
	   
	    
	  
	    document.add( Chunk.NEWLINE );
	    document.add( Chunk.NEWLINE );
	    document.add( Chunk.NEWLINE );
	    document.add( Chunk.NEWLINE );
	    document.add( Chunk.NEWLINE );
	    document.add( Chunk.NEWLINE );
	    
	    
	    //Document Table of devices
	    PdfPTable table = new PdfPTable(4);
	    table.setHorizontalAlignment(Element.ALIGN_CENTER);
	    addTableHeader(table);
	    for(int i=0; i<devList.size(); i++){
	    	addRows(table,devList.get(i));
	    }
	    
	  //Grab image and Setup Image for placement
	    Image img = Image.getInstance("./GraphOutput/test_Device_output.png");
	    img.scaleToFit(PageSize.A4.getWidth()-125, 400);
	    img.setAlignment(Element.ALIGN_CENTER);
	     
	    
	     
	    document.add(chunk);
	 // add a couple of blank lines
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        
        
	    document.add(table);
	    document.add(img);
	    document.close();
    }
    
    private void addTableHeader(PdfPTable table) {
        Stream.of("Agency", "Device Type", "IP Address", "Uptime %")
          .forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }
    
    
 
    
    private void addRows(PdfPTable table, Device dev ) {
        table.addCell(dev.getAgency());
        table.addCell(dev.getTitle());
        table.addCell(dev.getIp());
        table.addCell("100%");
    }

}
