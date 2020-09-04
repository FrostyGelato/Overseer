package com.joshua.overseer;

import java.io.File;

public class DirectoryChecker {
	
	// program directory is in user's home directory
	String programFolder = System.getProperty("user.home") + File.separator + ".overseer";
	
	public DirectoryChecker() {
		// checks if the .overseer directory is present
		if (new File(programFolder).isDirectory() == false) {
			new File(programFolder).mkdirs(); // if not, create directory
		}
	}

}
