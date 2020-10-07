package com.joshua.overseer;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Task {
	
	public String name;
	public LocalDateTime deadline;
	public LocalTime timeRequired;

	public Task(String name, LocalDateTime deadline, LocalTime timeRequired) {
		super();
		this.name = name;
		this.deadline = deadline;
		this.timeRequired = timeRequired;
	}

}
