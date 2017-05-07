package edu.ycp.cs320.cspath1.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.cspath1.model.Pair;
import edu.ycp.cs320.cspath1.user.User;
import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.enums.ProjectType;
import edu.ycp.cs320.cspath1.enums.SolicitationType;
import edu.ycp.cs320.cspath1.persist.IDatabase;
import edu.ycp.cs320.cspath1.persist.YCPDatabase;
import edu.ycp.cs320.cspath1.project.Project;
import edu.ycp.cs320.cspath1.persist.DatabaseProvider;

public class SearchProjects {

	private IDatabase db = null;

	public SearchProjects() {
		
		// creating DB instance here
		DatabaseProvider.setInstance(new YCPDatabase());
		db = DatabaseProvider.getInstance();		
	}

	public ArrayList<Project> getAllProjects() throws IOException, SQLException {
		
		// get the list of users from DB
		List<Project> projectList = db.findAllProjects();
		ArrayList<Project> projects = null;
		
		if (projectList.isEmpty()) {
			System.out.println("No Users found in library");
			return null;
		}
		else {
			projects = new ArrayList<Project>();
			for (Project project : projectList) {
				projects.add(project);
				System.out.println(project.getTitle());
			}			
		}
		
		// return users
		return projects;
	}
	public ArrayList<Project> getProjectsByTitle(String title) throws IOException, SQLException{
		//List<User> userList = db.findAllUsers();
		ArrayList<Project> projects = new ArrayList<Project>();
		projects.add(db.findProjectByTitle(title));
		if (projects.isEmpty()) {
			System.out.println("No Users found in library");
			return null;
		}
		else {
			return projects;
			}
	}
	public ArrayList<Project> getProjectsByDescription(String description) throws IOException, SQLException{
		//List<User> userList = db.findAllUsers();
		ArrayList<Project> projects = new ArrayList<Project>();
		projects.add(db.findProjectByDescription(description));
		if (projects.isEmpty()) {
			System.out.println("No Users found in library");
			return null;
		}
		else {
			return projects;
			}
	}
	public ArrayList<Project> getProjectsByMajor(MajorType major) throws IOException, SQLException {
		
		// get the list of users from DB
		List<Project> projectList = db.findProjectByMajorType(major);
		ArrayList<Project> projects = null;
		
		if (projectList.isEmpty()) {
			System.out.println("No Users found in library");
			return null;
		}
		else {
			projects = new ArrayList<Project>();
			for (Project project : projectList) {
				projects.add(project);
				System.out.println(project.getTitle());
			}			
		}
		
		// return return users for his major
		return projects;
	}
	public ArrayList<Project> getUsersByProjectType(ProjectType projecttype) throws IOException, SQLException {
	
	// get the list of users from DB
	List<Project> projectList = db.findProjectByProjectType(projecttype);
	ArrayList<Project> projects = null;
	
	if (projectList.isEmpty()) {
		System.out.println("No Users found in library");
		return null;
	}
	else {
		projects = new ArrayList<Project>();
		for (Project user : projectList) {
			projects.add(user);
			System.out.println(user.getTitle());
		}			
	}
	
	// return return users for his major
	return projects;
	}
	public ArrayList<Project> getProjectsByClassType(ClassType classtype) throws IOException, SQLException {
		
		// get the list of users from DB
		List<Project> projectList = db.findProjectByClassType(classtype);
		ArrayList<Project> projects = null;
		
		if (projectList.isEmpty()) {
			System.out.println("No Users found in library");
			return null;
		}
		else {
			projects = new ArrayList<Project>();
			for (Project project : projectList) {
				projects.add(project);
				System.out.println(project.getTitle());
			}			
		}
		
		// return return users for his major
		return projects;
	}
	public ArrayList<Project> getProjectsByStart(String start) throws IOException, SQLException {
		
		// get the list of users from DB
		List<Project> projectList = db.findProjectByStart(start);
		ArrayList<Project> projects = null;
		
		if (projectList.isEmpty()) {
			System.out.println("No Users found in library");
			return null;
		}
		else {
			projects = new ArrayList<Project>();
			for (Project project : projectList) {
				projects.add(project);
				System.out.println(project.getTitle());
			}			
		}
		
		// return return users for his major
		return projects;
		}
	public ArrayList<Project> getProjectsByDuration(int duration) throws IOException, SQLException{
		
		// get list of projects from DB
		List<Project> projectList = db.findProjectByDuration(duration);
		ArrayList<Project> projects = null;
		
		if(projectList.isEmpty()){
			System.out.println("No Projects found in library");
			return null;
		}
		else{
			projects = new ArrayList<Project>();
			for(Project project : projectList){
				projects.add(project);
				System.out.println(project.getTitle());
			}
		}
		
		//return projects with this duration
		return projects;
	}
	public ArrayList<Project> getProjectsBySolicitationType(SolicitationType type) throws IOException, SQLException{
		// get list of projects from DB
				List<Project> projectList = db.findProjectBySolicitationType(type);
				ArrayList<Project> projects = null;
				
				if(projectList.isEmpty()){
					System.out.println("No Projects found in library");
					return null;
				}
				else{
					projects = new ArrayList<Project>();
					for(Project project : projectList){
						projects.add(project);
						System.out.println(project.getTitle());
					}
				}
				
				//return projects with this Solicitation Type
				return projects;
	}
	public ArrayList<Project> getProjectsByNumberOfStudents(int numStudents)throws IOException, SQLException{
		// get list of projects from DB
		List<Project> projectList = db.findProjectByNumStudents(numStudents);
		ArrayList<Project> projects = null;
		
		if(projectList.isEmpty()){
			System.out.println("No Projects found in library");
			return null;
		}
		else{
			projects = new ArrayList<Project>();
			for(Project project : projectList){
				projects.add(project);
				System.out.println(project.getTitle());
			}
		}
		
		//return projects with this number of students
		return projects;
	}
	public ArrayList<Project> getProjectsByCost(double cost)throws IOException, SQLException{
		// get list of projects from DB
				List<Project> projectList = db.findProjectByCost(cost);
				ArrayList<Project> projects = null;
				
				if(projectList.isEmpty()){
					System.out.println("No Projects found in library");
					return null;
				}
				else{
					projects = new ArrayList<Project>();
					for(Project project : projectList){
						projects.add(project);
						System.out.println(project.getTitle());
					}
				}
				
				//return projects by cost
				return projects;
	}
	public ArrayList<Project> getProjectsByFunding(boolean funded) throws IOException, SQLException{
		// get list of projects from DB
				List<Project> projectList = db.findProjectByIsFunded(funded);
				ArrayList<Project> projects = null;
				
				if(projectList.isEmpty()){
					System.out.println("No Projects found in library");
					return null;
				}
				else{
					projects = new ArrayList<Project>();
					for(Project project : projectList){
						projects.add(project);
						System.out.println(project.getTitle());
					}
				}
				
				//return projects with this number of students
				return projects;
	}
	public ArrayList<Project> getProjectsByBudget(Double budget) throws IOException, SQLException{
		// get list of projects from DB
				List<Project> projectList = db.findProjectByBudget(budget);
				ArrayList<Project> projects = null;
				
				if(projectList.isEmpty()){
					System.out.println("No Projects found in library");
					return null;
				}
				else{
					projects = new ArrayList<Project>();
					for(Project project : projectList){
						projects.add(project);
						System.out.println(project.getTitle());
					}
				}
				
				//return projects with this number of students
				return projects;
	}
	public ArrayList<Project> getProjectsByDeadline(String deadline) throws IOException, SQLException{
		// get list of projects from DB
				List<Project> projectList = db.findProjectByDeadline(deadline);
				ArrayList<Project> projects = null;
				
				if(projectList.isEmpty()){
					System.out.println("No Projects found in library");
					return null;
				}
				else{
					projects = new ArrayList<Project>();
					for(Project project : projectList){
						projects.add(project);
						System.out.println(project.getTitle());
					}
				}
				
				//return projects with this number of students
				return projects;
	}
	public ArrayList<Project> getProjectsByUser(int UserID) throws IOException, SQLException{
		
		// get the list of (Author, Book) pairs from DB
		List<Pair<User, Project>> userProjectList = db.findAllProjectsByUser(UserID);
		ArrayList<Project> projects = null;
		
		if (userProjectList.isEmpty()) {
			System.out.println("No Projects found in library with userID <" + UserID + ">");
			return null;
		}
		else {
			projects = new ArrayList<Project>();
			for (Pair<User, Project> userProject : userProjectList) {
				User user = userProject.getLeft();
				Project project = userProject.getRight();
				projects.add(project);
				System.out.println(project.getTitle() + " was made by "+user.getUsername());
			}			
		}
		
		// return authors for this title
		return projects;
	}
}

