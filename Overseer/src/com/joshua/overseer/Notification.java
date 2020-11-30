package com.joshua.overseer;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.TimerTask;

public class Notification extends TimerTask {
	
	public void templateNotification(String title, String message) {
		if (SystemTray.isSupported()) {
			SystemTray alert = SystemTray.getSystemTray();

            // if the icon is a file
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            // alternative (if the icon is on the classpath):
            // Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            // let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            // set tooltip text for the tray icon
            trayIcon.setToolTip("System tray icon demo");
            try {
				alert.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}

            trayIcon.displayMessage(title, message, MessageType.INFO);
            
        } else {
            System.err.println("Notifications are not supported on your OS.");
        }
	}

	@Override
	public void run() {

	}

}
