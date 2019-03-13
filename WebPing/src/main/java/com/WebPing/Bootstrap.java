
// Main TODO Section
//https://www.mongodb.com/blog/post/getting-started-with-mongodb-and-java-part-i
// http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
// https://github.com/shekhargulati/todoapp-spark
//Time Series Visulization: http://square.github.io/cubism/
//Block Diagram for Highlevel Stats: https://github.com/hootsuite/grid    Not: https://isotope.metafizzy.co/
// Dashboard: https://rstudio.github.io/shinydashboard/index.html


package com.WebPing;
 
import spark.Request;
import spark.Response;
import spark.Route;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import static spark.Spark.setIpAddress;
import static spark.Spark.setPort;
import static spark.SparkBase.staticFileLocation;
 
import static spark.Spark.*;
 
public class Bootstrap {
 
	 private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
	 private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 8117;
	 
	
	
    public static void main(String[] args) throws Exception {
    	setIpAddress(IP_ADDRESS);
        setPort(PORT);
        staticFileLocation("/public");
        new DeviceResource(new DeviceService(mongo()), new PingService(mongo()));
    }
    
    //https://www.mkyong.com/mongodb/java-authentication-access-to-mongodb/
    //https://stackoverflow.com/questions/35392797/how-to-connect-to-mongodb-3-2-in-java-with-username-and-password
               
    private static MongoDatabase mongo() throws Exception {
        String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
        if (host == null) {
            MongoClient mongoClient = new MongoClient("localhost");
            return mongoClient.getDatabase("todoapp");
        }
        int port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
        String dbname = System.getenv("OPENSHIFT_APP_NAME");
        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
        MongoClient mongoClient = new MongoClient("localhost");//new ServerAddress(host, port), mongoClientOptions);
        MongoCredential credential = MongoCredential.createMongoCRCredential(username, dbname, "password".toCharArray());
        //mongoClient.setWriteConcern(WriteConcern.SAFE);
        MongoDatabase database = mongoClient.getDatabase(dbname);
        return database;
        
    }
  
}

