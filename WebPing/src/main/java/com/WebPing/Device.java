package com.WebPing;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Device {
	
	private String id;
	private String title;
	private String agency;
	private boolean done;
	private Date createdOn = new Date();
	private Date lastPingAttempt = null;
	private String lastPingFormatted = "";
	private InetAddress inet = null;
	private boolean pingable;
	private Location location; //lat, long to be implemented later
	private String type;   //Type of device, CCTV, switch, CMS, Controller, etc.
	private Ping status; //implement with Ping class
	private double uptime;
	
	//Variables for Reviews and ratings
	//String review;
	//Float  starRating

	//mac address
	//Intersection Cross Streets - Class for intersection streets. N/S, E/W cross streets, signalized, cabinet corner, etc.
	
	// A Create a device from a Document in Mongo is a Row
	public Device(Document dbObject) {
		this.id = ((ObjectId) dbObject.get("_id")).toString();
		this.title = dbObject.getString("title");
		this.agency = dbObject.getString("agency");
		try {
			this.inet = InetAddress.getByName(dbObject.getString("ip"));
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
		this.done = dbObject.getBoolean("done");
		this.createdOn = dbObject.getDate("createdOn");
		
		this.status = new Ping(id,inet);
		this.lastPingAttempt = status.getLastPingAttempt();
		this.lastPingFormatted = status.getDateTimeFormated();
		this.pingable = status.getPingable();
		
		/*
		 try { //Try to add ip address from String stored in Database.
		
			this.inet = InetAddress.getByName(dbObject.getString("ip"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.pingable = inet.isReachable(25) ? true : false;   //TODO: convert to asynchron ping script
			lastPingAttempt = new Date();
		} catch (IOException e) {
		
			e.printStackTrace();
		} */
		
		
				
	}
	
	// A Create a device from a Device Object
		public Device(Device deviceObject) {
			this.id =  deviceObject.getId();
			this.title = deviceObject.getTitle();
			this.agency = deviceObject.getAgency();
	
			this.inet = deviceObject.getIpInet();
			
			
			this.done = deviceObject.isDone();
			this.createdOn = deviceObject.getCreatedOn();
			
			this.status = new Ping(id,inet);
			this.lastPingAttempt = status.getLastPingAttempt();
			this.lastPingFormatted = status.getDateTimeFormated();
			this.pingable = status.getPingable();
			
		}

	public String getTitle(){
		return title;
	}
	
	public String getId(){
		return id;
	}
	
	public String getAgency(){
		return agency;
	}
	
	public boolean isDone(){
		return done;
	}
	
	public String getIp(){
		return inet.getHostAddress();
	}
	
	public InetAddress getIpInet(){
		return inet;
	}
	
	public Date getCreatedOn(){
		return createdOn;
	}
	
	//for button to individually ping an ip address
	public boolean getPingable(){
		
		
		/*try {
			this.pingable = inet.isReachable(250) ? true : false;  //TODO: convert to asynchron ping script
			lastPingAttempt = new Date();
		} catch (IOException e) {
			lastPingAttempt = new Date();
			e.printStackTrace();
		}*/
		//System.out.println(status.getPingable());
		return status.getPingable();
	}
	
	public Ping getPingClass(){
		
		

	 return status;
	}
	
	
	
	public String getlocation(){
		return location.toString();
	}
	
	public double getlatitude(){
		return location.getLatitude();
	}
	
	public double getlongitude(){
		return location.getLongitude();
	}
	
	public String getType(){
		return type;
	}
	
/*	public String getDateTimeFormated(){
		
		
		DateTimeFormatter mdyhms = DateTimeFormat.forPattern("M/d/yyyy k:m:s");
		DateTime t = new DateTime(lastPingAttempt);
		String str = t.toString(mdyhms);
		return str;
	} */

	
	public boolean wasPingable(DateTime t){
		
		
		//status.getLastPingAttemptDateTime().minus(t);
		
		
		return pingable;
		
	}
	
	public double get24hrUptime(){
		
		//status.getLastPingAttemptDateTime().minusHours(1);

		 return uptime;
		}

}
