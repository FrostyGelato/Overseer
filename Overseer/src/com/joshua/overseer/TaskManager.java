package com.joshua.overseer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TaskManager {
	
	JSONArray taskArray = new JSONArray();
	JSONObject task = new JSONObject();
	
	Scheduler3 scheduler = new Scheduler3();
	
	String taskPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "tasks.json";
	
	public TaskManager() {
		JSONParser parser = new JSONParser();
		
		if (new File(taskPath).isFile() == true) {
			try {
	        	//parse tasks.json file and load the array data
				taskArray = (JSONArray) parser.parse(new FileReader(taskPath));
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addTask(String name, LocalTime timeRequired, LocalDateTime deadline) {
		
		task = new JSONObject();
		
		task.put("name", name);
		//colons in time makes JSON invalid
		task.put("timeRequired", timeRequired.toString());
		task.put("deadline", deadline.toString());
		
		taskArray.add(task);
		
		writeToDisk();
		
		scheduler.add(name, deadline.toLocalDate(), timeRequired);
	}
	
	public void modifyTask(String name, LocalTime timeRequired, LocalDateTime deadline) {
		
		deleteTask(name);
		
		addTask(name, timeRequired, deadline);
	}
	
	public void deleteTask(String taskName) {
		
		JSONArray newTaskArray = new JSONArray();     
		
		if (taskArray != null) {
			
		   for (int i=0; i<taskArray.size(); i++) {
			   
			   JSONObject taskObject = (JSONObject) taskArray.get(i);
			   
		        if (!(taskName.equals(taskObject.get("name")))) {
		        	
		            newTaskArray.add(taskArray.get(i));
		        }
		   } 
		}
		
		taskArray = newTaskArray;
		
		writeToDisk();
		
		scheduler.recompute();
	}
	
	public String getTimeRequired(String taskName) {
		// used by ModifyMenu
		
		String timeRequired = "";
		
		if (taskArray != null) {
			for (int i=0; i<taskArray.size(); i++) {
				JSONObject taskObject = (JSONObject) taskArray.get(i);
				
				if (taskName.equals(taskObject.get("name"))) {
					timeRequired = (String) taskObject.get("timeRequired");
				}
			}
		}
		
		return timeRequired;
	}
	
	public ArrayList<Task> getTasks() {
		
		ArrayList<Task> taskArrayList = new ArrayList<>();
		
		for (Object i: taskArray) {
			JSONObject task = (JSONObject) i;
			Task taskInstance = new Task((String) task.get("name"), LocalDateTime.parse((String) task.get("deadline")), LocalTime.parse((String) task.get("timeRequired")));
			taskArrayList.add(taskInstance);
		}
		
		return taskArrayList;
	}
	
	//saves data to disk
	public void writeToDisk() {
		try {
			FileWriter file = new FileWriter(taskPath);
			file.write(taskArray.toJSONString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

