package edu.ycp.cs320.cspath1.persist;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.ProjectType;
import edu.ycp.cs320.cspath1.enums.SolicitationType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.enums.ProjectType;
import edu.ycp.cs320.cspath1.enums.UserType;
import edu.ycp.cs320.cspath1.model.Pair;
import edu.ycp.cs320.cspath1.persist.IDatabase;
import edu.ycp.cs320.cspath1.project.ActiveProject;
import edu.ycp.cs320.cspath1.project.Project;
import edu.ycp.cs320.cspath1.user.User;

public class YCPDatabaseTests {
	private IDatabase db = null;
	
	User user = null;
	Project project = null;
	ActiveProject activeProject = null;
	List<Project> projects = null;
	List<ActiveProject> activeProjects = null;
	List<User> users = null;
	List<Pair<User, Project>> userProjectList = null;
	List<Pair<User, Project>> projectUserList = null;
	List<Pair<User, ActiveProject>> activeProjectUsers = null;
	List<Pair<Project, Project>> projectsPair = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}
	
	@Before 
	public void setUp() throws Exception {
		DatabaseProvider.setInstance(new YCPDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testFindUserByFirstname() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByFirstname");
		
		String firstname = "Jason";
		
		users = db.findUserByFirstname(firstname);
		
		if(users.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			for (User user : users){
				System.out.println(user.getUsername() + ", " + user.getEmail());
			}
		}
	}
	
	@Test
	public void testFindUserByLastname() throws IOException, SQLException{
		System.out.println("\n*** Testing findUserByLastname");
		
		String lastname = "Spath";
		
		users = db.findUserByLastname(lastname);
		
		if(users.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		else {
			for (User user : users) {
				System.out.println(user.getEmail() + ", " + user.getUsername());
			}
		}
	}
	
	@Test
	public void testFindUserByUsernameAndPassword() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByUsernameAndPassword ***");
		
		String username = "cspath1";
		String password = "password";
		
		user = db.findUserByUsernameAndPassword(username, password);
		
		if(user.getUsername() == null){
			System.out.println("No user found in database with username <" + username + "> and password <" + password + ">");
			fail("No user with username <" + username + "> and password <" + password + "> returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getUsername() + ", " + user.getPassword() + ", " + user.getUsertype());
		}
	}
	
	@Test
	public void testFindUserByClassType() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByClassType ***");
		
		List<User> userList = db.findUserByClassType(ClassType.SOPHOMORE);
		
		
		if (userList.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
				
		}
		
		else {
			users = new ArrayList<User>();
			for (User user : userList){
				users.add(user);
				System.out.println(user.getUsername() + ", " + user.getEmail() + ", " + user.getPassword() + ", " + user.getUsertype());;
			}
		}
		
	}
	
	@Test
	public void testFindUsersByMajorType() throws IOException, SQLException {
		System.out.println("\n*** Testing findUsersByMajorType ***");
		
		List<User> userList = db.findUserByMajorType(MajorType.CS);
		
		if(userList.isEmpty()) {
			System.out.println("No users found in DB");
			fail("No users returned from DB");
		}
		
		else {
			users = new ArrayList<User>();
			for (User user : userList) {
				users.add(user);
				System.out.println(user.getEmail() + ", " + user.getUsername() + ", " + user.getUserID());
			}
		}
	}
	
	@Test
	public void testFindUserByUserID() throws IOException, SQLException{
		System.out.println("\n*** Testing findUserByUserID***");
		
		int userID = 4;
		
		user = db.findUserByUserID(userID);
		
		if (user.getEmail() == null){
			System.out.println("No users found in database");
			fail("No users returned from DB");
		}
		else {
			System.out.println(user.getEmail() + ", " + user.getPassword() + ", " + user.getUsername());
		}
		
	}
	
	@Test
	public void testFindUserByUsername() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByUsername***");
		
		String username = "jhopkins1";
		
		user = db.findUserByUsername(username);
		
		if (user.getPassword() == null) {
			System.out.println("No users found in database");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getPassword() + ", " + user.getUsername());
		}
	}
	
	@Test
	public void testFindUserByEmail() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByEmail***");
		
		String email = "jhopkins1@ycp.edu";
		
		user = db.findUserByEmail(email);
		
		if (user.getUsername() == null){
			System.out.println("No users found in database");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getPassword() + ", " + user.getUsername());
		}
	}
	
	@Test
	public void testFindUserByUsertype() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByUsertype");
		
		UserType usertype = UserType.STUDENT;
		
		users = db.findUserByUserType(usertype);
		
		if (users.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			for (User user : users) {
				System.out.println(user.getEmail() + ", " + user.getPassword() + ", " + user.getUsername());
			}
		}
	}
	
	@Test
	public void testFindUserByName() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByName");
		
		user = db.findUserByName("Business1 Inc.");
		
		if(user.getEmail() == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getUsername());
		}
	}
	
	@Test
	public void testFindUserByAddress() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByAddress");
		
		user = db.findUserByAddress("123 Fake Address Street");
		
		if(user.getEmail() == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getUsername());
		}
		
	}
	
	@Test
	public void testFindUserByNumber() throws IOException, SQLException {
		System.out.println("\n*** Testing findUserByNumber");
		
		user = db.findUserByNumber("717-777-7776");
		
		if(user.getPassword() == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getUsername());
		}
	}
	
	@Test
	public void testEditEmail() throws IOException, SQLException { 
		System.out.println("\n*** Testing editEmail***");
		
		db.editEmail(17, "cspath@ycp.edu");
		
		user = db.findUserByEmail("cspath@ycp.edu");
		
		if(user.getPassword() == null){
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getPassword() + ", " + user.getUsername());
		}
		
	}
	
	@Test
	public void testEditUsername() throws IOException, SQLException {
		System.out.println("\n*** Testing editUsername");
		
		db.editUsername(17, "spathcody");
		
		user = db.findUserByUserID(17);
		
		if(user.getEmail() == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getUsername() + ", " + user.getUserID());
		}
	}
	
	@Test
	public void testEditPassword() throws IOException, SQLException {
		System.out.println("\n*** Testing editPassword***");
		
		db.editPassword(17, "password1");
		
		user = db.findUserByUserID(17);
		
		if (user.getEmail() == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from db");
			
		}
		
		else {
			System.out.println(user.getEmail() + ", " + user.getPassword());
		}
	}
	
	@Test
	public void testInsertUser() throws IOException, SQLException {
		System.out.println("\n*** Testing insertUser***");
		
		Integer user_id = 0;
		user_id = db.insertUser("cspath2", "password", "cspath2@ycp.edu", UserType.STUDENT, "Charles", "Spath", MajorType.EE, ClassType.FRESHMAN, null, null, null);
		
		System.out.println(user_id);
		
		if(user_id > 0){
			user = db.findUserByUserID(user_id);
			
			if(user.getEmail() == null){
				System.out.println("Something has gone horribly wrong...");
				fail("No users returned from DB");
			}
			else {
				System.out.println(user.getEmail() + ", " + user.getUsername() + ", " + user.getPassword());
				db.deleteUserAndProjects(user.getUserID());
			}	
		}
		else {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
	}
	
	@Test
	public void testFindAllUsers() throws IOException, SQLException {
		System.out.println("\n*** Testing findAllUsers***");
		
		List<User> userList = db.findAllUsers();
		
		if (userList.isEmpty()) {
			System.out.println("No users found in DB");
			fail("No users returned from DB");
			
		}
		else {
			users = new ArrayList<User>();
			for (User user : userList){
				users.add(user);
				System.out.println(user.getUsername() + ", " + user.getUserID());
			}
		}
	}	
	
	@Test
	public void testInsertProject() throws IOException, SQLException {
		System.out.println("\n*** Testing insertProject ***");
		ArrayList<MajorType> majors = new ArrayList<MajorType>();
		majors.add(MajorType.CIV);
		
		ArrayList<ClassType> classes = new ArrayList<ClassType>();
		classes.add(ClassType.JUNIOR);
		int project_id = 0;
		project_id = db.insertProject(1, "Test", "description", "4/20/17", 1, ProjectType.SOLICITATION, SolicitationType.CivE_CAPSTONE, majors, classes
				, 2, 8000, true, "5/16/17");
		
		System.out.println(project_id);
		
		if(project_id > 0) {
			project = db.findProjectByProjectID(project_id);
			
			if(project.getTitle() == null){
				System.out.println("Project was not inserted correctly");
				fail("Project does not match specified project");
			}
			else {
				System.out.println(project.getTitle() + ", " + project.getDescription());
				db.deleteProject(project_id);
			}	
		} else {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		}
	}
	
	@Test
	public void testDeleteProject() throws IOException, SQLException {
		System.out.println("\n*** Testing deleteProject ***");
		ArrayList<MajorType> majors = new ArrayList<MajorType>();
		ArrayList<ClassType> classes = new ArrayList<ClassType>();
		
		majors.add(MajorType.CS);
		classes.add(ClassType.SOPHOMORE);
		
		int project_id = db.insertProject(1, "Test", "description", "4/20/17", 1, ProjectType.PROPOSAL, null, majors, classes, 2, 3000, false, "4/21/17");
		
		db.deleteProject(project_id);
		project = db.findProjectByProjectID(project_id);
		
		if(project.getDescription() == null) {
			System.out.println("Project was deleted successfully!");
		} else {
			System.out.println(project.getTitle());
			System.out.println("Project was not deleted");
			fail("DB did not successfully delete the project");
		}
	}

	@Test
	public void testFindAllProjects() throws IOException, SQLException {
		System.out.println("\n*** Testing findAllProjects ***");
		

		List<Project> projectList = db.findAllProjects();
		projects = new ArrayList<Project>();
		if (projectList.isEmpty()) {
			System.out.println("No projectss found in DB");

		projects = new ArrayList<Project>();
		projects = db.findAllProjects();
		
		if (projects.isEmpty()) {
			System.out.println("No projects found in DB");
			fail("No projectss returned from DB");
			
		}
		
		
		} else {
			for (Project project : projects){

				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByProjectID() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByProjectID ***");
		
		int ProjectID = 1;
		
		project = db.findProjectByProjectID(ProjectID);
		
		if (project == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			System.out.println(project.getProjectID() + ", " + project.getTitle());
		}
	}
	
	@Test
	public void testFindProjectByTitle() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByTitle ***");
		
		String title = "YCP Project Database";
		
		project = db.findProjectByTitle(title);
		
		if (project == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			System.out.println(project.getProjectID() + ", " + project.getTitle());
		}
	}
	
	@Test
	public void testFindProjectByDescription() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByDescription ***");
		
		String description = "Allows for insight on specific classes";
		
		project = db.findProjectByDescription(description);
		
		if (project == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			System.out.println(project.getProjectID() + ", " + project.getTitle());
		}
	}
	
	@Test
	public void testFindProjectByStart() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByStart ***");
		
		String start = "1/27/17";
		
		projects = db.findProjectByStart(start);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByDuration() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByDuration ***");
		
		int duration = 1;
		
		projects = db.findProjectByDuration(duration);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByProjectType() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByProjectType ***");
		
		ProjectType type = ProjectType.PROPOSAL;
		
		projects = db.findProjectByProjectType(type);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectBySolicitationType() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectBySolicitationType ***");
		
		SolicitationType type = SolicitationType.CLASS_PROJECT;
		
		projects = db.findProjectBySolicitationType(type);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByMajorType() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByMajorType ***");
		
		MajorType major = MajorType.CE;
		
		projects = db.findProjectByMajorType(major);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByClassType() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByClassType ***");
		
		ClassType type = ClassType.SOPHOMORE;
		
		projects = db.findProjectByClassType(type);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByNumStudents() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByNumStudents ***");
		
		int numStudents = 3;
		
		projects = db.findProjectByNumStudents(numStudents);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByCost() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByCost ***");
		
		double cost = 0;
		
		projects = db.findProjectByCost(cost);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByIsFunded() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByIsFunded ***");
		
		Boolean isFunded = true;
		
		projects = db.findProjectByIsFunded(isFunded);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindProjectByDeadline() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByDeadline ***");
		
		String deadline = "5/7/17";
		
		projects = db.findProjectByDeadline(deadline);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}
	
	/*@Test
	public void testFindProjectByBudget() throws IOException, SQLException {
		System.out.println("\n*** Testing findProjectByBudget ***");
		
		double budget = 10000;
		
		projects = db.findProjectByBudget(budget);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from DB");
		} else {
			for (Project project: projects) {
				System.out.println(project.getProjectID() + ", " + project.getTitle());
			}
		}
	}*/
	
	@Test

	public void testFindAllUsersByProject() throws IOException, SQLException {
		System.out.println("\n*** Testing findAllUsersByProject ***");
		
		int project_id = 1;
		
		projectUserList = db.findAllUsersByProject(1);
		
		if(projectUserList.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No project with such project id was found in the DB");
		} else {
			for (Pair<User, Project> projectUser : projectUserList) {
				User user = projectUser.getLeft();
				Project project = projectUser.getRight();
				System.out.println(user.getUserID() + ", " + user.getUsername() + ", " + project.getTitle());
			}
		}
	}
	
	@Test
	public void testFindAllProjectsByUser() throws IOException, SQLException {
		System.out.println("\n*** Testing findAllProjectsByUser ***");
		
		int user_id = 1;
		
		userProjectList = db.findAllProjectsByUser(user_id);
		
		if(userProjectList.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No user with such user id was found in the DB");
		} else {
			for (Pair<User, Project> projectUser : userProjectList) {
				User user = projectUser.getLeft();
				Project project = projectUser.getRight();
				System.out.println(project.getProjectID() + ", " + project.getTitle() + ", " + user.getUsername());
			}
		}
	}
		

	public void testEditDescription() throws IOException, SQLException {
		System.out.println("\n*** Testing editDescription");
		
		Integer project_id = 3;
		String description = "I would like to change the description to this project...";
		
		db.editDescription(project_id, description);
		
		project = db.findProjectByDescription(description);
		
		if(project == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(project.getTitle() + ", " + project.getProjectID());
		}
	}
	
	@Test
	public void testEditTitle() throws IOException, SQLException {
		System.out.println("\n*** Testing editTitle");
		
		Integer project_id = 2;
		String title = "NEW PROJECT TITLE";
		
		db.editTitle(project_id, title);
		
		project = db.findProjectByTitle(title);
		
		if (project == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		else {
			System.out.println(project.getStart() + ", " + project.getProjectID());
		}
	}
	
	@Test
	public void testEditStart() throws IOException, SQLException {
		System.out.println("\n*** Testing editStart");
		
		Integer project_id = 4;
		String start = "3/14/17";
		
		db.editStart(project_id, start);
		
		projects = db.findProjectByStart(start);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(projects.get(0).getProjectID());
		}
	}
	
	@Test
	public void testEditDuration() throws IOException, SQLException {
		System.out.println("\n ***Testing editDuration");
		
		Integer project_id = 1;
		Integer duration = 8;
		db.editDuration(project_id, duration);
		
		projects = db.findProjectByDuration(duration);
		
		if(projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from DB");
		}
		
		else {
			System.out.println(projects.get(0).getDescription() + ", " + projects.get(0).getProjectID());
		}

		
	}
	
	@Test
	public void testEditCost() throws IOException, SQLException {
		System.out.println("\n ***Testing editCost***");
		
		Integer project_id = 2;
		double cost = 100000;
		db.editCost(project_id, cost);
		
		projects = db.findProjectByCost(cost);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			System.out.println(projects.get(0).getDescription() + ", " + projects.get(0).getTitle());
		}
		
	}
	
	@Test 
	public void testEditNumStudents() throws IOException, SQLException {
		System.out.println("\n ***Testing editNumStudents***");
		
		Integer project_id = 3;
		Integer numStudents = 15;
		
		db.editNumStudents(project_id, numStudents);
		
		projects = db.findProjectByNumStudents(numStudents);
		
		if(projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			System.out.println(projects.get(0).getDuration() + ", " + projects.get(0).getProjectID());
		}
	}
	
	@Test
	public void testEditFunded() throws IOException, SQLException {
		System.out.println("\n ***Testing editFunding***");
		
		Integer project_id = 2;
		Boolean isFunded = false;
		
		db.editFunding(project_id, isFunded);
		
		projects = db.findProjectByIsFunded(isFunded);
		
		if(projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			System.out.println(projects.get(0).getDescription() + ", " + projects.get(0).getDuration());
		}
	}
	
	@Test
	public void testEditSolicitationType() throws IOException, SQLException {
		System.out.println("\n ***Testing editSolicitationType***");
		
		Integer project_id = 4;
		SolicitationType solicitationType = SolicitationType.CS_INTERNSHIP;
		db.editSolicitationType(project_id, solicitationType);
		
		projects = db.findProjectBySolicitationType(solicitationType);
		
		if (projects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		else {
			System.out.println(projects.get(0).getDuration() + ", " + projects.get(0).getProjectID());
		}
	}
	
	@Test
	public void testFindActiveProjectByActiveProjectID() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByActiveProjectID");
		
		Integer active_project_id = 2;
		activeProject = db.findActiveProjectByActiveProjectID(active_project_id);
		
		if(activeProject.getDescription() == null) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			System.out.println(activeProject.getStart() + ", " + activeProject.getTitle());
		}
	}
	
	@Test
	public void testFindActiveProjectByProjectIDCopy1() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByProjectIDCopy1");
		
		Integer project_id_copy1 = 1;
		activeProjects = db.findActiveProjectByProjectIDCopy2(project_id_copy1);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			System.out.println(activeProjects.get(0).getDuration() + ", " + activeProjects.get(0).getTitle());
		}
	}
	
	@Test
	public void testFindActiveProjectByTitle() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByTitle");
		
		String title = "Electric Car";
		activeProjects = db.findActiveProjectByTitle(title);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testFindActiveProjectByProjectIDCopy2() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByProjectIDCopy2");
		
		Integer project_id_copy2 = 3;
		activeProjects = db.findActiveProjectByProjectIDCopy1(project_id_copy2);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			System.out.println(activeProjects.get(0).getDuration() + ", " + activeProjects.get(0).getTitle());
		}
	}
	
	@Test 
	public void testFindActiveProjectByStart() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByStart");
		
		String start = "5/14/17";
		activeProjects = db.findActiveProjectByStart(start);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testFindActiveProjectByDuration() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByDuration");
		
		Integer duration = 2;
		activeProjects = db.findActiveProjectByDuration(duration);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testFindActiveProjectByMajor() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByMajor");
		
		MajorType major = MajorType.EE;
		activeProjects = db.findActiveProjectByMajorType(major);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testFindActiveProjectByClass() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByClass");
		
		ClassType classtype = ClassType.JUNIOR;
		activeProjects = db.findActiveProjectByClassType(classtype);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
		
	}
	
	@Test
	public void testFindActiveProjectByNumStudents() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByNumStudents");
		
		Integer numStudents = 3;
		activeProjects = db.findActiveProjectByNumStudents(numStudents);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testFindActiveProjectByFunding() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByFunding");
		
		Boolean funding = true;
		activeProjects = db.findActiveProjectByIsFunded(funding);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test 
	public void testFindActiveProjectByDeadline() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByDeadline");
		
		String deadline = "12/1/17";
		
		activeProjects = db.findActiveProjectByDeadline(deadline);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testFindActiveProjectByBudget() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByBudget");
		
		double budget = 10000;
		
		activeProjects = db.findActiveProjectByBudget(budget);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testFindActiveProjectByCost() throws IOException, SQLException {
		System.out.println("\n ***Testing findActiveProjectByCost");
		
		double cost = 8794;
		activeProjects = db.findActiveProjectByCost(cost);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
		}
	}
	
	@Test
	public void testEditActiveProjectTitle() throws IOException, SQLException {
		System.out.println("\n ***Testing editActiveProjectTitle");
		
		String title = "NEW TITLE";
		Integer active_project_id = 1;
		
		db.editActiveProjectTitle(active_project_id, title);
		
		activeProjects = db.findActiveProjectByTitle(title);

		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
		
	}
	
	/*@Test
	public void testEditActiveProjectDescription() throws IOException, SQLException {
		System.out.println("\n ***Testing editActiveProjectDescription");
		
		String description = "THIS IS MY NEW DESCRIPTION";
		Integer active_project_id = 1;
		
		db.editActiveProjectDescription(active_project_id, description);
		
		activeProjects = db.findActiveProjectByDescription(description);
		

		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}*/
	
	@Test
	public void testEditActiveProjectStart() throws IOException, SQLException {
		System.out.println("\n ***Testing editActiveProjectStart");
		
		String start = "4/20/17";
		Integer active_project_id = 1;
		
		db.editActiveProjectStart(active_project_id, start);
		
		activeProjects = db.findActiveProjectByStart(start);
		

		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testEditActiveProjectDuration() throws IOException, SQLException {
		System.out.println("\n ***Testing editActiveProjectDuration");
		
		Integer duration = 3;
		Integer active_project_id = 1;
		
		db.editActiveProjectDuration(active_project_id, duration);
		
		activeProjects = db.findActiveProjectByDuration(duration);
		

		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testEditActiveProjectNumStudents() throws IOException, SQLException {
		System.out.println("\n ***Testing editActiveProjectNumStudents");
		
		Integer numStudents = 5;
		Integer active_project_id = 1;
		
		db.editActiveProjectNumStudents(active_project_id, numStudents);
		
		activeProjects = db.findActiveProjectByNumStudents(numStudents);

		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
			
		}
	}
	
	@Test
	public void testEditActiveProjectCost() throws IOException, SQLException {
		System.out.println("\n ***Testing editActiveProjectCost");
		
		double cost = 6666;
		Integer active_project_id = 1;
		
		db.editActiveProjectCost(active_project_id, cost);
		
		activeProjects = db.findActiveProjectByCost(cost);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
		}
	}
	
	@Test
	public void testEditDeadline() throws IOException, SQLException {
		System.out.println("\n ***Testing editActiveProjectDeadline");
		
		String deadline = "12/25/17";
		Integer active_project_id = 1;
		
		db.editActiveProjectDeadline(active_project_id, deadline);
		
		activeProjects = db.findActiveProjectByDeadline(deadline);
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
		}
	}
	
	@Test
	public void testFindAllActiveProjects() throws IOException, SQLException {
		System.out.println("\n ***Testing findAllActiveProjects");
		
		activeProjects = db.findAllActiveProjects();
		
		if(activeProjects.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for (ActiveProject activeProject : activeProjects) {
				System.out.println(activeProject.getTitle() + ", " + activeProject.getDuration());
			}
		}
	}
	
	@Test
	public void testFindUserIDByUsernameAndPassword() throws IOException, SQLException {
		System.out.println("\n ***Testing findUserIDByUsernameAndPassword");
		
		int user_id = db.findUserIDByUsernameAndPassword("cspath1", "password");
		
		assertEquals(user_id, 30);
	}
	
	@Test 
	public void testFindAllActiveProjectsByUser() throws IOException, SQLException {
		System.out.println("\n ***Testing findAllActiveProjectsByUser");
		
		int user_id = 17;
		
		activeProjectUsers = db.findAllActiveProjectsByUser(user_id);
		
		if(activeProjectUsers.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for(Pair<User, ActiveProject> activeProjectUser : activeProjectUsers) {
				System.out.println(activeProjectUser.getLeft().getUsername() + ", " + activeProjectUser.getRight().getTitle());
			}
		}
	}
	
	@Test
	public void testFindAllUsersByActiveProject() throws IOException, SQLException {
		System.out.println("\n ***Testing findAllUsersByActiveProject");
		
		int active_project_id = 2;
		
		activeProjectUsers = db.findAllUsersByActiveProject(active_project_id);
		
		if(activeProjectUsers.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
		else {
			for(Pair<User, ActiveProject> activeProjectUser : activeProjectUsers) {
				System.out.println(activeProjectUser.getLeft().getUsername() + ", " + activeProjectUser.getRight().getTitle());
			}
		}
	}
	
	@Test
	public void testInsertActiveProject() throws IOException, SQLException {
		System.out.println("\n ***Testing insertActiveProject");
		ArrayList<MajorType> majors = new ArrayList<MajorType>();
		majors.add(MajorType.CIV);
		
		ArrayList<ClassType> classes = new ArrayList<ClassType>();
		classes.add(ClassType.JUNIOR);
		int active_project_id = -1; 
		active_project_id = db.insertActiveProject(4, 2, "testing", "do a thing", "5/17/17", 3, ProjectType.ACTIVE, majors, classes, 4, 8000, true, "4/20/18", 8500);
		
		activeProject = db.findActiveProjectByActiveProjectID(active_project_id);
		if(active_project_id != -1) {
			if(activeProject.getDescription() == null) {
				System.out.println("Something has gone horribly wrong...");
				fail("No projects returned from db");
			}
			else {
				db.deleteActiveProject(active_project_id);
			}
		}
		else {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db");
		}
		
	}
	
	@Test
	public void testFindUserByNameWildcard() throws IOException, SQLException {
		System.out.println("\n ***Testing findUserByNameWildcard***");
		String name = "ss7";
		
		users = db.findUserByNameWildcard(name);
		
		if (users.get(0).getEmail() == null) {
			System.out.println("Something has gone horribly wrong");
			fail("No users returned from db");
		}
		else {
			for (User user : users) {
				System.out.println(user.getEmail() + ", " + user.getUserID());
			}
		}
	}
	
	@Test
	public void testFindUserByAddressWildcard() throws IOException, SQLException {
		System.out.println("\n ***Testing findUserByAddressWildcard***");
		
		String address = "Avenue";

		users = db.findUserByAddressWildcard(address);
		
		if (users.get(0).getEmail() == null) {
			System.out.println("Something has gone horribly wrong");
			fail("No users returned from db");
		}
		else {
			for (User user : users) {
				System.out.println(user.getEmail() + ", " + user.getUserID());
			}
		}
	}
	
	@Test
	public void testInsertProjectsToProjectProjects() throws IOException, SQLException {
		System.out.println("\n ***Testing insertProjectsIntoProjectProjects***");
		
		Integer project_id = db.insertProjectsintoProjectProjects(3, 2);
		
		projectsPair = db.findAllProjectsByProjectID(3);
		
		if(projectsPair.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No projects returned from db.");
			
		}
		else {
			for (Pair<Project, Project> projectPair : projectsPair) {
				System.out.println(projectPair.getLeft().getProjectID() + ", " + projectPair.getRight().getProjectID());
			}
		}
		
		db.deleteProjectFromProjectProjects(3, 2);
		
		projectsPair = db.findAllProjectsByProjectID(3);
		
		if(!projectsPair.isEmpty()) {
			System.out.println("Relation was not successfully deleted.");
			fail("Relation not deleted in table");
		}
		else {
			System.out.println("Project relation was inserted and deleted successfully.");
		}
	}
	
	@Test
	public void testEditMajorType() throws IOException, SQLException {
		System.out.println("\n ***Testing editMajorType***");
		
		
		db.editMajorType(32, MajorType.CE);
		
		users = db.findUserByMajorType(MajorType.CE);
		
		if(users.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from database");
		}
		else {
			for (User user : users) {
				System.out.println(user.getEmail() + ", " + user.getUsername());
			}
		}
	}
	
	@Test
	public void testEditClassType() throws IOException, SQLException {
		System.out.println("\n ***Testing editClassType");
		
		db.editClassType(32, ClassType.SOPHOMORE);
		
		users = db.findUserByClassType(ClassType.SOPHOMORE);
		
		if(users.isEmpty()) {
			System.out.println("Something has gone horribly wrong...");
			fail("No users returned from database");
		}
		else {
			for (User user : users) {
				System.out.println(user.getEmail() + ", " + user.getUsername());
			}
		}
	}
}
