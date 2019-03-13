package com.Archive;


import com.WebPing.JsonTransformer;
import com.WebPing.StandardResponse;
import com.WebPing.StatusResponse;

//https://jaxenter.com/crud-rest-apis-spark-framework-136053.html
//https://github.com/shekhargulati/todoapp-spark/tree/master/src/main


import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
 
import java.util.HashMap;
 
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

public class TodoResource {
	
	 private static final String API_CONTEXT = "";
	 
	 private final TodoService todoService;
	 
	 public TodoResource(TodoService todoService) {
	      this.todoService = todoService;
	      setupEndpoints();
	 }
	 
	 private void setupEndpoints() {
	        post(API_CONTEXT + "/todos", "application/json", (request, response) -> {
	        	System.out.println(request.body());
	        	System.out.println(request.contentType()); // What type of data am I sending?
	            System.out.println(request.params()); // What are the params sent?
	            System.out.println(request.raw()); // What's the raw data sent?
	            todoService.createNewTodo(request.body());
	            response.status(201);
	            return response;
	        }, new JsonTransformer());
	        
	        post("/todos", (request, response) -> {
	            response.type("application/json");
	            System.out.println("Out-Post:" + request.body().toString());
	            Todo todo = new Gson().fromJson(request.body(), Todo.class);
	            todoService.createNewTodo(todo);
	         
	            return new Gson()
	              .toJson(new StandardResponse(StatusResponse.SUCCESS));
	        });
	 
	        get("/todos/:id", (request, response) -> {
	            response.type("application/json");
	            return new Gson().toJson(
	              new StandardResponse(StatusResponse.SUCCESS,new Gson()
	                .toJsonTree(todoService.find(request.params(":id")))));
	        });
	 
	        get(API_CONTEXT + "/todos", "application/json", (request, response)
	 
	                -> todoService.findAll(), new JsonTransformer());
	        
	        delete("/todos/:id", (request, response) -> {
	            response.type("application/json");
	            todoService.remove(request.params(":id"));
	            return new Gson().toJson(
	              new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
	        });
	 
	        put(API_CONTEXT + "/todos/:id",  (request, response) -> {
	        		response.type("application/json");
	        		Todo todo = new Gson().fromJson(request.body(), Todo.class);
	        		System.out.println("Out-put:" + request.body().toString());
	                todoService.update(todo);
	                
	                return new Gson().toJson(
	      	              new StandardResponse(StatusResponse.SUCCESS, "Pinged Again.."));
	        });
	                //request.body()), new JsonTransformer());
	    }

	

	
	 
	 

}
