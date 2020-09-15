package com.joshua.overseer;

import java.util.TimerTask;

public class MyTimeTask extends TimerTask {

	public MyTimeTask() {
		
	}

	@Override
	public void run() {
		Notification notification = new Notification("Temp");
	}

}
