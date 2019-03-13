package com.Archive;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bson.conversions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TodoService {

	private final MongoDatabase db;
    private final MongoCollection<Document> collection;
 
    
 
    public TodoService(MongoDatabase mongo) {
    	this.db = mongo;
        this.collection = mongo.getCollection("todos");
	}

	public List<Todo> findAll() {
        List<Todo> todos = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        //FindIterable<Document> dbObjects = collection.find().iterator();
        while (cursor.hasNext()) {
            Document dbObject = cursor.next();
            todos.add(new Todo((Document) dbObject));
        }
        return todos;
    }
 
    
    //Add new todo to database as a string, NOT USED
    public void createNewTodo(String todo2) {
        Todo todo = new Gson().fromJson(todo2, Todo.class);
        collection.insertOne(new Document("title", todo.getTitle()).append("ip", todo.getIp()).append("done", todo.isDone()).append("createdOn", new Date()));
    }
 
    public Todo find(String id) {
    	Document out = (Document) collection.find(new Document("_id", new ObjectId(id)));
    	//System.out.print(out.get("_id"));
        return  null;
    }
 
    

	public void update(Todo todo ) {
        //Todo todo = new Gson().fromJson(body, Todo.class);
        //System.out.print("Update fun:"+ todo.getTitle());
        
        collection.updateOne(new BasicDBObject("_id", new ObjectId()),
        		new Document("$set", new Document("pingable", todo.getPingable())));
        /*collection.updateOne(new BasicDBObject("_title", new ObjectId(todo.getTitle())),
        		new Document("$set", new Document("done", todo.isDone())));*/
       
        
    }
    
    public void remove(String todoId) {
        collection.deleteOne(new Document("_id", new ObjectId(todoId)));
        	
    }
    
    //Create new todo document in database from todo class
	public void createNewTodo(Todo todo) {
		collection.insertOne(new Document("title", todo.getTitle()).append("ip", todo.getIp()).append("done", todo.isDone()).append("createdOn", new Date()));
		System.out.println(todo.getIp());
		
	}

    

	
}
