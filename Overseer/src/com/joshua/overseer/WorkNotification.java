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
	
	// will run immediately if scheduled time has already past
	@Override
	public void run() {
		notification(name);
	}

}
