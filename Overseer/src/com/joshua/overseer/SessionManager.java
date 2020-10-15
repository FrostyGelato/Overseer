package com.joshua.overseer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SessionManager {
	
	JSONArray sessionArray = new JSONArray();
	JSONObject sessionPeriod = new JSONObject();
	ArrayList<Session> sessionArrayList = new ArrayList<>();
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
	
	public void addSessions(ArrayList<Session> sessions) {
		
		for (Session i: sessions) {
			sessionPeriod = new JSONObject();
			
			String startTimeWithoutNano = i.startTime.truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_LOCAL_TIME);
			String endTimeWithoutNano = i.endTime.truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_LOCAL_TIME);
			
			sessionPeriod.put("name", i.name);
			sessionPeriod.put("startTime", startTimeWithoutNano);
			sessionPeriod.put("endTime", endTimeWithoutNano);
			sessionPeriod.put("date", i.date.toString());
			
			sessionArray.add(sessionPeriod);
		}
		
		writeToDisk();
	}
	
	public void clearSession() {
		
		sessionArray.clear();
		
		writeToDisk();
	}
	
	public ArrayList<Session> getSessions() {
		for (Object i: sessionArray) {
			JSONObject session = (JSONObject) i;
			Session sessionInstance = new Session((String) session.get("name"), LocalTime.parse((String) session.get("startTime")), LocalTime.parse((String) session.get("endTime")), LocalDate.parse((String) session.get("date")));
			sessionArrayList.add(sessionInstance);
		}
		
		return sessionArrayList;
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
