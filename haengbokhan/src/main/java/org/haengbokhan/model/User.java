package org.haengbokhan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Hana Lee
 * @since 0.0.2 2013. 1. 21. 오전 7:11:00
 * @revision $LastChangedRevision: 6112 $
 * @date $LastChangedDate: 2013-02-22 23:59:39 +0900 (금, 22 2월 2013) $
 * @by $LastChangedBy: jmlim $
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
		@NamedQuery(name = "org.haengbokhan.model.User@getUser():param.userId", query = "from User as user where ID = :userId"),
		@NamedQuery(name = "org.haengbokhan.model.User@getUser():param.uId", query = "from User as user where U_ID = :uId"),
		@NamedQuery(name = "org.haengbokhan.model.User@getUser()", query = "from User as user") })
public class User extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6582137205440661975L;

	// uid 를 부적합한 식별자 때문에 변경하였음.
	@Column(name = "U_ID", nullable = false)
	private String uid;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "EMAIL", nullable = false)
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	@OneToMany(targetEntity = Article.class, mappedBy = "owner", cascade = CascadeType.ALL)
	private List<Article> articles;

	@Column(name = "PENALTY")
	private Integer penaltyScore = 0;

	@Column(name = "ROLE")
	private String role;

	public User() {
	}

	/**
	 * @param uid
	 * @param name
	 * @param email
	 */
	public User(String uid, String name, String email) {
		this.uid = uid;
		this.name = name;
		this.email = email;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the articles
	 */
	public List<Article> getArticles() {
		if (this.articles == null) {
			this.articles = new ArrayList<Article>();
		}
		return this.articles;
	}

	/**
	 * @param article
	 */
	public void addArticle(Article article) {
		getArticles().add(article);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the penaltyScore
	 */
	public Integer getPenaltyScore() {
		return penaltyScore;
	}

	/**
	 * @param penaltyScore
	 *            the penaltyScore to set
	 */
	public void setPenaltyScore(Integer penaltyScore) {
		this.penaltyScore = penaltyScore;
	}

	/**
	 * @param articles
	 *            the articles to set
	 */
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User information (id : " + this.getId() + ", uid : "
				+ this.getUid() + ", name : " + this.getName() + ", email : "
				+ this.getEmail() + ")";
	}

}
