package edu.idol.mca.bcs.service;


import java.util.List;

import javax.servlet.http.HttpSession;

import edu.idol.mca.bcs.domain.User;

public interface UserService {
	
	/**
	 * This saveUser method will register user in system.
	 * @param user to be registered
	 * @return Registered user if successfully registered otherwise null
	 */
	public User saveUser(User user);
	/**
	 * This updateUser method will update user in system.
	 * @param user to be updated
	 * @return Registered user if successfully updated 
	 */
	public User updateUser(User user);
	/**
	 * This method is used for deleting user details in system.
	 * @param loginName of the user
	 */
	public void deleteUserByLoginName(String loginName);
	/**
	 * This method return list of all users in system.
	 * @return list of users.
	 */
	public List<User> findAll();
	/**
	 * This method is used to find user in database.
	 * @param loginName of the user to be found
	 * @return user if found otherwise null
	 */
	public User findUserByLoginName(String loginName);
	/**
	 * This method is used for authentication and login of user
	 * @param loginName of the user
	 * @param pwd of the user
	 * @param session created for login
	 * @return logged in user
	 */
	public User authenticateUser(String loginName, String pwd, HttpSession session);
}
