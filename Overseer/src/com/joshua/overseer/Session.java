package com.joshua.overseer;

import java.time.LocalTime;

public class Session {
	
	String name;
	LocalTime startTime;
	LocalTime endTime;
	
	public Session(String taskName, LocalTime start, LocalTime end) {
		name = taskName;
		startTime = start;
		endTime = end;
	}

}
