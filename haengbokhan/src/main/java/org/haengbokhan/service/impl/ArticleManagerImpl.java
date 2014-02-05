package org.haengbokhan.service.impl;

import java.util.Date;
import java.util.List;

import org.haengbokhan.model.Article;
import org.haengbokhan.model.ArticleReply;
import org.haengbokhan.service.AbstractJpaDaoService;
import org.haengbokhan.service.ArticleManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Hana Lee
 * @since 0.0.2 2013. 1. 21. 오전 7:15:55
 * @revision $LastChangedRevision: 6070 $
 * @date $LastChangedDate: 2013-02-16 12:31:02 +0900 (토, 16 2월 2013) $
 * @by $LastChangedBy: jmlim $
 */
@Service("articleManager")
@Transactional
public class ArticleManagerImpl extends AbstractJpaDaoService implements
		ArticleManager {

	/**
	 * @see org.haengbokhan.service.ArticleManager#getArticle(java.lang.String)
	 */
	@Transactional(readOnly = true)
	public Article getArticle(Integer articleId) {
		return getEntityManager().find(Article.class, articleId);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#deleteArticle(org.haengbokhan.model.Article)
	 */
	public void deleteArticle(Article article) {
		article.setDeletedDate(new Date());
		article.setEnabled(false);
		getEntityManager().merge(article);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#updateArticle(org.haengbokhan.model.Article)
	 */
	public void updateArticle(Article article) {
		article.setModifiedDate(new Date());
		getEntityManager().merge(article);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#createArticle(org.haengbokhan.model.Article)
	 */
	public void createArticle(Article article) {
		article.setCreatedDate(new Date());
		getEntityManager().merge(article);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#getArticles()
	 */
	@Transactional(readOnly = true)
	public List<Article> getArticles() {

		List<Article> results = getEntityManager().createNamedQuery(
				"org.haengbokhan.model.Article@getArticles()", Article.class)
				.getResultList();

		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#getEnabledArticles(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public List<Article> getEnabledArticles(Integer studyRoomId) {
		// TODO Auto-generated method stub
		List<Article> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.Article@getEnabledArticles(studyRooomId)",
						Article.class).setParameter("studyRoomId", studyRoomId)
				.getResultList();
		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#getArticleReply(java.lang.Integer)
	 */
	public ArticleReply getArticleReply(Integer articleReplyId) {
		// TODO Auto-generated method stub
		return getEntityManager().find(ArticleReply.class, articleReplyId);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#createArticleReply(org.haengbokhan.model.ArticleReply)
	 */
	public void createArticleReply(ArticleReply reply) {
		reply.setCreatedDate(new Date());
		getEntityManager().merge(reply);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#updateArticleReply(org.haengbokhan.model.ArticleReply)
	 */
	public void updateArticleReply(ArticleReply reply) {
		reply.setModifiedDate(new Date());
		getEntityManager().merge(reply);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#deleteArticleReply(org.haengbokhan.model.ArticleReply)
	 */
	public void deleteArticleReply(ArticleReply reply) {
		reply.setDeletedDate(new Date());
		reply.setEnabled(false);
		getEntityManager().merge(reply);
	}

	/**
	 * @see org.haengbokhan.service.ArticleManager#getEnabledArticleReplies(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public List<ArticleReply> getEnabledArticleReplies(Integer articleId) {
		// TODO Auto-generated method stub
		List<ArticleReply> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.ArticleReply@getArticleReplies(articleId)",
						ArticleReply.class)
				.setParameter("articleId", articleId).getResultList();
		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}
}