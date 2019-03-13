


//https://jaxenter.com/crud-rest-apis-spark-framework-136053.html
//https://github.com/shekhargulati/todoapp-spark/tree/master/src/main
//http://www.baeldung.com/spark-framework-rest-api
//https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ScheduledExecutorService.html#scheduleAtFixedRate%28java.lang.Runnable,%20long,%20long,%20java.util.concurrent.TimeUnit%29


package com.WebPing;



import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

public class DeviceResource {
	
	 private static final String API_CONTEXT = "";
	 
	 private final DeviceService deviceService;
	 private final PingService pingService;
	 
	 public DeviceResource(DeviceService deviceService, PingService pingService) {
	      this.deviceService = deviceService;
	      this.pingService = pingService;
	      setupEndpoints();
	      
	      
	      
	      
	      Runnable helloRunnable = new Runnable() {
	    	    public void run() {
	    	    	deviceService.pingAllDevices();
	    	        System.out.println("5 minute - ping all devices");
	    	    }
	    	};

	    	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	    	executor.scheduleAtFixedRate(helloRunnable, 0, 300, TimeUnit.SECONDS);
	 }
	 
	 private void setupEndpoints() {
	        post(API_CONTEXT + "/devices", "application/json", (request, response) -> {
	        	System.out.println(request.body());
	        	System.out.println(request.contentType()); // What type of data am I sending?
	            System.out.println(request.params()); // What are the params sent?
	            System.out.println(request.raw()); // What's the raw data sent?
	            deviceService.createNewDevice(request.body());
	            response.status(201);
	            return response;
	        }, new JsonTransformer());
	        
	        post(API_CONTEXT + "/devices", (request, response) -> {
	            response.type("application/json");
	            System.out.println("Out-Post:" + request.body().toString());
	            Device device = new Gson().fromJson(request.body(), Device.class);
	            deviceService.createNewDevice(device);
	         
	            return new Gson()
	              .toJson(new StandardResponse(StatusResponse.SUCCESS));
	        });
	        
	        post(API_CONTEXT + "/pings", (request, response) -> {
	            response.type("application/json");
	            System.out.println("Out-Post(pings):" + request.body().toString());
	            Ping ping = new Gson().fromJson(request.body(), Ping.class);
	            pingService.createNewPing(ping);
	         
	            return new Gson()
	              .toJson(new StandardResponse(StatusResponse.SUCCESS));
	        });
	        
	        get(API_CONTEXT + "/devices/:id", (request, response) -> {
	            response.type("application/json");
	            return new Gson().toJson(
	              new StandardResponse(StatusResponse.SUCCESS,new Gson()
	                .toJsonTree(deviceService.find(request.params(":id")))));
	        });
	        
	 
	        get(API_CONTEXT + "/devices", "application/json", (request, response)
	        		 
	                -> deviceService.findAll(), new JsonTransformer());
	        
	        get(API_CONTEXT + "/pings", "application/json", (request, response)
	        		 
	                -> pingService.findAll(), new JsonTransformer());
	        
	        get(API_CONTEXT + "/pings/:id", "application/json", (request, response)
	        		 
	                -> pingService.find(request.params(":id")), new JsonTransformer());
	        
	        
	        
	        //ping Service function to graphArray function to return array for javascript charts
    		// Provide Array of Day+Times the device has been pinged
    		//Provide array of 0 or 100% if pingable or not. use request.params(":id") for each device
	        get(API_CONTEXT + "/graph", "application/json", (request, response)
	        		
	                ->pingService.graphArray("5aa970cc512086504cc530a3"), new JsonTransformer());
	        
	        get(API_CONTEXT + "/graph/:id", "application/json", (request, response)
	        		
	                ->pingService.graphArray(request.params(":id")), new JsonTransformer());	
	        
	        get(API_CONTEXT + "/deviceStats", "application/json", (request, response)
	        		
	                ->deviceService.deviceStats(), new JsonTransformer());
	        
	      /*  get(API_CONTEXT + "/graph/:id", "application/json", (request, response)
	        		 
	                -> pingService.graphArray(request.params(":id")), new JsonTransformer());  */
	        
	      /*  get(API_CONTEXT + "/devices", "application/json", (request, response) -> {
	        		System.out.println("get-request-agency: " + request.body().toString());
	        		System.out.println("get-response-agency: " + response.body().toString());
	        
			        response.type("application/json");
		            return new Gson().toJson(
		              new StandardResponse(StatusResponse.SUCCESS,new Gson()
		                .toJsonTree(deviceService.findAllAgency())));
			        
	        });
	        */
	        
	        delete("/devices/:id", (request, response) -> {
	            response.type("application/json");
	            deviceService.remove(request.params(":id"));
	            return new Gson().toJson(
	              new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
	        });
	 
	        put(API_CONTEXT + "/devices/:id",  (request, response) -> {
	        		response.type("application/json");
	        		Device device = new Gson().fromJson(request.body(), Device.class);
	        		System.out.println("Out-put:" + request.body().toString());
	                deviceService.update(device);
	                
	                return new Gson().toJson(
	      	              new StandardResponse(StatusResponse.SUCCESS, "Pinged Again.."));
	        });
	                //request.body()), new JsonTransformer());
	    }

	

	
	 
	 

}

