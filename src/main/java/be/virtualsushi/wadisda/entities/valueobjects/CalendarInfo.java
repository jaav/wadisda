package be.virtualsushi.wadisda.entities.valueobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CalendarInfo implements Serializable {

	private static final long serialVersionUID = -1287118940623763850L;

	@Column(name = "summary")
	private String summary;

	@Column(name = "calendar_id")
	private String id;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getId() {
		return id;
	}

	public void setId(String calendarId) {
		this.id = calendarId;
	}

}
