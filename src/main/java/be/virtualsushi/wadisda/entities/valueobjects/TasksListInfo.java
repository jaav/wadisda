package be.virtualsushi.wadisda.entities.valueobjects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TasksListInfo {

	@Column(name = "tasks_list_title")
	private String title;

	@Column(name = "tasks_list_id")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
