package be.virtualsushi.wadisda.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import be.virtualsushi.wadisda.entities.enums.Genders;

@Entity
@Table(name = "registration")
public class Registration extends BaseEntity {

	private static final long serialVersionUID = -4718943812734144874L;

	@Column(name = "epoch")
	private Date epoch;

	@Column(name = "duration")
	private Integer duration;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@ManyToOne
	@JoinColumn(name = "conversation_type")
	private ConversationType conversationType;

	@Column(name = "question", length = 255)
	private String question;

	@Column(name = "gender", length = 1)
	@Enumerated(EnumType.STRING)
	private Genders gender;

	@Column(name = "age")
	private Integer age;

	@ManyToOne
	@JoinColumn(name = "location")
	private Location location;

	@ManyToOne
	@JoinColumn(name = "presentation")
	private Presentation presentation;

	@ManyToOne
	@JoinColumn(name = "attitude")
	private Attitude attitude;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "product_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "product", referencedColumnName = "id"))
	private Set<Product> products;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "product_question_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "product_question", referencedColumnName = "id"))
	private Set<ProductQuestion> productQuestions;

	@ManyToOne
	@JoinColumn(name = "question_type")
	private QuestionType questionType;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "relation_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "relation", referencedColumnName = "id"))
	private Set<Relation> relations;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "social_context_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "social_context", referencedColumnName = "id"))
	private Set<SocialContext> socialContexts;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "referrer_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "referrer", referencedColumnName = "id"))
	private Set<Referer> referers;

	@Column(name = "interrupted")
	@Type(type = "true_false")
	private Boolean interrupted;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "task_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "task", referencedColumnName = "id"))
	private Set<Task> tasks;

	@Column(name = "has_violence")
	@Type(type = "true_false")
	private Boolean hasViolence;

	@Column(name = "tp_gender", length = 1)
	@Enumerated(EnumType.STRING)
	private Genders tpGender;

	@Column(name = "tp_age")
	private Integer tpAge;

	@ManyToOne
	@JoinColumn(name = "tp_usage_type")
	private UsageType tpUsageType;

	@ManyToOne
	@JoinColumn(name = "origin")
	private Origin origin;

	public Date getEpoch() {
		return epoch;
	}

	public void setEpoch(Date epoch) {
		this.epoch = epoch;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ConversationType getConversationType() {
		return conversationType;
	}

	public void setConversationType(ConversationType conversationType) {
		this.conversationType = conversationType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Genders getGender() {
		return gender;
	}

	public void setGender(Genders gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	public Attitude getAttitude() {
		return attitude;
	}

	public void setAttitude(Attitude attitude) {
		this.attitude = attitude;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Set<ProductQuestion> getProductQuestions() {
		return productQuestions;
	}

	public void setProductQuestions(Set<ProductQuestion> productQuestions) {
		this.productQuestions = productQuestions;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public Set<Relation> getRelations() {
		return relations;
	}

	public void setRelations(Set<Relation> relations) {
		this.relations = relations;
	}

	public Set<SocialContext> getSocialContexts() {
		return socialContexts;
	}

	public void setSocialContexts(Set<SocialContext> socialContexts) {
		this.socialContexts = socialContexts;
	}

	public Set<Referer> getReferers() {
		return referers;
	}

	public void setReferers(Set<Referer> referers) {
		this.referers = referers;
	}

	public Boolean getInterrupted() {
		return interrupted;
	}

	public void setInterrupted(Boolean interrupted) {
		this.interrupted = interrupted;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Boolean getHasViolence() {
		return hasViolence;
	}

	public void setHasViolence(Boolean hasViolence) {
		this.hasViolence = hasViolence;
	}

	public Genders getTpGender() {
		return tpGender;
	}

	public void setTpGender(Genders tpGender) {
		this.tpGender = tpGender;
	}

	public Integer getTpAge() {
		return tpAge;
	}

	public void setTpAge(Integer tpAge) {
		this.tpAge = tpAge;
	}

	public UsageType getTpUsageType() {
		return tpUsageType;
	}

	public void setTpUsageType(UsageType tpUsageType) {
		this.tpUsageType = tpUsageType;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}
}