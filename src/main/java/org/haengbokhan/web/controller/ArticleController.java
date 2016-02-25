package org.haengbokhan.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.haengbokhan.exception.ArticleNotFoundException;
import org.haengbokhan.exception.RoomNotFoundException;
import org.haengbokhan.exception.UserNotAutorityException;
import org.haengbokhan.exception.UserNotFoundException;
import org.haengbokhan.model.Article;
import org.haengbokhan.model.ArticleReply;
import org.haengbokhan.model.User;
import org.haengbokhan.service.ArticleManager;
import org.haengbokhan.service.UserManager;
import org.haengbokhan.web.validator.ArticleReplyValidator;
import org.haengbokhan.web.validator.ArticleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * @author Administrator
 * 
 */
@Controller
@SessionAttributes({ "article", "articleReply" })
public class ArticleController {

	private Validator articleValidator = new ArticleValidator();

	private Validator replyValidator = new ArticleReplyValidator();

	@Autowired
	private ArticleManager articleManager;

	@Autowired
	private UserManager userManager;

	@RequestMapping(value = "/article/edit-form", method = RequestMethod.GET)
	public String articleEditForm(
			Model model,
			HttpSession session,
			@RequestParam(value = "articleId", required = false) Integer articleId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException, RoomNotFoundException {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		if (articleId != null) {
			Article article = articleManager.getArticle(articleId);
			model.addAttribute("article", article);
		} else {
			model.addAttribute("article", new Article());
		}

		model.addAttribute("groupId", groupId);
		return "/pages/article/edit-form";
	}

	@RequestMapping(value = "/article/edit-form-submit", method = RequestMethod.POST)
	public String articleEditSubmit(Model model,
			@ModelAttribute Article article, HttpSession session,
			SessionStatus status, BindingResult result,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotFoundException, RoomNotFoundException,
			UserNotAutorityException {

		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		if (!currentUserArticleAccessControlCheck(article, currentUser)) {
			throw new UserNotAutorityException();
		}

		articleValidator.validate(article, result);
		if (result.hasErrors()) {
			model.addAttribute("groupId", groupId);
			return "/pages/article/edit-form";
		}

		article.setOwner(currentUser);
		article.setGroupId(groupId);

		if (article.getId() == null) {
			articleManager.createArticle(article);
			status.setComplete();
			return "redirect:/article/list?groupId=" + groupId;
		} else {
			articleManager.updateArticle(article);
			status.setComplete();
			return "redirect:/article/content?articleId=" + article.getId()
					+ "&groupId=" + groupId;
		}
	}

	@RequestMapping(value = "/article/delete", method = RequestMethod.GET)
	public String articleDelete(Model model, HttpSession session,
			@RequestParam("articleId") Integer articleId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotAutorityException, UserNotFoundException,
			RoomNotFoundException {
		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		Article article = articleManager.getArticle(articleId);

		if (!currentUserArticleAccessControlCheck(article, currentUser)) {
			throw new UserNotAutorityException();
		}

		articleManager.deleteArticle(article);
		return "redirect:/article/list?groupId=" + groupId;
	}

	@RequestMapping(value = "/article/content", method = RequestMethod.GET)
	public String getArticleContent(Model model,
			@RequestParam(value = "articleId") Integer articleId,
			@RequestParam(value = "groupId") String groupId)
			throws ArticleNotFoundException, RoomNotFoundException {

		Article article = articleManager.getArticle(articleId);

		if (article == null) {
			throw new ArticleNotFoundException();

		}

		model.addAttribute("groupId", groupId);
		model.addAttribute("article", article);
		model.addAttribute("articleReply", new ArticleReply());

		List<ArticleReply> articleReplies = articleManager
				.getArticleReplies(article.getId());

		model.addAttribute("articleReplies", articleReplies);

		return "/pages/article/content";
	}

	@RequestMapping(value = "/article/list", method = RequestMethod.GET)
	public String getArticles(
			Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "listCount", defaultValue = "7") int listCount,
			@RequestParam(value = "groupId") String groupId)
			throws RoomNotFoundException {

		int startNum = page * listCount;
		List<Article> articles = articleManager.getArticles(groupId);

		if (articles == null) {
			articles = new ArrayList<Article>();
		}

		int articlesSize = articles.size();

		int toIndex = startNum + listCount;
		if (articlesSize < toIndex) {
			toIndex = articlesSize;
		}
		List<Article> currentArticles = articles.subList(startNum, toIndex);

		int pageCount = (articlesSize - 1) / listCount;
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
		model.addAttribute("articles", currentArticles);
		model.addAttribute("groupId", groupId);
		// model.addAttribute("studyRoomId", studyRoomId);

		return "/pages/article/list";
	}

	@RequestMapping(value = "/article-reply/edit-form-submit", method = RequestMethod.POST)
	public String articleReplyEditSubmit(
			Model model,
			HttpSession session,
			@ModelAttribute("articleReply") ArticleReply reply,
			BindingResult result,
			SessionStatus status,
			@RequestParam(value = "articleId", required = false) Integer articleId,
			@RequestParam(value = "groupId") String groupId)
			throws ArticleNotFoundException, UserNotFoundException,
			RoomNotFoundException {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			throw new UserNotFoundException();
		}

		Article article = articleManager.getArticle(articleId);

		if (article == null) {
			throw new ArticleNotFoundException();
		}

		replyValidator.validate(reply, result);
		if (result.hasErrors()) {
			List<ArticleReply> articleReplies = articleManager
					.getArticleReplies(article.getId());

			model.addAttribute("articleReplies", articleReplies);
			model.addAttribute("groupId", groupId);

			return "/article/content";
		}

		reply.setArticle(article);
		reply.setOwner(currentUser);
		if (reply.getId() == null) {
			articleManager.createArticleReply(reply);
		} else {
			articleManager.updateArticleReply(reply);
		}

		status.setComplete();

		return "redirect:/article/content?articleId=" + articleId + "&groupId="
				+ groupId;
	}

	@RequestMapping(value = "/article-reply/delete", method = RequestMethod.GET)
	public String articleReplyDelete(Model model, HttpSession session,
			@RequestParam("articleReplyId") Integer articleReplyId,
			@RequestParam("articleId") Integer articleId,
			@RequestParam(value = "groupId") String groupId)
			throws UserNotAutorityException, UserNotFoundException {

		User currentUser = (User) session.getAttribute("user");

		if (currentUser == null || currentUser.getUid() == null) {
			throw new UserNotFoundException();
		}

		ArticleReply reply = articleManager.getArticleReply(articleReplyId);
		if (!currentUserArticleReplyAccessControlCheck(reply, currentUser)) {
			throw new UserNotAutorityException();
		}

		articleManager.deleteArticleReply(reply);

		return "redirect:/article/content?articleId=" + articleId + "&groupId="
				+ groupId;
	}

	/*
	 * @ExceptionHandler(UserNotFoundException.class)
	 * 
	 * @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	 * 
	 * @ResponseBody public String
	 * handleUserNotFoundException(UserNotFoundException ex) { return
	 * "UserNotFoundException"; }
	 */

	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFoundException(UserNotFoundException ex) {
		return "/pages/article/error-page";
	}

	@ExceptionHandler(HttpSessionRequiredException.class)
	public String handleHttpSessionRequiredException(
			HttpSessionRequiredException ex,
			@RequestParam(value = "studyRoomId") Integer studyRoomId) {
		return "redirect:/article/list?studyRoomId=" + studyRoomId;
	}

	@ExceptionHandler(ArticleNotFoundException.class)
	public String handleArticleNotFoundException(ArticleNotFoundException ex) {
		return "/pages/article/error-page";
	}

	@ExceptionHandler(RoomNotFoundException.class)
	public String handleRoomNotFoundException(RoomNotFoundException ex) {
		return "/pages/article/error-page";
	}

	@ExceptionHandler(UserNotAutorityException.class)
	public String handleUserNotAutorityException(UserNotAutorityException ex) {
		return "/pages/article/error-page";
	}

	protected boolean currentUserArticleAccessControlCheck(Article article,
			User currentUser) {
		Integer articleId = article.getId();
		if (articleId != null) {
			Article previousArticle = articleManager.getArticle(articleId);
			User articleOwner = previousArticle.getOwner();
			if (articleOwner != null && currentUser != null) {
				String articleOwnerUId = articleOwner.getUid();
				String currentUserUId = currentUser.getUid();
				if (!articleOwnerUId.equals(currentUserUId)) {
					// TODO
					return false;
				}
			}
		}
		return true;
	}

	protected boolean currentUserArticleReplyAccessControlCheck(
			ArticleReply reply, User currentUser) {
		Integer replyId = reply.getId();
		if (replyId != null) {
			ArticleReply previousArticleReply = articleManager
					.getArticleReply(replyId);
			User replyOwner = previousArticleReply.getOwner();
			if (replyOwner != null && currentUser != null) {
				String articleOwnerUId = replyOwner.getUid();
				String currentUserUId = currentUser.getUid();
				if (!articleOwnerUId.equals(currentUserUId)) {
					// TODO
					return false;
				}
			}
		}
		return true;
	}
}
