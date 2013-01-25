package be.virtualsushi.wadisda.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "product_question")
public class ProductQuestion extends BaseEntity {

	private static final long serialVersionUID = 2854313505055601984L;

	@NotBlank
	private String question;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

}