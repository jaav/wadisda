package be.virtualsushi.wadisda.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "task_type")
public class TaskType extends IdNameEntity {

	private static final long serialVersionUID = 4235145443347661324L;

}
