package com.joshua.overseer;

public class WorkNotification extends Notification {
	
	String name;
	
	// runs when the timer is set up
	public WorkNotification(String taskName) {
		name = taskName;
	}

	public void notification(String taskName) {
		templateNotification(taskName, "Time to get started on this task!");
	}
	
	@Override
	public void run() {
		notification(name);
	}

}
