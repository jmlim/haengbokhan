package org.haengbokhan.service;

import java.util.List;

import org.haengbokhan.model.Image;
import org.haengbokhan.model.ImageReply;

public interface ImageManager {

	public Image getImage(Integer imageId);

	public void deleteImage(Image image);

	public void updateImage(Image image);

	public void createImage(Image image);

	public List<Image> getAllImages();

	public List<Image> getImages(String groupId);

	ImageReply getImageReply(Integer imageReplyId);

	void createImageReply(ImageReply reply);

	void updateImageReply(ImageReply reply);

	void deleteImageReply(ImageReply reply);

	List<ImageReply> getImageReplies(Integer imageId);
}
