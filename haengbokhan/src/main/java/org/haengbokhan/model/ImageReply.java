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

/**
 * @author Administrator
 * 
 */
@Entity
@Table(name = "IMAGE_REPLIES")
@NamedQueries({
		@NamedQuery(name = "org.haengbokhan.model.ImageReply@getImageReplies(imageId)", query = "from ImageReply as reply where image_id = :imageId order by ID"),
		@NamedQuery(name = "org.haengbokhan.model.ImageReply@getImageReply(imageReplyId)", query = "from ImageReply as reply where ID = :imageReplyId") })
public class ImageReply extends BaseEntity {

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	@JoinColumn(name = "image_id", nullable = false)
	private Image image;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	private User owner;

	@Lob
	@Column(nullable = false)
	private String content;

	public ImageReply() {
		super();
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
