package edu.idol.mca.bcs.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.idol.mca.bcs.service.UserService;
import edu.idol.mca.bcs.serviceImpl.MapValidationErrorService;
import edu.idol.mca.bcs.domain.User;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MapValidationErrorService errorService;
	
	/* |---------------------------------------------| USER CRUD OPERATIONS |----------------------------------------------------------| */


	/**
	 * Method for Registration of new user and storing data to database.
	 * @param user Data collected to save
	 * @param result contains the result and errors of validation
	 * @return saved user in database
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
		ResponseEntity<?> errorMap = errorService.mapValidationError(result);
		if(errorMap!=null) return errorMap;
		User savedUser= userService.saveUser(user);

		//return new ResponseEntity<User>(savedUser,HttpStatus.OK);
		return new ResponseEntity<String>("Registration Successful",HttpStatus.OK);
	}
	
	
	/**
	 * Method for updating user in database.
	 * @param user Data collected to update
	 * @param result contains the result and errors of validation
	 * @return saved user in database
	 */
	@PatchMapping("/update")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user,HttpSession session, BindingResult result){
		ResponseEntity<?> errorMap = errorService.mapValidationError(result);
		if(errorMap!=null) return errorMap;		
		if(session.getAttribute("loginName")!=null && session.getAttribute("loginName").equals(user.getLoginName())) {
			User savedUser= userService.updateUser(user);
			return new ResponseEntity<User>(savedUser,HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have access !!!",HttpStatus.UNAUTHORIZED);
		
	}
	
	/**
	 * Method to delete user by loginName
	 * 
	 * @param loginName of the user
	 */

	@DeleteMapping("/{loginName}")
	public ResponseEntity<?> deleteUser(@PathVariable String loginName, HttpSession session) {
		if (session.getAttribute("loginName") != null && session.getAttribute("loginName").equals(loginName)) {
		
		userService.deleteUserByLoginName(loginName);
		return new ResponseEntity<String>("User with loginName :" + loginName + " is deleted", HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
		//return new ResponseEntity<String>(loginName, HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/{loginName}")
	public ResponseEntity<?> getUser(@PathVariable String loginName, HttpSession session) {
		if (session.getAttribute("loginName") != null && session.getAttribute("loginName").equals(loginName)) {

			User user = userService.findUserByLoginName(loginName);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/all")
	public ResponseEntity<?> getUsers(HttpSession session) {
		if (session.getAttribute("loginName") != null) {
			List<User> user = userService.findAll();
			return new ResponseEntity<List<User>>(user, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}
	
/* |-------------------------------------------------| LOGIN AUTHENTICATION |--------------------------------------------------------------| */
	
	/**
		 * Method for handling user login and creating session.
		 * 
		 * @param user
		 * @param result       contains the result and error of validation
		 * @param session      Creates New Session
		 * @return Response Entity with logged In user with HTTP Status
		 */
		@PostMapping("/login")
		public ResponseEntity<?> handleUserLogin(@RequestBody User user, BindingResult result,
				HttpSession session) {
			ResponseEntity<?> errorMap = errorService.mapValidationError(result);
			if (errorMap != null) {
				return errorMap;
			}
			User loggedInOwner = userService.authenticateUser(user.getLoginName(),
					user.getPwd(), session);
			//return new ResponseEntity<User>(loggedInUser, HttpStatus.OK);
			return new ResponseEntity<String>("Login Successful", HttpStatus.OK);
		}


	/**
	 * Method for logging out User and terminating existing session.
	 * @param session get current session details
	 * @return Logout Success message
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> handleUserLogout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<String>("Logout Successful", HttpStatus.OK);
	}
	
	/**
	 * Method for getting Session Information.
	 * 
	 * @param session get current session details
	 * @return Logout Success message
	 */

	@GetMapping("/getSession")
	public ResponseEntity<?> getUserSesssion(HttpSession session) {
		
		HashMap<String, String> sessionMap = new HashMap<>();
		if(session.getAttribute("loginName") != null) {
			sessionMap.put("name", (String) session.getAttribute("name"));
			sessionMap.put("loginName", (String) session.getAttribute("loginName"));
			return new ResponseEntity<HashMap<String, String>>(sessionMap, HttpStatus.OK);	
		}
		else {
			sessionMap.put("name", "notLoggedIn");
			sessionMap.put("loginName","notLoggedIn");
			return new ResponseEntity<HashMap<String, String>>(sessionMap, HttpStatus.OK);
		}

	}

}
