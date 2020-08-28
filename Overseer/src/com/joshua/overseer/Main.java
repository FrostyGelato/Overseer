// made by Joshua Fan
package com.joshua.overseer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	
	//loads main window
	private static void showGUI() {
		MainMenu mainFrame = new MainMenu();
		mainFrame.setVisible(true);
		// Set window title
		mainFrame.setTitle("Overseer");
	}
	
	// Use system UI
	private static void useSystemLF() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		useSystemLF();
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	showGUI();
	        }
	    });
	}

}

