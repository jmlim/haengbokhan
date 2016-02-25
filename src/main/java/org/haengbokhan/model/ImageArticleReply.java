package org.haengbokhan.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "IMAGE_ARTICLE_REPLIES")
@NamedQueries({
		@NamedQuery(name = "org.haengbokhan.model.ImageArticleReply@getImageArticleReplies(imageArticleId)", query = "from ImageArticleReply as reply where image_article_id = :imageArticleId order by ID"),
		@NamedQuery(name = "org.haengbokhan.model.ImageArticleReply@getImageArticleReply(imageArticleReplyId)", query = "from ImageArticleReply as reply where ID = :imageArticleReplyId") })
public class ImageArticleReply extends BaseEntity {

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	@JoinColumn(name = "image_article_id", nullable = false)
	private ImageArticle imageArticle;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	private User owner;

	@Lob
	@Column(nullable = false)
	private String content;

	/**
	 * @return the imageArticle
	 */
	public ImageArticle getImageArticle() {
		return imageArticle;
	}

	/**
	 * @param imageArticle
	 *            the imageArticle to set
	 */
	public void setImageArticle(ImageArticle imageArticle) {
		this.imageArticle = imageArticle;
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
}
