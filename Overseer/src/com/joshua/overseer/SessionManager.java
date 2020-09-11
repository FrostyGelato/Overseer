package com.joshua.overseer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SessionManager {
	
	JSONArray sessionArray = new JSONArray();
	JSONObject sessionPeriod = new JSONObject();
	String sessionName;
	
	String sessionPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "session.json";

	public SessionManager() {
		
		JSONParser parser = new JSONParser();
	    
		if (new File(sessionPath).isFile() == true) {
			try {
	        	//parse session.json file and load the array data
				sessionArray = (JSONArray) parser.parse(new FileReader(sessionPath));
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void addSessions(Session[] sessions) {
		
		for (Session i: sessions) {
			sessionPeriod = new JSONObject();
			
			sessionName = i.name;
			
			System.out.println(sessionName);
			
			sessionPeriod.put("name", sessionName);
			
			System.out.println(i.startTime);
			sessionPeriod.put("startTime", i.startTime);
			sessionPeriod.put("endTime", i.endTime);
			sessionPeriod.put("date", i.date);
			
			sessionArray.add(sessionPeriod);
		}
		
		writeToDisk();
	}
	
	public void writeToDisk() {
		try {
			FileWriter file = new FileWriter(sessionPath);
			file.write(sessionArray.toJSONString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
