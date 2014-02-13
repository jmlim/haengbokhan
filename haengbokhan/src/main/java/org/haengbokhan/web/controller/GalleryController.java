package org.haengbokhan.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.haengbokhan.exception.UserNotAutorityException;
import org.haengbokhan.exception.UserNotFoundException;
import org.haengbokhan.model.Image;
import org.haengbokhan.model.ImageReply;
import org.haengbokhan.model.User;
import org.haengbokhan.service.ImageManager;
import org.haengbokhan.service.UserManager;
import org.haengbokhan.web.validator.ImageReplyValidator;
import org.haengbokhan.web.validator.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * 
 */
@Controller
// @SessionAttributes(value = { "user" }, types = { User.class })
@SessionAttributes({ "image" })
public class GalleryController {

	private final String FILE_SEPARATOR = System.getProperty("file.separator");

	private Log log = LogFactory.getLog(getClass());

	private ImageValidator imageValidator = new ImageValidator();

	private ImageReplyValidator replyValidator = new ImageReplyValidator();

	@Autowired
	private ImageManager imageManager;

	@Autowired
	private UserManager userManager;

	/**
	 * @param model
	 * @param page
	 * @param offset
	 * @param listCount
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = { "/gallery/gallery" }, method = RequestMethod.GET)
	public String gallery(
			Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "listCount", defaultValue = "12") int listCount,
			@RequestParam(value = "groupId") String groupId) {

		// 컬럼 4개로 고정.
		int colCount = 4;

		int startNum = page * listCount;
		// 수정필요 (전체 이미지 카운트 추가 와 현재 page 의 이미지들만 가져올 수 있는 함수 추가.).
		List<Image> images = imageManager.getImages(groupId);

		if (images == null) {
			images = new ArrayList<Image>();
		}

		int imagesSize = images.size();

		int toIndex = startNum + listCount;
		if (imagesSize < toIndex) {
			toIndex = imagesSize;
		}

		List<Image> currentImages = images.subList(startNum, toIndex);
		List<List<Image>> imagesRows = new ArrayList<List<Image>>();
		for (int imageIndex = 0, divideNum = 0, currentImagesSize = currentImages
				.size(); imageIndex <= currentImagesSize; ++imageIndex) {
			if (imageIndex % colCount == 0) {
				imagesRows.add(currentImages.subList(divideNum, imageIndex));
				divideNum = imageIndex;
			}

			if (imageIndex % colCount != 0 && imageIndex == currentImagesSize) {
				imagesRows.add(currentImages.subList(divideNum, imageIndex));
			}
		}

		// list 의 페이지 갯수는 10개로 제한.
		int pageCount = (imagesSize - 1) / listCount;
		int maxOffset = pageCount / 10;

		if (offset < 0) {
			offset = 0;
		}

		if (maxOffset < offset) {
			offset = maxOffset;
		}

		int startCount = (offset + 0) * 10;

		model.addAttribute("maxOffset", maxOffset);
		if ((offset + 1) * 9 < pageCount) {
			pageCount = (offset + 1) * 9;
		}

		model.addAttribute("startCount", startCount);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("offset", offset);
		model.addAttribute("imagesRows", imagesRows);
		model.addAttribute("groupId", groupId);

		return "/pages/gallery/gallery";
	}

	/**
	 * @param model
	 * @param session
	 * @param imageId
	 * @param groupId
	 * @return
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/gallery/edit-form", method = RequestMethod.GET)
	public String galleryEditForm(Model model, HttpSession session,
			@RequestParam(value = "imageId", required = false) Integer imageId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		if (imageId != null) {
			Image image = imageManager.getImage(imageId);
			model.addAttribute("image", image);
		} else {
			model.addAttribute("image", new Image());
		}

		model.addAttribute("groupId", groupId);
		return "/pages/gallery/edit-form";
	}

	@RequestMapping(value = "/gallery/content", method = RequestMethod.GET)
	public String getImageContent(Model model,
			@RequestParam(value = "imageId") Integer imageId,
			@RequestParam(value = "groupId") String groupId) {

		Image image = imageManager.getImage(imageId);

		model.addAttribute("groupId", groupId);
		model.addAttribute("image", image);
		model.addAttribute("imageReply", new ImageReply());

		List<ImageReply> imageReplies = imageManager.getImageReplies(image
				.getId());

		model.addAttribute("imageReplies", imageReplies);

		return "/pages/gallery/content";
	}

	/**
	 * @param model
	 * @param image
	 * @param session
	 * @param status
	 * @param result
	 * @param groupId
	 * @return
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/gallery/edit-form-submit", method = RequestMethod.POST)
	public String galleryEditSubmit(Model model, @ModelAttribute Image image,
			HttpSession session, SessionStatus status, BindingResult result,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		/*
		 * if (!currentUserImageAccessControlCheck(image, currentUser)) { throw
		 * new UserNotAutorityException(); }
		 */

		imageValidator.validate(image, result);
		if (result.hasErrors()) {
			model.addAttribute("groupId", groupId);
			return "/pages/gallery/edit-form";
		}

		image.setOwner(currentUser);
		image.setGroupId(groupId);

		if (image.getId() == null) {
			imageManager.createImage(image);
			status.setComplete();
			return "redirect:/gallery/gallery?groupId=" + groupId;
		} else {
			imageManager.updateImage(image);
			status.setComplete();
			return "redirect:/gallery/content?imageId=" + image.getId()
					+ "&groupId=" + groupId;
		}
	}

	@RequestMapping(value = "/gallery-reply/edit-form-submit", method = RequestMethod.POST)
	public String galleryReplyEditSubmit(Model model, HttpSession session,
			@ModelAttribute("imageReply") ImageReply reply,
			BindingResult result, SessionStatus status,
			@RequestParam(value = "imageId", required = false) Integer imageId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			throw new UserNotFoundException();
		}

		Image image = imageManager.getImage(imageId);

		replyValidator.validate(reply, result);
		if (result.hasErrors()) {
			List<ImageReply> imageReplies = imageManager.getImageReplies(image
					.getId());

			model.addAttribute("imageReplies", imageReplies);
			model.addAttribute("groupId", groupId);

			return "/gallery/content";
		}

		reply.setImage(image);
		reply.setOwner(currentUser);
		if (reply.getId() == null) {
			imageManager.createImageReply(reply);
		} else {
			imageManager.updateImageReply(reply);
		}

		status.setComplete();

		return "redirect:/image/content?imageId=" + imageId + "&groupId="
				+ groupId;
	}

	@RequestMapping(value = "/image-reply/delete", method = RequestMethod.GET)
	public String imageReplyDelete(Model model, HttpSession session,
			@RequestParam("imageReplyId") Integer imageReplyId,
			@RequestParam("imageId") Integer imageId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotAutorityException, UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		ImageReply reply = imageManager.getImageReply(imageReplyId);
		if (!currentUserImageReplyAccessControlCheck(reply, currentUser)) {
			throw new UserNotAutorityException();
		}

		imageManager.deleteImageReply(reply);

		return "redirect:/image/content?imageId=" + imageId + "&groupId="
				+ groupId;
	}

	protected boolean currentUserImageAccessControlCheck(Image image,
			User currentUser) {
		Integer imageId = image.getId();
		if (imageId != null) {
			Image previousImage = imageManager.getImage(imageId);
			User imageOwner = previousImage.getOwner();
			if (imageOwner != null && currentUser != null) {
				String imageOwnerUId = imageOwner.getUid();
				String currentUserUId = currentUser.getUid();
				if (!imageOwnerUId.equals(currentUserUId)) {
					// TODO
					return false;
				}
			}
		}
		return true;
	}

	protected boolean currentUserImageReplyAccessControlCheck(ImageReply reply,
			User currentUser) {
		Integer replyId = reply.getId();
		if (replyId != null) {
			ImageReply previousImageReply = imageManager.getImageReply(replyId);
			User replyOwner = previousImageReply.getOwner();
			if (replyOwner != null && currentUser != null) {
				String imageOwnerUId = replyOwner.getUid();
				String currentUserUId = currentUser.getUid();
				if (!imageOwnerUId.equals(currentUserUId)) {
					// TODO
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param model
	 * @param file
	 * @param httpSession
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Object uploadHandler(Model model, @RequestParam("file") MultipartFile file,
			@RequestParam(value = "groupId") String groupId,
			HttpSession httpSession, HttpServletResponse response)
			throws IllegalStateException, IOException {
		String originalFilename = file.getOriginalFilename();
		String storeFileName = getStoreFileName(originalFilename);
		String fileStorePath = getFileStorePath();

		file.transferTo(new File(fileStorePath + storeFileName));

		Image image = new Image();
		image.setRealName(originalFilename);
		image.setName(storeFileName);
		image.setSize(file.getSize());
		image.setPath(fileStorePath);
		image.setContentType(file.getContentType());
		image.setGroupId(groupId);

		response.setContentType("application/json");

		User owner = (User) httpSession.getAttribute("user");
		image.setOwner(owner);
		imageManager.createImage(image);

		image.setUrl(getImageUrl(image));
		imageManager.updateImage(image);

		model.addAttribute("image", image);
		httpSession.setAttribute("image", image);

		List<Map<String, Object>> files = new ArrayList<Map<String, Object>>();

		Map<String, Object> fileInfo = new HashMap<String, Object>();

		fileInfo.put("name", image.getRealName());
		fileInfo.put("size", image.getSize());
		fileInfo.put("url", image.getUrl());
		fileInfo.put("thumbnail_url", image.getUrl());
		files.add(fileInfo);

		Map<String, List<Map<String, Object>>> info = new HashMap<String, List<Map<String, Object>>>();
		info.put("files", files);
		return info;
	}

	/**
	 * @param imageId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/image/uploaded", method = RequestMethod.GET)
	@ResponseBody
	public void imageShowHandler(@RequestParam("imageId") Integer imageId,
			HttpServletResponse response) throws IOException {
		Image image = imageManager.getImage(imageId);
		String path = image.getPath() + image.getName();
		InputStream in = new FileInputStream(path);
		response.setContentType(image.getContentType());
		IOUtils.copyLarge(in, response.getOutputStream());
	}

	protected String getImageUrl(Image image) {
		StringBuilder sb = new StringBuilder();
		sb.append("/haengbokhan/image/uploaded/");
		sb.append("?");
		sb.append("imageId");
		sb.append("=");
		sb.append(image.getId());
		return sb.toString();
	}

	/**
	 * @return
	 */
	protected String getFileStorePath() {
		String espHomePath = System.getProperty("haengbokhan.home");
		StringBuilder filePath = new StringBuilder();
		filePath.append(espHomePath);
		filePath.append("upload");
		filePath.append(FILE_SEPARATOR);
		filePath.append("images");
		filePath.append(FILE_SEPARATOR);
		return filePath.toString();
	}

	/**
	 * @param originalFilename
	 * @return
	 */
	protected String getStoreFileName(String originalFilename) {
		if (StringUtils.isNotBlank(originalFilename)) {
			String fileType = StringUtils.substringAfterLast(originalFilename,
					".");
			String fileName = StringUtils.substringBeforeLast(originalFilename,
					".");
			StringBuilder storeFile = new StringBuilder();
			storeFile.append(fileName);
			storeFile.append("_");
			storeFile.append(System.currentTimeMillis());
			storeFile.append(".");
			storeFile.append(fileType);
			return storeFile.toString();
		}
		return null;
	}

}
