package com.joshua.overseer;

import java.time.LocalDate;
import java.time.LocalTime;

public class Session {
	
	String name;
	LocalTime startTime;
	LocalTime endTime;
	LocalDate date;
	Integer id;
	
	public Session(String taskName, LocalTime start, LocalTime end, LocalDate taskDate, Integer id) {
		name = taskName;
		startTime = start;
		endTime = end;
		date = taskDate;
		this.id = id;
	}

}
