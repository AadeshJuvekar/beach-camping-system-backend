package edu.idol.mca.bcs.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.idol.mca.bcs.domain.User;
import edu.idol.mca.bcs.exception.UserAlreadyExistException;
import edu.idol.mca.bcs.exception.UserNotFoundException;
import edu.idol.mca.bcs.repository.UserRepository;
import edu.idol.mca.bcs.service.UserService;
import edu.idol.mca.bcs.exception.LoginException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	//-------------------------------------------------------------- USER CRUD OPERATIONS -------------------------------------------------------------------
		@Override
		public User saveUser(User user) {
			
			//Check for null values
			if(user.getLoginName()==null|| user.getPwd()==null||user.getFirstName()==null) {
				throw new NullPointerException("Please fill the required fields");
			}
			
			//Check if user already exists
			if(userRepository.findByLoginName(user.getLoginName())!=null) {
				throw new UserAlreadyExistException("User with "+ user.getLoginName() +" already exists");
			}
			
			//Register New User
			return userRepository.save(user);
		}

		@Override
		public User updateUser(User user) {	
			User oldUser =null;
			//Check for Null Values
			if (user.getLoginName() == null) {
				throw new NullPointerException("Please Fill the Required Fields");
			}
			// Check if user exists
			if ((oldUser=userRepository.findByLoginName(user.getLoginName())) == null) {
				throw new UserNotFoundException("User with loginName : " + user.getLoginName() + " does not exists");
			}
			// update user object
			user.setId(oldUser.getId());
			oldUser = user;
			return userRepository.save(oldUser);
		}

		@Override
		public void deleteUserByLoginName(String loginName) {
			User user = null;
			//Check for Null Values
			if (loginName == null) {
				throw new NullPointerException("Please Provide Login Name");
			}
			//Check if User exists
			if ((user = userRepository.findByLoginName(loginName)) == null) {
				throw new UserNotFoundException("User with loginName : " + loginName + " does not exists");
			}
			//Delete User
			userRepository.delete(user);

		}

		@Override
		public List<User> findAll() {		
			try {
				return userRepository.findAll();
			} catch (Exception e) {
				throw new UserNotFoundException("No User Found");
			}
		}

		@Override
		public User findUserByLoginName(String loginName) {
			try {
				return userRepository.findByLoginName(loginName);
			} catch (Exception e) {
				throw new UserNotFoundException("User with loginName : " + loginName + " does not exist");
			}
		}
		
		//----------------------------------------------------------- USER LOGIN -------------------------------------------------------------------------------
		@Override
		public User authenticateUser(String loginName, String pwd, HttpSession session) {	
			User user = null;
			//Check for null values
			if (loginName == null || pwd == null) {
				throw new LoginException("Please Enter Credentials");
			}
			//Check if User exists
			if ((user = userRepository.findByLoginName(loginName)) == null) {
				throw new UserNotFoundException("User with loginName : " + loginName + " does not exist");
			}
			//Check for password
			if (user.getPwd().equals(pwd)) {
				addUserInSession(user, session);
				return user;
			}else {
				throw new LoginException("Invalid Credentials");
			}
		}

		private User addUserInSession(User user, HttpSession session) {
			session.setAttribute("userId", user.getId());
			session.setAttribute("userName", user.getFirstName());
			session.setAttribute("loginName", user.getLoginName());	
			return user;
		}

}
