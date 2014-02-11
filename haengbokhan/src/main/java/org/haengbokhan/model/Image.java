package org.haengbokhan.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "IMAGES")
@NamedQueries({
		@NamedQuery(name = "org.haengbokhan.model.Image@getImage(imageId)", query = "from Image as image where ID = :imageId"),
		@NamedQuery(name = "org.haengbokhan.model.Image@getImages(groupId)", query = "from Image as image where GROUP_ID = :groupId order by ID desc"),
		@NamedQuery(name = "org.haengbokhan.model.Image@getAllImages()", query = "from Image as image order by ID desc") })
public class Image extends BaseEntity {

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner", nullable = false)
	private User owner;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "REAL_NAME", nullable = false)
	private String realName;

	@Column(name = "IMAGE_SIZE", nullable = false)
	private Long size;

	@Column(name = "IMAGE_URL")
	private String url;

	@Column(name = "GROUP_ID")
	private String groupId;

	@Column(name = "IMAGE_PATH")
	private String path;

	@Column(name = "CONTENT_TYPE")
	private String contentType;

	@OneToMany(targetEntity = ImageReply.class, mappedBy = "image", cascade = {
			CascadeType.MERGE, CascadeType.REMOVE })
	private List<ImageReply> imageReplies;

	public Image() {
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
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the imageReplies
	 */
	public List<ImageReply> getImageReplies() {
		return imageReplies;
	}

	/**
	 * @param imageReplies
	 *            the imageReplies to set
	 */
	public void setImageReplies(List<ImageReply> imageReplies) {
		this.imageReplies = imageReplies;
	}
}
