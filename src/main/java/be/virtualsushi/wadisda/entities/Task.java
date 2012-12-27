package be.virtualsushi.wadisda.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class Task extends IdNameEntity {

	private static final long serialVersionUID = -8132594827462746290L;

	@Column(name = "creator")
	private User creator;

	@Column(name = "previous_assignee")
	private User previousAssignee;

	@Column(name = "assignee")
	private User assignee;

	@Column(name = "description")
	private String description;

	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "due_date")
	private Date dueDate;

	@Column(name = "importance")
	private Integer importance;

	@Column(name = "status")
	private Integer status;

	@Column(name = "task_type")
	private TaskType taskType;

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getPreviousAssignee() {
		return previousAssignee;
	}

	public void setPreviousAssignee(User previousAssignee) {
		this.previousAssignee = previousAssignee;
	}

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getImportance() {
		return importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

}
