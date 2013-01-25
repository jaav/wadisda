package be.virtualsushi.wadisda.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "product")
public class Product extends IdNameEntity {

	private static final long serialVersionUID = -7286410793577679426L;

	@NotBlank
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}