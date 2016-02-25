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
import org.haengbokhan.model.ImageArticle;
import org.haengbokhan.model.ImageArticleReply;
import org.haengbokhan.model.User;
import org.haengbokhan.service.ImageArticleManager;
import org.haengbokhan.service.ImageManager;
import org.haengbokhan.service.UserManager;
import org.haengbokhan.web.validator.ImageArticleReplyValidator;
import org.haengbokhan.web.validator.ImageArticleValidator;
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
@SessionAttributes({ "imageArticle" })
public class ImageArticleController {

	private final String FILE_SEPARATOR = System.getProperty("file.separator");

	private Log log = LogFactory.getLog(getClass());

	private ImageArticleValidator imageArticleValidator = new ImageArticleValidator();

	private ImageArticleReplyValidator replyValidator = new ImageArticleReplyValidator();

	@Autowired
	private ImageArticleManager imageArticleManager;

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
	@RequestMapping(value = { "/image-article/list" }, method = RequestMethod.GET)
	public String imageArticles(
			Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "listCount", defaultValue = "12") int listCount,
			@RequestParam(value = "groupId") String groupId) {

		// 컬럼 4개로 고정.
		int colCount = 4;

		int startNum = page * listCount;
		// 수정필요 (전체 이미지 카운트 추가 와 현재 page 의 이미지들만 가져올 수 있는 함수 추가.).
		List<ImageArticle> imageArticles = imageArticleManager
				.getImageArticles(groupId);

		if (imageArticles == null) {
			imageArticles = new ArrayList<ImageArticle>();
		}

		int imageArticlesSize = imageArticles.size();

		int toIndex = startNum + listCount;
		if (imageArticlesSize < toIndex) {
			toIndex = imageArticlesSize;
		}

		List<ImageArticle> currentImageArticles = imageArticles.subList(
				startNum, toIndex);
		List<List<ImageArticle>> imageArticlesRows = new ArrayList<List<ImageArticle>>();
		for (int imageArticleIndex = 0, divideNum = 0, currentImageArticlesSize = currentImageArticles
				.size(); imageArticleIndex <= currentImageArticlesSize; ++imageArticleIndex) {
			if (imageArticleIndex % colCount == 0) {
				imageArticlesRows.add(currentImageArticles.subList(divideNum,
						imageArticleIndex));
				divideNum = imageArticleIndex;
			}

			if (imageArticleIndex % colCount != 0
					&& imageArticleIndex == currentImageArticlesSize) {
				imageArticlesRows.add(currentImageArticles.subList(divideNum,
						imageArticleIndex));
			}
		}

		// list 의 페이지 갯수는 10개로 제한.
		int pageCount = (imageArticlesSize - 1) / listCount;
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
		model.addAttribute("imageArticlesRows", imageArticlesRows);
		model.addAttribute("groupId", groupId);

		return "/pages/image-article/list";
	}

	/**
	 * @param model
	 * @param session
	 * @param imageArticleId
	 * @param groupId
	 * @return
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/image-article/edit-form", method = RequestMethod.GET)
	public String imageArticleEditForm(
			Model model,
			HttpSession session,
			@RequestParam(value = "imageArticleId", required = false) Integer imageArticleId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		if (imageArticleId != null) {
			ImageArticle imageArticle = imageArticleManager
					.getImageArticle(imageArticleId);
			model.addAttribute("imageArticle", imageArticle);
		} else {
			model.addAttribute("imageArticle", new ImageArticle());
		}

		model.addAttribute("groupId", groupId);
		return "/pages/image-article/edit-form";
	}

	@RequestMapping(value = "/image-article/content", method = RequestMethod.GET)
	public String getImageArticleContent(Model model,
			@RequestParam(value = "imageArticleId") Integer imageArticleId,
			@RequestParam(value = "groupId") String groupId) {

		ImageArticle imageArticle = imageArticleManager
				.getImageArticle(imageArticleId);

		model.addAttribute("groupId", groupId);
		model.addAttribute("imageArticle", imageArticle);
		model.addAttribute("imageArticleReply", new ImageArticleReply());

		List<ImageArticleReply> imageArticleReplies = imageArticleManager
				.getImageArticleReplies(imageArticle.getId());

		model.addAttribute("imageArticleReplies", imageArticleReplies);

		return "/pages/image-article/content";
	}

	/**
	 * @param model
	 * @param imageArticle
	 * @param session
	 * @param status
	 * @param result
	 * @param groupId
	 * @return
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/image-article/edit-form-submit", method = RequestMethod.POST)
	public String imageArticleEditSubmit(Model model,
			@ModelAttribute ImageArticle imageArticle, HttpSession session,
			SessionStatus status, BindingResult result,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		/*
		 * if (!currentUserImageArticleAccessControlCheck(imageArticle,
		 * currentUser)) { throw new UserNotAutorityException(); }
		 */

		imageArticleValidator.validate(imageArticle, result);
		if (result.hasErrors()) {
			model.addAttribute("groupId", groupId);
			return "/pages/image-article/edit-form";
		}

		imageArticle.setOwner(currentUser);
		imageArticle.setGroupId(groupId);

		if (imageArticle.getId() == null) {
			imageArticleManager.createImageArticle(imageArticle);
			status.setComplete();
			return "redirect:/image-article/list?groupId=" + groupId;
		} else {
			imageArticleManager.updateImageArticle(imageArticle);
			status.setComplete();
			return "redirect:/image-article/content?imageArticleId="
					+ imageArticle.getId() + "&groupId=" + groupId;
		}
	}

	@RequestMapping(value = "/image-article-reply/edit-form-submit", method = RequestMethod.POST)
	public String imageArticleReplyEditSubmit(
			Model model,
			HttpSession session,
			@ModelAttribute("imageArticleReply") ImageArticleReply reply,
			BindingResult result,
			SessionStatus status,
			@RequestParam(value = "imageArticleId", required = false) Integer imageArticleId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			throw new UserNotFoundException();
		}

		ImageArticle imageArticle = imageArticleManager
				.getImageArticle(imageArticleId);

		replyValidator.validate(reply, result);
		if (result.hasErrors()) {
			List<ImageArticleReply> imageArticleReplies = imageArticleManager
					.getImageArticleReplies(imageArticle.getId());

			model.addAttribute("imageArticleReplies", imageArticleReplies);
			model.addAttribute("groupId", groupId);

			return "/image-article/content";
		}

		reply.setImageArticle(imageArticle);
		reply.setOwner(currentUser);
		if (reply.getId() == null) {
			imageArticleManager.createImageArticleReply(reply);
		} else {
			imageArticleManager.updateImageArticleReply(reply);
		}

		status.setComplete();

		return "redirect:/image-article/content?imageArticleId="
				+ imageArticleId + "&groupId=" + groupId;
	}

	@RequestMapping(value = "/image-article-reply/delete", method = RequestMethod.GET)
	public String imageArticleReplyDelete(Model model, HttpSession session,
			@RequestParam("imageArticleReplyId") Integer imageArticleReplyId,
			@RequestParam("imageArticleId") Integer imageArticleId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotAutorityException, UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		ImageArticleReply reply = imageArticleManager
				.getImageArticleReply(imageArticleReplyId);
		if (!currentUserImageArticleReplyAccessControlCheck(reply, currentUser)) {
			throw new UserNotAutorityException();
		}

		imageArticleManager.deleteImageArticleReply(reply);

		return "redirect:/image-article/content?imageArticleId="
				+ imageArticleId + "&groupId=" + groupId;
	}

	protected boolean currentUserImageArticleAccessControlCheck(
			ImageArticle imageArticle, User currentUser) {
		Integer imageArticleId = imageArticle.getId();
		if (imageArticleId != null) {
			ImageArticle previousImageArticle = imageArticleManager
					.getImageArticle(imageArticleId);
			User imageArticleOwner = previousImageArticle.getOwner();
			if (imageArticleOwner != null && currentUser != null) {
				String imageArticleOwnerUId = imageArticleOwner.getUid();
				String currentUserUId = currentUser.getUid();
				if (!imageArticleOwnerUId.equals(currentUserUId)) {
					// TODO
					return false;
				}
			}
		}
		return true;
	}

	protected boolean currentUserImageArticleReplyAccessControlCheck(
			ImageArticleReply reply, User currentUser) {
		Integer replyId = reply.getId();
		if (replyId != null) {
			ImageArticleReply previousImageArticleReply = imageArticleManager
					.getImageArticleReply(replyId);
			User replyOwner = previousImageArticleReply.getOwner();
			if (replyOwner != null && currentUser != null) {
				String imageArticleOwnerUId = replyOwner.getUid();
				String currentUserUId = currentUser.getUid();
				if (!imageArticleOwnerUId.equals(currentUserUId)) {
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
	@RequestMapping(value = "/image-article/upload", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	Object uploadHandler(Model model,
			@ModelAttribute ImageArticle imageArticle,
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "groupId") String groupId,
			HttpSession httpSession, HttpServletResponse response)
			throws IllegalStateException, IOException {
		String originalFilename = file.getOriginalFilename();
		String storeFileName = getStoreFileName(originalFilename);
		String fileStorePath = getFileStorePath();

		file.transferTo(new File(fileStorePath + storeFileName));

		ImageArticle createdImageArticle = new ImageArticle();

		Image image = new Image();
		image.setRealName(originalFilename);
		image.setName(storeFileName);
		image.setSize(file.getSize());
		image.setPath(fileStorePath);
		image.setContentType(file.getContentType());
		createdImageArticle.setGroupId(groupId);
		createdImageArticle.setTitle(imageArticle.getTitle());
		createdImageArticle.setContent(imageArticle.getContent());

		response.setContentType("application/json");

		User owner = (User) httpSession.getAttribute("user");
		createdImageArticle.setOwner(owner);
		image.setOwner(owner);
		imageManager.createImage(image);
		createdImageArticle.setImage(image);

		imageArticleManager.createImageArticle(createdImageArticle);

		image.setUrl(getImageArticleUrl(createdImageArticle));
		imageManager.updateImage(image);
		imageArticleManager.updateImageArticle(createdImageArticle);

		model.addAttribute("imageArticle", createdImageArticle);
		httpSession.setAttribute("imageArticle", createdImageArticle);
		model.addAttribute("image", image);

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
	 * @param imageArticleId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/image-article/uploaded", method = RequestMethod.GET)
	@ResponseBody
	public void imageArticleShowHandler(
			@RequestParam("imageArticleId") Integer imageArticleId,
			HttpServletResponse response) throws IOException {
		ImageArticle imageArticle = imageArticleManager
				.getImageArticle(imageArticleId);
		Image image = imageArticle.getImage();
		String path = image.getPath() + image.getName();
		InputStream in = new FileInputStream(path);
		response.setContentType(image.getContentType());
		IOUtils.copyLarge(in, response.getOutputStream());
	}

	protected String getImageArticleUrl(ImageArticle imageArticle) {
		StringBuilder sb = new StringBuilder();
		sb.append("/haengbokhan/image-article/uploaded/");
		sb.append("?");
		sb.append("imageArticleId");
		sb.append("=");
		sb.append(imageArticle.getId());
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
