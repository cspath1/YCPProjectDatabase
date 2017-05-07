package edu.ycp.cs320.cspath1.model;

import edu.ycp.cs320.cspath1.email.EmailValidator;
import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.enums.UserType;

public class AccountCreationModel {
	private UserType usertype;
	private String email;
	private String username;
	private String password;
	private MajorType majortype;
	private ClassType classtype;
	private EmailValidator emailValidator;
	private String name;
	private String firstName;
	private String lastName;
	private String address;
	private String contactNum;
	
	
	//tested
	public ClassType getClasstype() {
		return classtype;
	}
	
	//tested
	public void setClasstype(ClassType classtype) {
		this.classtype = classtype;
	}
	
	//tested
	public MajorType getMajortype() {
		return majortype;
	}
	
	//tested
	public void setMajortype(MajorType majortype) {
		this.majortype = majortype;
	}
	
	//tested
	public String getPassword() {
		return password;
	}
	
	//tested
	public void setPassword(String password) {
		this.password = password;
	}
	
	//tested
	public String getUsername() {
		return username;
	}
	
	//tested
	public void setUsername(String username) {
		this.username = username;
	}
	
	//tested
	public String getEmail() {
		return email;
	}
	
	//tested
	public void setEmail(String email) {
		this.email = email;
	}
	
	//tested
	public UserType getUsertype() {
		return usertype;
	}
	
	//tested
	public void setUsertype(UserType usertype) {
		this.usertype = usertype;
	}

	public EmailValidator getEmailValidator() {
		return emailValidator;
	}

	public void setEmailValidator(EmailValidator emailValidator) {
		this.emailValidator = emailValidator;
	}
	
	
	
	//this will change soon...ignore for now
	public Boolean allFieldsMet(){
		//placeholder for now
		if (usertype == UserType.ADMIN){
			return true;
		}
		else if (usertype == UserType.FACULTY){
			if (email != null && username != null && password != null && majortype != null){
				return true;
			}
			else { return false; }
		}
		else if (usertype == UserType.STUDENT){
			if (email != null && username != null && password != null && majortype != null && classtype != null){
				return true;
			}
			else { return false; }
		}
		else{
			if (email != null && username != null && password != null){
				return true;
			}
			else { return false; }
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	
	
}
