package be.virtualsushi.wadisda.entities;

import java.util.Date;
import java.util.HashSet;
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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import be.virtualsushi.wadisda.entities.enums.Genders;
import be.virtualsushi.wadisda.entities.valueobjects.TimeValue;

@Entity
@Table(name = "registration")
public class Registration extends BaseEntity {

	private static final long serialVersionUID = -4718943812734144874L;

	@NotNull
	@Column(name = "epoch")
	private Date epoch;

	@NotNull
	@Column(name = "duration")
	private Integer duration;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "conversation_type")
	private ConversationType conversationType;

	@NotBlank
	@Column(name = "question", length = 255)
	private String question;

	@NotNull
	@Column(name = "gender", length = 1)
	@Enumerated(EnumType.STRING)
	private Genders gender;

	@NotNull
	@Column(name = "age")
	private Integer age;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "location")
	private Location location;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "presentation")
	private Presentation presentation;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "attitude")
	private Attitude attitude;

	@NotEmpty
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "product_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "product", referencedColumnName = "id"))
	private Set<Product> products;

	@NotEmpty
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "product_question_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "product_question", referencedColumnName = "id"))
	private Set<ProductQuestion> productQuestions;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "question_type")
	private QuestionType questionType;

	@NotEmpty
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "relation_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "relation", referencedColumnName = "id"))
	private Set<Relation> relations;

	@NotEmpty
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "social_context_map", joinColumns = @JoinColumn(name = "registration", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "social_context", referencedColumnName = "id"))
	private Set<SocialContext> socialContexts;

	@NotEmpty
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

	@NotNull
	@Column(name = "tp_gender", length = 1)
	@Enumerated(EnumType.STRING)
	private Genders tpGender;

	@NotNull
	@Column(name = "tp_age")
	private Integer tpAge;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "tp_usage_type")
	private UsageType tpUsageType;

	@NotNull
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

	public void addRelation(Relation relation) {
		if (relation == null) {
			return;
		}
		if (relations == null) {
			relations = new HashSet<Relation>();
		}
		if (!relations.contains(relation)) {
			relations.add(relation);
		}
	}

	public void addReferer(Referer referer) {
		if (referer == null) {
			return;
		}
		if (referers == null) {
			referers = new HashSet<Referer>();
		}
		if (!referers.contains(referer)) {
			referers.add(referer);
		}
	}

	public void addProduct(Product product) {
		if (product == null) {
			return;
		}
		if (products == null) {
			products = new HashSet<Product>();
		}
		if (!products.contains(product)) {
			products.add(product);
		}
	}

	public void addProductgQuestion(ProductQuestion productQuestion) {
		if (productQuestion == null) {
			return;
		}
		if (productQuestions == null) {
			productQuestions = new HashSet<ProductQuestion>();
		}
		if (!productQuestions.contains(productQuestion)) {
			productQuestions.add(productQuestion);
		}
	}

	public void addSocialContext(SocialContext socialContext) {
		if (socialContext == null) {
			return;
		}
		if (socialContexts == null) {
			socialContexts = new HashSet<SocialContext>();
		}
		if (!socialContexts.contains(socialContext)) {
			socialContexts.add(socialContext);
		}
	}

	public TimeValue getTime() {
		return TimeValue.of(epoch);
	}

}
