package com.joshua.overseer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DirectoryChecker {
	
	// program directory is in user's home directory
	String programFolderPath = System.getProperty("user.home") + File.separator + ".overseer";
	String sessionPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "session.json";
	
	public DirectoryChecker() {
		// checks if the .overseer directory is present
		if (new File(programFolderPath).isDirectory() == false) {
			new File(programFolderPath).mkdirs(); // if not, create directory
		}
	}
	
	public void checkForSession() {
		if (new File(sessionPath).isFile() == false) {
			createSessionFile();
		}
	}
	
	private void createSessionFile() {
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
