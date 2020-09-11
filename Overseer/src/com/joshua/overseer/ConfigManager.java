package com.joshua.overseer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;

public class ConfigManager {
	
	Properties appConfig;
	String configPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "config.properties";
	String defaultConfigPath = "src/default.properties";
	
	//loads properties
	public ConfigManager() {
		try {
			//loads default configuration from storage
			Properties defaultConfig = new Properties();
			FileInputStream in = new FileInputStream(defaultConfigPath);
			defaultConfig.load(in);
			in.close();
			
			//use default configuration to create custom config
			appConfig = new Properties(defaultConfig);
			
			//checks if previous user-set configuration is available
			if (new File(configPath).isFile() == true) {
				in = new FileInputStream(configPath);
				appConfig.load(in);
				in.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	//writes data to disk
	public void saveProperties() throws IOException {
		//stores data in user home directory
		FileOutputStream out = new FileOutputStream(configPath);
		appConfig.store(out, "---No Comment---");
		out.close();
	}
	
	//returns the length of break time
	public int getBreakTimeLength() {
		return Integer.parseInt(appConfig.getProperty("breakTimeLength"));
	}
	
	public int getWorkTimeLength() {
		return Integer.parseInt(appConfig.getProperty("workTimeLength"));
	}
	
	public int getCombinedTimeLength() {
		return getBreakTimeLength() + getWorkTimeLength();
	}
	
	public String getStartTime() {
		return appConfig.getProperty("startTime").toString();
	}
	
	public String getEndTime() {
		return appConfig.getProperty("endTime").toString();
	}
	
	//gets all the user-set values from components
	public void saveData(int breakLength, int workLength, LocalTime startTime, LocalTime endTime) {
		String breakLengthString = Integer.toString(breakLength);
		String workLengthString = Integer.toString(workLength);
		appConfig.setProperty("breakTimeLength", breakLengthString);
		appConfig.setProperty("workTimeLength", workLengthString);
		appConfig.setProperty("startTime", startTime.toString());
		appConfig.setProperty("endTime", endTime.toString());
	}

}

