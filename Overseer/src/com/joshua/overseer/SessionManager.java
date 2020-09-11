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
	JSONObject session = new JSONObject();
	
	String sessionPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "session.json";

	public SessionManager() {
		
		JSONParser parser = new JSONParser();
	    
        try {
        	//parse session.json file and load the array data
			sessionArray = (JSONArray) parser.parse(new FileReader(sessionPath));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addSessions(Session[] sessions) {
		
		for (Session session: sessions) {
			JSONObject sessionPeriod = new JSONObject();
			
			sessionPeriod.put("startTime", session.startTime);
			sessionPeriod.put("endTime", session.endTime);
			
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
