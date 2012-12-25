package be.virtualsushi.wadisda.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdNameEntity extends BaseEntity {

	private static final long serialVersionUID = 5556325162127384153L;

	@Column(name = "name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
