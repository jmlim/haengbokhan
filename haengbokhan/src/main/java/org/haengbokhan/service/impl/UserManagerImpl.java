package org.haengbokhan.service.impl;

import java.util.Date;
import java.util.List;

import org.haengbokhan.model.User;
import org.haengbokhan.service.AbstractJpaDaoService;
import org.haengbokhan.service.UserManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Hana Lee
 * @since 0.0.2 2013. 1. 21. 오전 7:16:00
 * @revision $LastChangedRevision: 6070 $
 * @date $LastChangedDate: 2013-02-16 12:31:02 +0900 (토, 16 2월 2013) $
 * @by $LastChangedBy: jmlim $
 */
@Service("userManager")
@Transactional
public class UserManagerImpl extends AbstractJpaDaoService implements
		UserManager, UserDetailsService {

	/**
	 * @see org.haengbokhan.service.UserManager#getUser(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public User getUser(Integer id) {

		List<User> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.User@getUser():param.userId",
						User.class).setParameter("userId", id).getResultList();

		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * @see org.haengbokhan.service.UserManager#getUser(java.lang.String)
	 */
	@Transactional(readOnly = true)
	public User getUser(String userId) {

		List<User> results = getEntityManager()
				.createNamedQuery(
						"org.haengbokhan.model.User@getUser():param.uId",
						User.class).setParameter("uId", userId).getResultList();

		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * @see org.haengbokhan.service.UserManager#createUser(org.haengbokhan.model.User)
	 */
	public void createUser(User user) {
		user.setCreatedDate(new Date());
		if (user.getRole() == null) {
			user.setRole("ROLE_USER");
		}
		getEntityManager().persist(user);
	}

	/**
	 * @see org.haengbokhan.service.UserManager#updateUser(org.haengbokhan.model.User)
	 */
	public void updateUser(User user) {
		user.setModifiedDate(new Date());
		getEntityManager().merge(user);
	}

	/**
	 * @see org.haengbokhan.service.UserManager#deleteUser(org.haengbokhan.model.User)
	 */
	public void deleteUser(User user) {
		getEntityManager().remove(user);
	}

	/**
	 * @see org.haengbokhan.service.UserManager#getUsers()
	 */
	@Transactional(readOnly = true)
	public List<User> getUsers() {

		List<User> results = getEntityManager().createNamedQuery(
				"org.haengbokhan.model.User@getUser()", User.class)
				.getResultList();
		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return null;
	}
}
