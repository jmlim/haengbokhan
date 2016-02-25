package org.haengbokhan.service.impl;

import java.util.Date;
import java.util.List;

import org.haengbokhan.model.ImageArticle;
import org.haengbokhan.model.ImageArticleReply;
import org.haengbokhan.service.AbstractJpaDaoService;
import org.haengbokhan.service.ImageArticleManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("imageArticleManager")
@Transactional
public class ImageArticleManagerImpl extends AbstractJpaDaoService implements
		ImageArticleManager {

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#getImageArticle(java.lang.Integer)
	 */
	@Override
	public ImageArticle getImageArticle(Integer imageArticleId) {
		return getEntityManager().find(ImageArticle.class, imageArticleId);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#deleteImageArticle(org.haengbokhan.model.ImageArticle)
	 */
	@Override
	public void deleteImageArticle(ImageArticle imageArticle) {
		getEntityManager().remove(imageArticle);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#updateImageArticle(org.haengbokhan.model.ImageArticle)
	 */
	@Override
	public void updateImageArticle(ImageArticle imageArticle) {
		imageArticle.setModifiedDate(new Date());
		getEntityManager().merge(imageArticle);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#createImageArticle(org.haengbokhan.model.ImageArticle)
	 */
	@Override
	public void createImageArticle(ImageArticle imageArticle) {
		imageArticle.setCreatedDate(new Date());
		getEntityManager().persist(imageArticle);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#getAllImageArticles()
	 */
	@Override
	public List<ImageArticle> getAllImageArticles() {
		List<ImageArticle> results = getEntityManager().createNamedQuery(
				"org.haengbokhan.model.ImageArticle@getAllImageArticles()",
				ImageArticle.class).getResultList();

		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#getImageArticles(java.lang.String)
	 */
	@Override
	public List<ImageArticle> getImageArticles(String groupId) {
		// TODO Auto-generated method stub
		List<ImageArticle> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.ImageArticle@getImageArticles(groupId)",
						ImageArticle.class).setParameter("groupId", groupId)
				.getResultList();
		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#getImageArticleReply(java.lang.Integer)
	 */
	@Override
	public ImageArticleReply getImageArticleReply(Integer imageArticleReplyId) {
		return getEntityManager().find(ImageArticleReply.class,
				imageArticleReplyId);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#createImageArticleReply(org.haengbokhan.model.ImageArticleReply)
	 */
	@Override
	public void createImageArticleReply(ImageArticleReply reply) {
		reply.setCreatedDate(new Date());
		getEntityManager().merge(reply);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#updateImageArticleReply(org.haengbokhan.model.ImageArticleReply)
	 */
	@Override
	public void updateImageArticleReply(ImageArticleReply reply) {
		reply.setModifiedDate(new Date());
		getEntityManager().merge(reply);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#deleteImageArticleReply(org.haengbokhan.model.ImageArticleReply)
	 */
	@Override
	public void deleteImageArticleReply(ImageArticleReply reply) {
		getEntityManager().remove(reply);
	}

	/**
	 * @see org.haengbokhan.service.ImageArticleManager#getImageArticleReplies(java.lang.Integer)
	 */
	@Override
	public List<ImageArticleReply> getImageArticleReplies(Integer imageArticleId) {
		// TODO Auto-generated method stub
		List<ImageArticleReply> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.ImageArticleReply@getImageArticleReplies(imageArticleId)",
						ImageArticleReply.class)
				.setParameter("imageArticleId", imageArticleId).getResultList();
		if (results != null && results.size() > 0) {
			return results;
		}
		return null;
	}
}
