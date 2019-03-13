package com.WebPing;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Ping {
	
	
	private String id;
	private String devId;
	private Date lastPingAttempt = null;
	private boolean pingable;
	private InetAddress inet = null;
	


// A Document in MongoDB is a Row
	
//Create Ping object from document dbObject
public Ping(Document dbObject) {
	this.id = ((ObjectId) dbObject.get("_id")).toString();
	this.devId = dbObject.getString("devId");
	
	try { //Try to add ip address from String stored in Database.
		this.inet = InetAddress.getByName(dbObject.getString("ip"));
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	this.pingable =  dbObject.getBoolean("pingable");
		
	
	this.lastPingAttempt = dbObject.getDate("lastping");
	
			
}

//build Ping object with constructor from varables directly
public Ping(String devId,InetAddress inet){
	this.id = "";
	this.devId = devId;
	
	this.inet = inet;
	try {
		this.pingable = inet.isReachable(25) ? true : false;   //TODO: convert to asynchron ping script
		lastPingAttempt = new Date();
	} catch (IOException e) {
		lastPingAttempt = new Date();
		e.printStackTrace();
	}
}


public String getId(){
	return id;
}

public String getDevId(){
	return devId;
}




public Date getLastPingAttempt(){
	
	return lastPingAttempt;
}

public DateTime getLastPingAttemptDateTime(){
	
	return new DateTime(lastPingAttempt);
}

public String getDateTimeFormated(){
	
	
	DateTimeFormatter mdyhms = DateTimeFormat.forPattern("M/d/yyyy k:m:s");
	DateTime t = new DateTime(lastPingAttempt);
	String str = t.toString(mdyhms);
	return str;
}

public boolean wasPingable(DateTime t){
	boolean result = false;
	
	
	
	
	
	return result;
	
}




public boolean getPingable(){	
	
	return pingable;
}

public String getIp(){
	return inet.getHostAddress();
}

}
