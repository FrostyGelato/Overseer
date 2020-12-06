package com.joshua.overseer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Scheduler3 {

	// get current date and time
	LocalTime currentTime = LocalTime.now();
	LocalDate today = LocalDate.now();
	LocalDate tomorrow = today.plusDays(1);

	SessionManager sessionManager = new SessionManager();
	// needed to fetch settings
	ConfigManager configManager = new ConfigManager();

	// fetch setting data
	Integer configCombinedMinutes = configManager.getCombinedTimeLength();
	Integer configBreakMinutes = configManager.getBreakTimeLength();
	Integer configWorkMinutes = configManager.getWorkTimeLength();

	LocalTime configWorkStartTime = LocalTime.parse(configManager.getStartTime());
	LocalTime configWorkEndTime = LocalTime.parse(configManager.getEndTime());

	LocalTime previousSessionEnds = configWorkStartTime;
	LocalTime lastSessionEnds = previousSessionEnds;

	public void recompute() {

		sessionManager.sessionArray.clear();

		TaskManager taskManager = new TaskManager();

		ArrayList<Task> taskArrayList = taskManager.getTasks();

		// if arrayList is empty, clear session file
		if (taskArrayList.isEmpty()) {

			sessionManager.clearSession();

		} else {

			for (Task i : taskArrayList) {
				add(i.name, i.deadline.toLocalDate(), i.timeRequired);
			}
		}
	}

	public void add(String name, LocalDate deadline, LocalTime timeRequired) {

		// convert timeRequired from LocalTime to Duration
		Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		int minutesRequired = timeRequired.getMinute();
		Integer durationRequiredInMinutes = (int) hoursRequired.toMinutes() + minutesRequired;

		// create array
		ArrayList<Session> sessionArray = new ArrayList<Session>();

		// creates a dictionary to store separate sessionNumbers for each day
		Map<LocalDate, Integer> sessionNumberMap = new HashMap<LocalDate, Integer>();

		Session newSession;

		LocalDate date = today;
		
		Integer id = 0;

		// allocates sessions across multiple days
		while (durationRequiredInMinutes > 0) {

			// gets ALL sessions
			ArrayList<Session> sessionArrayList = sessionManager.getSessions();

			previousSessionEnds = configWorkStartTime;
			lastSessionEnds = previousSessionEnds;

			if (!(date.equals(deadline))) {

				for (Session session : sessionArrayList) {

					if (session.date.equals(date)) {

						if (session.endTime.isAfter(previousSessionEnds)) {

							lastSessionEnds = session.endTime;
							previousSessionEnds = lastSessionEnds;
						}
					}
				}

				LocalTime availableTime = configWorkStartTime;

				Duration remainderOfWorkPeriod;

				// only runs the day the task is added
				if (date.equals(today)) {

					if (lastSessionEnds.isAfter(currentTime)) {
						availableTime = lastSessionEnds.plusMinutes(5);
					} else {
						availableTime = currentTime;
					}

					// how much time until work period ends
					remainderOfWorkPeriod = Duration.between(currentTime, configWorkEndTime);

				} else {

					if (!(lastSessionEnds.equals(configWorkStartTime))) {
						availableTime = lastSessionEnds.plusMinutes(5);
					}

					remainderOfWorkPeriod = Duration.between(availableTime, configWorkEndTime);
				}

				Integer remainderOfWorkPeriodInMin = (int) remainderOfWorkPeriod.toMinutes();

				// only add session if there is empty time that day
				if (remainderOfWorkPeriodInMin > configCombinedMinutes) {
					// set session start and end times
					LocalTime sessionStartTime = availableTime
							.plusMinutes(configCombinedMinutes * sessionNumberMap.getOrDefault(date, 0));
					LocalTime sessionEndTime = sessionStartTime.plusMinutes(configWorkMinutes);

					// create new session
					newSession = new Session(name, sessionStartTime, sessionEndTime, date, id);

					// add session to array
					sessionArray.add(newSession);

					durationRequiredInMinutes = durationRequiredInMinutes - configWorkMinutes;
					remainderOfWorkPeriodInMin = remainderOfWorkPeriodInMin
							- ((sessionNumberMap.getOrDefault(date, 0) + 1) * configCombinedMinutes);

					sessionNumberMap.put(date, sessionNumberMap.getOrDefault(date, 0) + 1);
					
					id++;
				}

				date = date.plusDays(1);

				// reset to today after cycling through all days between today and deadline
				if (date.equals(deadline) && durationRequiredInMinutes > 0) {
					date = today;
				}
			}
		}
		sessionManager.addSessions(sessionArray);
	}
}