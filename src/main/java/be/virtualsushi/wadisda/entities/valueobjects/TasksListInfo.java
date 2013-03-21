package be.virtualsushi.wadisda.entities.valueobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TasksListInfo implements Serializable {

	private static final long serialVersionUID = 7563313430849412452L;

	@Column(name = "tasks_list_title")
	private String title;

	@Column(name = "tasks_list_id")
	private String id;

	public TasksListInfo() {

	}

	public TasksListInfo(String id, String title) {
		this.id = id;
		this.title = title;
	}

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
