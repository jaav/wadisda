package be.virtualsushi.wadisda.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location extends IdNameEntity {

	private static final long serialVersionUID = -8203828088972557851L;

	@Column(name = "zipcode")
	private String zipCode;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
