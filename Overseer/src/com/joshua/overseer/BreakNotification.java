package com.joshua.overseer;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.TimerTask;

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
