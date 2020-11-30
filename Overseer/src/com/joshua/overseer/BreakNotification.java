package com.joshua.overseer;

public class BreakNotification extends Notification {

	public void notification() {
		templateNotification("Break Time", "Take a break.");
	}
	
	@Override
	public void run() {
		notification();
	}

}
