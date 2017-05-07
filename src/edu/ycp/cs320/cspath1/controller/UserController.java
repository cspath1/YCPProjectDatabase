package edu.ycp.cs320.cspath1.controller;

import edu.ycp.cs320.cspath1.user.Student;
import edu.ycp.cs320.cspath1.user.User;
import edu.ycp.cs320.cspath1.email.EmailValidator;
import edu.ycp.cs320.cspath1.enums.UserType;
import edu.ycp.cs320.cspath1.model.AccountCreationModel;
import edu.ycp.cs320.cspath1.persist.DatabaseProvider;
import edu.ycp.cs320.cspath1.persist.IDatabase;
import edu.ycp.cs320.cspath1.persist.YCPDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class UserController {
	private User user;
	private IDatabase db;
	private EmailValidator emailValidator;
	private AccountCreationModel model;
	
	public UserController() {
		DatabaseProvider.setInstance(new YCPDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User login() throws IOException, SQLException {
		user = db.findUserByUsernameAndPassword(model.getUsername(), model.getPassword());
		if (user.getUsername().equals(model.getUsername()) && user.getPassword().equals(model.getPassword())) {
			return user;
		}
		else {
			return null;
		}
		
	}
	
	public void logout() {
		//logout
	}
	public void editInfo() {
		//call specific edit method based on what they want to edit
	}
	public void verifyCred() {
		//
	}
	//should be good just need to implement
	public int createAcct() throws IOException, SQLException {
		emailValidator = new EmailValidator();
		User user = new Student();
		boolean check = emailValidator.validate(model.getEmail());
		if (check == true) {
			Integer user_id = db.insertUser(model.getUsername(), model.getPassword(), model.getEmail(), model.getUsertype(), model.getFirstName(),
					model.getLastName(), model.getMajortype(), model.getClasstype(), model.getName(), model.getAddress(), model.getContactNum());
			user = db.findUserByUserID(user_id);
			return user.getUserID();
		}
		else {
			System.out.println("That username is already in use.");
			return -1;
		}
	}
		
		
		
	
	public void proposeProject() {
		//create a new proposal a send to admin/faculty
	}

	public AccountCreationModel getModel() {
		return model;
	}

	public void setModel(AccountCreationModel model) {
		this.model = model;
	}
}