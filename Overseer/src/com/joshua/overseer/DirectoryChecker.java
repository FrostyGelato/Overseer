package com.joshua.overseer;

import java.io.File;

public class DirectoryChecker {
	
	String programFolder = System.getProperty("user.home") + File.separator + ".overseer";
	
	public DirectoryChecker() {
		if (new File(programFolder).isDirectory() == false) {
			new File(programFolder).mkdirs();
		}
	}

}
