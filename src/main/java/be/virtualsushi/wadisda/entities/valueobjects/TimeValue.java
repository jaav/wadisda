package be.virtualsushi.wadisda.entities.valueobjects;

import java.util.Calendar;
import java.util.Date;

public class TimeValue {

	private int hours;
	private int minutes;

	public TimeValue(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public static TimeValue of(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return new TimeValue(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	}

}
