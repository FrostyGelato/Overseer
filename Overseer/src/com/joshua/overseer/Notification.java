package com.joshua.overseer;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class Notification {

	public Notification(String taskName) {
		if (SystemTray.isSupported()) {
			SystemTray alert = SystemTray.getSystemTray();

            //If the icon is a file
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            trayIcon.setToolTip("System tray icon demo");
            try {
				alert.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}

            trayIcon.displayMessage(taskName, "Time to get started on this task!", MessageType.INFO);
        } else {
            System.err.println("Notifications are not supported on your device.");
        }
	}

}
