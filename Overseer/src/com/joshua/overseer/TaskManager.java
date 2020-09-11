package com.joshua.overseer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TaskManager {
	
	JSONArray taskArray = new JSONArray();
	JSONObject task = new JSONObject();
	
	String taskPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "tasks.json";
	
	public TaskManager() {
		JSONParser parser = new JSONParser();
    
        try {
        	//parse tasks.json file and load the array data
			taskArray = (JSONArray) parser.parse(new FileReader(taskPath));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void addTask(String name, LocalTime timeRequired, LocalDateTime deadline) {
		task = new JSONObject();
		
		task.put("name", name);
		//colons in time makes JSON invalid
		task.put("timeRequired", timeRequired.toString());
		task.put("deadline", deadline.toString());
		
		taskArray.add(task);
		
		Scheduler scheduler = new Scheduler(deadline.toLocalDate(), timeRequired);
		
		writeToDisk();
	}
	
	public void deleteTask(int arrayIndex) {
		if (arrayIndex >= 0) {
			taskArray.remove(arrayIndex);
			writeToDisk();
		}
	}
	
	public String getTaskName(int arrayIndex) {
		String taskName = "";
		if (arrayIndex >= 0) {
			JSONObject taskToModify = (JSONObject) taskArray.get(arrayIndex);
			taskName = taskToModify.get("name").toString();
		}
		return taskName;
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

