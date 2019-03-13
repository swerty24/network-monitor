package com.Archive;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class Todo {
	
	private String id;
	private String title;
	private boolean done;
	private Date createdOn = new Date();
	private InetAddress inet = null;
	private boolean pingable;
	

	public Todo(Document dbObject) {
		this.id = ((ObjectId) dbObject.get("_id")).toString();
		this.title = dbObject.getString("title");
		try { //Try to add ip address from String stored in Database.
			this.inet = InetAddress.getByName(dbObject.getString("ip"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.done = dbObject.getBoolean("done");
		this.createdOn = dbObject.getDate("createdOn");
		try {
			this.pingable = inet.isReachable(100) ? true : false;
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}


	public String getTitle(){
		return title;
	}
	
	public boolean isDone(){
		return done;
	}
	
	public String getIp(){
		return inet.getHostAddress();
	}
	
	public Date getCreatedOn(){
		return createdOn;
	}
	
	public boolean getPingable(){
		try {
			this.pingable = inet.isReachable(1000) ? true : false;
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pingable;
	}

	
	
	

}
