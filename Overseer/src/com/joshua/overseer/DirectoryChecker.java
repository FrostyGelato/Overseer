package com.joshua.overseer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DirectoryChecker {
	
	// program directory is in user's home directory
	String programFolder = System.getProperty("user.home") + File.separator + ".overseer";
	String sessionPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "session.json";
	
	public DirectoryChecker() {
		// checks if the .overseer directory is present
		if (new File(programFolder).isDirectory() == false) {
			new File(programFolder).mkdirs(); // if not, create directory
		}
	}
	
	public boolean doesSessionExists() {
		if (new File(sessionPath).isFile() == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public void createSessionFile() {
		try {
			FileWriter file = new FileWriter(sessionPath);
			file.append("[]");
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
