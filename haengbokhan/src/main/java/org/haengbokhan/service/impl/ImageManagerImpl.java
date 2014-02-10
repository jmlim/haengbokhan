package org.haengbokhan.service.impl;

import java.util.Date;
import java.util.List;

import org.haengbokhan.model.Image;
import org.haengbokhan.model.ImageReply;
import org.haengbokhan.service.AbstractJpaDaoService;
import org.haengbokhan.service.ImageManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("imageManager")
@Transactional
public class ImageManagerImpl extends AbstractJpaDaoService implements
		ImageManager {

	/**
	 * @see org.haengbokhan.service.ImageManager#getImage(java.lang.Integer)
	 */
	@Override
	public Image getImage(Integer imageId) {
		return getEntityManager().find(Image.class, imageId);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#deleteImage(org.haengbokhan.model.Image)
	 */
	@Override
	public void deleteImage(Image image) {
		getEntityManager().remove(image);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#updateImage(org.haengbokhan.model.Image)
	 */
	@Override
	public void updateImage(Image image) {
		image.setModifiedDate(new Date());
		getEntityManager().merge(image);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#createImage(org.haengbokhan.model.Image)
	 */
	@Override
	public void createImage(Image image) {
		image.setCreatedDate(new Date());
		getEntityManager().merge(image);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#getAllImages()
	 */
	@Override
	public List<Image> getAllImages() {
		List<Image> results = getEntityManager().createNamedQuery(
				"org.haengbokhan.model.Image@getAllImages()", Image.class)
				.getResultList();

		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#getImages(java.lang.String)
	 */
	@Override
	public List<Image> getImages(String groupId) {
		// TODO Auto-generated method stub
		List<Image> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.Image@getImages(groupId)",
						Image.class).setParameter("groupId", groupId)
				.getResultList();
		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#getImageReply(java.lang.Integer)
	 */
	@Override
	public ImageReply getImageReply(Integer imageReplyId) {
		return getEntityManager().find(ImageReply.class, imageReplyId);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#createImageReply(org.haengbokhan.model.ImageReply)
	 */
	@Override
	public void createImageReply(ImageReply reply) {
		reply.setCreatedDate(new Date());
		getEntityManager().merge(reply);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#updateImageReply(org.haengbokhan.model.ImageReply)
	 */
	@Override
	public void updateImageReply(ImageReply reply) {
		reply.setModifiedDate(new Date());
		getEntityManager().merge(reply);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#deleteImageReply(org.haengbokhan.model.ImageReply)
	 */
	@Override
	public void deleteImageReply(ImageReply reply) {
		getEntityManager().remove(reply);
	}

	/**
	 * @see org.haengbokhan.service.ImageManager#getImageReplies(java.lang.Integer)
	 */
	@Override
	public List<ImageReply> getImageReplies(Integer imageId) {
		// TODO Auto-generated method stub
		List<ImageReply> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.ImageReply@getImageReplies(imageId)",
						ImageReply.class).setParameter("imageId", imageId)
				.getResultList();
		if (results != null && results.size() > 0) {
			return results;
		}
		return null;
	}
}
