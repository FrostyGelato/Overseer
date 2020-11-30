package com.joshua.overseer;

public class BreakNotification extends Notification {

	public void notification() {
		templateNotification("Break Time", "Take a break.");
	}
	
	// will run immediately if scheduled time has already past
	@Override
	public void run() {
		notification();
	}

}
