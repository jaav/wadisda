package be.virtualsushi.wadisda.entities.valueobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.virtualsushi.wadisda.entities.User;

public class Message {

	private String title;

	private String description;

	private String status;

	private Date dueDate;

	private List<User> attendees;

	private String registrationLink;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String decription) {
		this.description = decription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<User> attendees) {
		this.attendees = attendees;
	}

	public void addAttendee(User attendee) {
		if (attendees == null) {
			attendees = new ArrayList<User>();
		}
		attendees.add(attendee);
	}

	public void removeAttendee(User attendee) {
		if (attendees != null) {
			attendees.remove(attendee);
		}
	}

	public boolean isUserAttended(User user) {
		return attendees != null && attendees.contains(user);
	}

	public String getRegistrationLink() {
		return registrationLink;
	}

	public void setRegistrationLink(String registrationLink) {
		this.registrationLink = registrationLink;
	}

}
