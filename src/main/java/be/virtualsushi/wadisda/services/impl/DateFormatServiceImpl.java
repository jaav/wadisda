package be.virtualsushi.wadisda.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.ioc.Messages;

import be.virtualsushi.wadisda.services.DateFormatService;

public class DateFormatServiceImpl implements DateFormatService {

	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;

	public DateFormatServiceImpl(Messages messages) {
		dateFormat = new SimpleDateFormat(messages.get("date.pattern"));
		timeFormat = new SimpleDateFormat(messages.get("time.pattern"));
	}

	@Override
	public String formatDate(Date date) {
		return dateFormat.format(date);
	}

	@Override
	public String formatTime(Date date) {
		return timeFormat.format(date);
	}

}
