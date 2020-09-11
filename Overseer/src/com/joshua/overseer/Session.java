package com.joshua.overseer;

import java.time.LocalTime;

public class Session {
	
	LocalTime startTime;
	LocalTime endTime;
	
	public Session(LocalTime start, LocalTime end) {
		startTime = start;
		endTime = end;
	}

}
