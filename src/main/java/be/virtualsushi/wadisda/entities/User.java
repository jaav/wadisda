package be.virtualsushi.wadisda.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import be.virtualsushi.wadisda.entities.enums.Roles;
import be.virtualsushi.wadisda.entities.valueobjects.CalendarInfo;
import be.virtualsushi.wadisda.entities.valueobjects.TasksListInfo;

@Entity
@Table(name = "user")
public class User extends IdNameEntity {

	private static final long serialVersionUID = -3133182804040655061L;

	@Column(name = "email")
	private String email;

	@Column(name = "avatar_url")
	private String avatarUrl;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "user_to_roles", joinColumns = { @JoinColumn(name = "user_id") })
	@Column(name = "roles")
	@Enumerated(EnumType.STRING)
	private Set<Roles> roles;

	@Column(name = "is_active")
	private boolean active;

	@NotNull
	@Embedded
	private CalendarInfo calendar;

	@NotNull
	@Embedded
	private TasksListInfo tasksList;

	@Column(name = "agreement_accepted")
	private boolean agreementAccepted;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void addRole(Roles role) {
		if (roles == null) {
			roles = new HashSet<Roles>();
		}
		roles.add(role);
	}

	public void removeRole(Roles role) {
		if (roles != null) {
			roles.remove(role);
		}
	}

	public boolean hasRole(Roles role) {
		return roles != null && roles.contains(role);
	}

	public TasksListInfo getTasksList() {
		return tasksList;
	}

	public void setTasksList(TasksListInfo tasksList) {
		this.tasksList = tasksList;
	}

	public CalendarInfo getCalendar() {
		return calendar;
	}

	public void setCalendar(CalendarInfo calendar) {
		this.calendar = calendar;
	}

	public boolean isAgreementAccepted() {
		return agreementAccepted;
	}

	public void setAgreementAccepted(boolean agreementAccepted) {
		this.agreementAccepted = agreementAccepted;
	}

}
