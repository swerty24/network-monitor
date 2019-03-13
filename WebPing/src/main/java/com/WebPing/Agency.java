package com.WebPing;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

public class Agency {
	
	private String networkName;
	
	private int numAgencys;
	private int numDevices;
	private int numConnectedDevices;
	private int numDisconnectedDevices = numDevices - numConnectedDevices;
	
	
	private List<Device> allDevices;
	private List<String> allDeviceTypes;
	
	
	
	
	
	
	
	
	
	

}
