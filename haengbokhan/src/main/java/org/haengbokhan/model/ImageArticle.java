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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "IMAGE_ARTICLES")
@NamedQueries({
		@NamedQuery(name = "org.haengbokhan.model.ImageArticle@getImageArticle(imageArticleId)", query = "from ImageArticle as imageArticle where ID = :imageArticleId"),
		@NamedQuery(name = "org.haengbokhan.model.ImageArticle@getImageArticles(groupId)", query = "from ImageArticle as imageArticle where GROUP_ID = :groupId order by ID desc"),
		@NamedQuery(name = "org.haengbokhan.model.ImageArticle@getAllImageArticles()", query = "from ImageArticle as imageArticle order by ID desc") })
public class ImageArticle extends BaseEntity {

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	// 임시
	@JoinColumn(name = "owner", nullable = false)
	private User owner;

	@Column(name = "TITLE")
	private String title;

	@Lob
	@Column
	private String content;

	@Column(name = "GROUP_ID", nullable = false)
	private String groupId;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "imageId")
	private Image image;

	@OneToMany(targetEntity = ImageArticleReply.class, mappedBy = "imageArticle", cascade = {
			CascadeType.MERGE, CascadeType.REMOVE })
	private List<ImageArticleReply> imageArticleReplies;

	public ImageArticle() {
		super();
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

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the imageArticleReplies
	 */
	public List<ImageArticleReply> getImageArticleReplies() {
		return imageArticleReplies;
	}

	/**
	 * @param imageArticleReplies
	 *            the imageArticleReplies to set
	 */
	public void setImageArticleReplies(
			List<ImageArticleReply> imageArticleReplies) {
		this.imageArticleReplies = imageArticleReplies;
	}
}
