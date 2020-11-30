package com.joshua.overseer;

import java.time.LocalDate;
import java.time.LocalTime;

public class Session extends SessionForTimer {
	
	LocalDate date;
	Integer id;
	
	public Session(String taskName, LocalTime start, LocalTime end, LocalDate taskDate, Integer id) {
		super(taskName, start, end);
		
		date = taskDate;
		this.id = id;
	}

}
