package com.WebPing;

//https://stackoverflow.com/questions/12694490/how-do-i-use-aggregation-operators-in-a-match-in-mongodb-for-example-year-or
//https://www.compose.com/articles/aggregations-in-mongodb-by-example/
//http://www.joda.org/joda-time/apidocs/org/joda/time/DateTime.html
//https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Path;
import com.itextpdf.text.pdf.parser.clipper.Paths;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Aggregates.*;

public class PingService {


	private final MongoDatabase db;
    @SuppressWarnings("rawtypes")
	private final MongoCollection<org.bson.Document> pingCollection;
    //private final MongoCollection<org.bson.Document> deviceCollection;
    
    /* Mongo Collection-> Mongo Document -> Mongo row */
 
    public PingService(MongoDatabase mongo) {
    	this.db = mongo;
        this.pingCollection = mongo.getCollection("pingsv2");
        //this.deviceCollection = mongo.getCollection("devices");
	}
    
    //Provide a list of all ping attempts
	public List<Ping> findAll() {
        List<Ping> pings = new ArrayList<>();

        MongoCursor<org.bson.Document> cursor = pingCollection.find().iterator();
      
        while (cursor.hasNext()) {
        	org.bson.Document dbObject = cursor.next();
            pings.add(new Ping((org.bson.Document) dbObject));
            
        }
        System.out.print("find all: "+pings);
      
    
        
        return pings;
    }
	
	 //Create new ping from ping class object
    public void createNewPing(Ping ping) {
    	pingCollection.insertOne(new org.bson.Document("id", ping.getId())
    			.append("devId", ping.getDevId())
    			.append("ip", ping.getIp())
    			.append("lastping", ping.getLastPingAttempt())
    			.append("pingable", ping.getPingable()));
		System.out.println("create new ping: "+ ping);
		
	}
	
    //Add new ping to database from a string
    public void addNewPing(String pingAttempt) {
        Ping ping = new Gson().fromJson(pingAttempt, Ping.class);
        pingCollection.insertOne(new org.bson.Document("id", ping.getId())
        		.append("ip", ping.getIp())
        		.append("devId", ping.getDevId())
        		.append("lastping", ping.getLastPingAttempt())
        		.append("pingable", ping.getPingable()));
    }
    
    //provide a list of pings given an device ID for that device
    //This would allow for adding new ping's to devices over time the connectivity to this device, for charting.
    public List<Ping> find(String devIdLookup) {
    	List<Ping> pings = new ArrayList<>();
    	//MongoCursor<org.bson.Document> cursor = pingCollection.find(new org.bson.Document("devId", devId)).iterator();
    	//MongoCursor<org.bson.Document> cursor = pingCollection.aggregate(new org.bson.Document("devId", devId)).iterator();
    	
    	AggregateIterable<org.bson.Document> iterable = pingCollection.aggregate(Arrays.asList(match(com.mongodb.client.model.Filters.eq("devId", devIdLookup))));
    	//System.out.println(pingCollection.aggregate(Arrays.asList(match(com.mongodb.client.model.Filters.eq("devId", devIdLookup)))).first());
    			
    	iterable.forEach((Block<org.bson.Document>) document -> {
    	    //System.out.println(document);
    	    pings.add(new Ping( document));
    	});
    	/*{ $match: { "devId": devIdLookup }}
    			
   
    			]);
    			   
    			
    		   
    			
    	
    	while (cursor.hasNext()) {
        	org.bson.Document dbObject = cursor.next();
            pings.add(new Ping((org.bson.Document) dbObject));
        }
        System.out.println("ping find one:"+pings.get(0));
        
        */
        return  pings;
        
        
    }
 
	public void update(Device device ) {
        //Device device = new Gson().fromJson(body, Todo.class);
        //System.out.print("Update fun:"+ todo.getTitle());
        
		pingCollection.updateOne(new BasicDBObject("_id", new ObjectId()),
        		new org.bson.Document("$set", new org.bson.Document("pingable", device.getPingable())));
        /*collection.updateOne(new BasicDBObject("_title", new ObjectId(todo.getTitle())),
        		new Document("$set", new Document("done", todo.isDone())));*/
       
        
    }
    
    public void remove(String pingId) {
    	pingCollection.deleteOne(new org.bson.Document("_id", new ObjectId(pingId)));
        	
    }

    
   

	
	
	// Outputs array object of two arrays, Device Ping success% v.s. Dates.
    //Provide array of Day+Times the device has been pinged
	//Provide array of 0 or 100% if pingable or not.
  //TODO: create for device specific id, currently testing for static device id
	public List<Object> graphArray(String pingId) {
		
		
		
		List<Integer> outArray = new ArrayList<>();
		List<String> dateArrayString = new ArrayList<>();
		List<Date> dateArray = new ArrayList<>();
		List<Ping> pingArray = new ArrayList<>();
		List<Object> objectArray = new ArrayList<>();
		String devIdLookup = pingId; //"5aa970cc512086504cc530a3";
		DateTimeFormatter mdy = DateTimeFormat.forPattern("M/d/yyyy");
		
		
		
 
		AggregateIterable<org.bson.Document> iterable = pingCollection.aggregate(Arrays.asList(match(com.mongodb.client.model.Filters.eq("devId", devIdLookup))));
    	
		iterable.forEach((Block<org.bson.Document>) document -> {
    	    //System.out.println(document);
    	    pingArray.add(new Ping( document));
    	});
		

		System.out.println(pingArray.size());
		
		
		List<Integer> truePing = new ArrayList<>();
		List<Integer> totalPing = new ArrayList<>();
		List<Long> percentPing = new ArrayList<>();
		int totalP = 0;
		int trueP = 0;
		//DateTime now = new DateTime();
		DateTime prevDays = null;
		DateTime midnight = null;
		//System.out.println("testing: ");
		totalPing.add(0,0);
		truePing.add(0,0);
		midnight = new DateTime().minusMillis(new DateTime().getMillisOfDay());
		
		dateArrayString.add(0,new DateTime().toString(mdy));
		dateArray.add(0,new DateTime().toDate());
		
		
		//Calculate Percent Ping of current day
		for(int k=0;k<pingArray.size();k++){
			if( pingArray.get(k).getLastPingAttemptDateTime().isAfter(midnight)){
				totalP++;
				if( pingArray.get(k).getPingable() ){
				trueP++;
				}
			}
	
		}
	
		if(totalP >0){percentPing.add(0,Math.round((trueP/(double)totalP)*100)); }
		else{percentPing.add(0,(long)-1.0);}
		
		
		totalP = 0;
		trueP = 0;
		
		for(int j=1;j<=30;j++){
			
			
			prevDays = midnight.minusDays(j);
			//System.out.println(prevDays);
			dateArrayString.add(j,prevDays.toString(mdy));
			dateArray.add(j,prevDays.toDate());
			
			
			for(int i=0;i<pingArray.size();i++){
				
				if( pingArray.get(i).getLastPingAttemptDateTime().isAfter(prevDays)
						&& pingArray.get(i).getLastPingAttemptDateTime().isBefore(prevDays.plusDays(1))){
					totalP++;
					
					if( pingArray.get(i).getPingable() ){
					trueP++;
					}
				}
				//totalPing.add(j,totalP);
				//truePing.add(j,trueP);
				
			
			}
			if(totalP >0){percentPing.add(j,Math.round((trueP/(double)totalP)*100)); }
			else{percentPing.add(j,(long)-1.0);}
			
			totalP = 0;
			trueP = 0;
		
		}
		System.out.println("PPSize:"+percentPing.size());
		System.out.println("dateArraySize:"+dateArray.size());
		//System.out.println("total: "+totalPing.get(1));
		ChartExporter chartOutput = new ChartExporter();
		chartOutput.ChartExporterCustom(dateArray, percentPing, "Connectivity % For Device 402", "Date", "% Connected", "test_Device_output");
			
				
				
			 
			 
		objectArray.add(percentPing);
    	objectArray.add(dateArrayString);
    	System.out.println(objectArray);
		return objectArray;
	}
}
