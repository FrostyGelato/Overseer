package com.joshua.overseer;

import java.time.LocalTime;

public class SessionForTimer {

	String name;
	LocalTime startTime;
	LocalTime endTime;
	
	public SessionForTimer(String taskName, LocalTime start, LocalTime end) {
		name = taskName;
		startTime = start;
		endTime = end;
	}

}
