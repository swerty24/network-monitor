package com.WebPing;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.bson.types.ObjectId;

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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.parser.Path;
import com.itextpdf.text.pdf.parser.clipper.Paths;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

	public class DeviceService {

		private final MongoDatabase db;
	    private final MongoCollection<org.bson.Document> collection;
	    private final MongoCollection<org.bson.Document> pingCollection;
	    private List<Double> deviceStatistics = new ArrayList<Double>();
	    
	 
	    public DeviceService(MongoDatabase mongo) {
	    	this.db = mongo;
	        this.collection = mongo.getCollection("devices");
	        this.pingCollection = mongo.getCollection("pingsv2");
		}
	    
	    //Provide a list of all the devices
		public List<Device> findAll() {
			
	        List<Device> devices = new ArrayList<>();
	        ReportGenerator rp = new ReportGenerator();
	        rp.findAllAgency(db);
	        //List<Device> agencys = new ArrayList<>();
	        //List diAgency= collection.distinct( "agency", null);
	        //Iterable<String> diAgency= collection.distinct( "agency");
	        MongoCursor<org.bson.Document> cursor = collection.find().iterator();
	        //FindIterable<Document> dbObjects = collection.find().iterator();
	        while (cursor.hasNext()) {
	        	org.bson.Document dbObject = cursor.next();
	            devices.add(new Device((org.bson.Document) dbObject));
	        }
	        
	       // List<Device> agencys = diAgency.forEach(null)
	        
	       
	        try {
				rp.createNewReport(devices);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        return devices;
	    }
		
		
		public List<Double> deviceStats(){
			
			List<Device> devices = new ArrayList<>();
			deviceStatistics.add(0,(double) 0);
			deviceStatistics.add(1,(double)0);
			deviceStatistics.add(2,(double)0);
			deviceStatistics.add(3,(double)0);
			
			
			MongoCursor<org.bson.Document> cursor = collection.find().iterator();
			 while (cursor.hasNext()) {
		        	org.bson.Document dbObject = cursor.next();
		            devices.add(new Device((org.bson.Document) dbObject));
		        }
			
			
			   double j = 0;
					   j = devices.size();
		        System.out.print("devs:"+ j);
		        deviceStatistics.set(0,(double)j);
		        //Count all the connected devices in the all device list. 
		        int disconnectedDevices=0;
			       for(int i=0; i<devices.size();i++){
			    	   if(!devices.get(i).getPingable()){
			    		   disconnectedDevices++;
			    	   }
			       }
			       
			       
			       
			       double k = 0;
			       k = (disconnectedDevices/j);
			       deviceStatistics.set(1,k);
			       System.out.print(" conn:"+ k);
			       
			       
			       double l = 0;
			       l = (j-disconnectedDevices);
			       deviceStatistics.set(2,l);
			       
			       
			       
			       return deviceStatistics;
		}
		
		
		public void pingAllDevices(){
			    
		    	List<Device> devices = new ArrayList<>();
		    	MongoCursor<org.bson.Document> cursor = collection.find().iterator();
		        
		        while (cursor.hasNext()) {
		        	org.bson.Document dbObject = cursor.next();
		            devices.add(new Device((org.bson.Document) dbObject));
		        }
		        
		        for(int i=0;i<devices.size();i++){
		        	Ping ping = devices.get(i).getPingClass();
		        	
		        	pingCollection.insertOne(new org.bson.Document("id", ping.getId())
		        			.append("devId", ping.getDevId())
		        			.append("ip", ping.getIp())
		        			.append("lastping", ping.getLastPingAttempt())
		        			.append("pingable", ping.getPingable()));
					//System.out.println("create new ping: "+ ping);
					
					
		        	//System.out.println(devices.get(i).getPingClass());
		        }
		        
		       
		        
		    	
	    }
	
		//provide a list of all the agency's - NOT BEING USED. Done in app.js
		@SuppressWarnings("unchecked")
		public List<Device> findAllAgency() {
	        List<Device> agencys = new ArrayList<>();
	        agencys =  (List<Device>) collection.distinct( "agency", Device.class );
	        
	        return agencys;
	    }
	 
	    
	    //Add new device to database from a string
	    public void createNewDevice(String device2) {
	        Device device = new Gson().fromJson(device2, Device.class);
	        collection.insertOne(new org.bson.Document("title", device.getTitle()).append("agency",  device.getAgency()).append("ip", device.getIp()).append("done", device.isDone()).append("createdOn", new Date()));
	        System.out.println("create new device: "+ device2);
	    }
	    
	 
	    public Device find(String id) {
	    	//Document out = (Document) collection.find(new org.bson.Document("_id", new ObjectId(id)));
	    	Device out = null;
	    	MongoCursor<org.bson.Document> cursor = collection.find(new org.bson.Document("_id", new ObjectId(id))).iterator();
	        while (cursor.hasNext()) {
	        	org.bson.Document dbObject = cursor.next();
	            out = new Device((org.bson.Document) dbObject);
	        }
	        return  out;
	    }
	 
	    
	    //update device by Device class
		public void update(Device device ) {
	        //Device device = new Gson().fromJson(body, Todo.class);
	        //System.out.print("Update fun:"+ todo.getTitle());
	        
	        collection.updateOne(new BasicDBObject("_id", new ObjectId()),
	        		new org.bson.Document("$set", new org.bson.Document("pingable", device.getPingable())));
	        /*collection.updateOne(new BasicDBObject("_title", new ObjectId(todo.getTitle())),
	        		new Document("$set", new Document("done", todo.isDone())));*/
	       
	        
	    }
	    //remove device listing by string ID
	    public void remove(String deviceId) {
	        collection.deleteOne(new org.bson.Document("_id", new ObjectId(deviceId)));
	        	
	    }
	    
	    //Create new device document in database from device class
	    public void createNewDevice(Device device) {
			collection.insertOne(new org.bson.Document("title", device.getTitle())
					.append("agency",  device.getAgency())
					.append("ip", device.getIp())
					.append("done", device.isDone())
					.append("createdOn", new Date()));
			System.out.println("create new device: "+ device);
			
		}
		
	
	    
	   
	    
	
	}
