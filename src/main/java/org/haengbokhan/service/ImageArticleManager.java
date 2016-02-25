package org.haengbokhan.service;

import java.util.List;

import org.haengbokhan.model.ImageArticle;
import org.haengbokhan.model.ImageArticleReply;

public interface ImageArticleManager {
	public ImageArticle getImageArticle(Integer imageArticleId);

	public void deleteImageArticle(ImageArticle imageArticle);

	public void updateImageArticle(ImageArticle imageArticle);

	public void createImageArticle(ImageArticle imageArticle);

	public List<ImageArticle> getAllImageArticles();

	public List<ImageArticle> getImageArticles(String groupId);

	ImageArticleReply getImageArticleReply(Integer imageArticleReplyId);

	void createImageArticleReply(ImageArticleReply reply);

	void updateImageArticleReply(ImageArticleReply reply);

	void deleteImageArticleReply(ImageArticleReply reply);

	List<ImageArticleReply> getImageArticleReplies(Integer imageArticleId);
}
