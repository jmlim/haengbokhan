package org.haengbokhan.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Hana Lee
 * @since 0.0.2 2013. 1. 21. 오전 7:14:29
 * @revision $LastChangedRevision: 6112 $
 * @date $LastChangedDate: 2013-02-22 23:59:39 +0900 (금, 22 2월 2013) $
 * @by $LastChangedBy: jmlim $
 */
@Entity
@Table(name = "ARTICLES")
@NamedQueries({
		@NamedQuery(name = "org.haengbokhan.model.Article@getArticle(articleId)", query = "from Article as article where ID = :articleId"),
		@NamedQuery(name = "org.haengbokhan.model.Article@getArticles(groupId)", query = "from Article as article where GROUP_ID = :groupId order by ID desc"),
		@NamedQuery(name = "org.haengbokhan.model.Article@getAllArticles()", query = "from Article as article order by ID desc") })
public class Article extends BaseEntity {

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	// 임시
	@JoinColumn(name = "owner", nullable = false)
	private User owner;

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "GROUP_ID", nullable = false)
	private String groupId;

	@OneToMany(targetEntity = ArticleReply.class, mappedBy = "article", cascade = {
			CascadeType.MERGE, CascadeType.REMOVE })
	private List<ArticleReply> articleReplies;

	public Article() {
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the articleReplies
	 */
	public List<ArticleReply> getArticleReplies() {
		return articleReplies;
	}

	/**
	 * @param articleReplies
	 *            the articleReplies to set
	 */
	public void setArticleReplies(List<ArticleReply> articleReplies) {
		this.articleReplies = articleReplies;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
