package edu.ycp.cs320.cspath1.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.enums.ProjectType;
import edu.ycp.cs320.cspath1.enums.SolicitationType;
import edu.ycp.cs320.cspath1.enums.UserType;
import edu.ycp.cs320.cspath1.model.Pair;
import edu.ycp.cs320.cspath1.project.ActiveProject;
import edu.ycp.cs320.cspath1.project.Project;
import edu.ycp.cs320.cspath1.project.Proposal;
import edu.ycp.cs320.cspath1.project.Solicitation;
import edu.ycp.cs320.cspath1.user.Business;
import edu.ycp.cs320.cspath1.user.Faculty;
import edu.ycp.cs320.cspath1.user.Student;
import edu.ycp.cs320.cspath1.user.User;

public interface IDatabase {

	//USERS
	//tested
	public Integer insertUser(String username, String password, String email, UserType usertype, String firstname, String lastname, MajorType major, ClassType classtype
			, String name, String address, String contactNum) throws IOException, SQLException;
	//tested
	public void deleteUserAndProjects(int user_id) throws IOException, SQLException;
	//tested
	public void editPassword(int UserID, String password) throws IOException, SQLException;
	//tested
	public void editEmail(int UserID, String email) throws IOException, SQLException;
	//tested
	public void editUsername(int UserID, String email) throws IOException, SQLException;
	//tested
	public User findUserByUserID(int UserID) throws IOException, SQLException;
	//tested
	public User findUserByUsername(String username) throws IOException, SQLException;
	//tested
	public User findUserByUsernameAndPassword(String username, String password) throws IOException, SQLException;
	//tested
	public User findUserByEmail(String email) throws IOException, SQLException;
	//tested
	public List<User> findUserByUserType(UserType usertype) throws IOException, SQLException; 
	//tested
	public List<User> findUserByFirstname(String firstname) throws IOException, SQLException;
	//tested
	public List<User> findUserByLastname(String lastname) throws IOException, SQLException; 
	//tested
	public List<User> findUserByMajorType(MajorType major) throws IOException, SQLException; 
	//tested
	public List<User> findUserByClassType(ClassType classtype) throws IOException, SQLException;
	//tested
	public List<User> findAllUsers() throws IOException, SQLException;
	//tested
	public User findUserByName(String name) throws IOException, SQLException;
	//tested
	public User findUserByAddress(String address) throws IOException, SQLException;
	//tested
	public User findUserByNumber(String number) throws IOException, SQLException;
	
	public int findUserIDByUsernameAndPassword(String username, String password) throws IOException, SQLException;
	
	
	//PROJECTS
	
	//tested
	public Integer insertProject(int UserID, String title, String description, String start, int duration, ProjectType type
			, SolicitationType solicitationType, ArrayList<MajorType> majors, ArrayList<ClassType> classes, int numStudents, double cost, boolean isFunded, String deadline) throws IOException, SQLException;
	//IN PROGRESS
	public void deleteProject(int project_id) throws IOException, SQLException;
	//tested
	public void editTitle(int ProjectID, String title) throws IOException, SQLException;
	//tested
	public void editDescription(int ProjectID, String description) throws IOException, SQLException;
	//tested
	public void editStart(int ProjectID, String start) throws IOException, SQLException;
	//tested
	public void editDuration(int ProjectID, int duration) throws IOException, SQLException;

	//tested
	public void editCost(int project_id, double cost) throws IOException, SQLException;
	//tested
	public void editNumStudents(int project_id, int numStudents) throws IOException, SQLException;
	//tested
	public void editFunding(int project_id, boolean isFunded) throws IOException, SQLException;
	//tested
	public void editSolicitationType(int project_id, SolicitationType solicitationType) throws IOException, SQLException;

	//tested
	public List<Project> findAllProjects() throws IOException, SQLException;
	//tested
	public Project findProjectByProjectID(int ProjectID) throws IOException, SQLException;
	//tested
	public Project findProjectByTitle(String title) throws IOException, SQLException;
	//tested
	public Project findProjectByDescription(String description) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByStart(String start) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByDuration(int duration) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByProjectType(ProjectType type) throws IOException, SQLException;
	//tested
	public List<Project> findProjectBySolicitationType(SolicitationType type) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByMajorType(MajorType major) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByClassType(ClassType classtype) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByNumStudents(int numStudents) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByCost(double cost) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByIsFunded(boolean funded) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByDeadline(String deadline) throws IOException, SQLException;
	//tested
	public List<Project> findProjectByBudget(Double budget) throws IOException, SQLException;
	//tested
	public List<Pair<User, Project>> findAllUsersByProject(int ProjectID) throws IOException, SQLException;
	//tested
	public List<Pair<User, Project>> findAllProjectsByUser(int UserID) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findAllActiveProjects() throws IOException, SQLException;
	//tested
	public ActiveProject findActiveProjectByActiveProjectID(int active_project_id) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByProjectIDCopy1(int project_id_copy1) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByProjectIDCopy2(int project_id_copy2) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByTitle(String title) throws IOException, SQLException;
	
	public List<ActiveProject> findActiveProjectByDescriptionWildcard(String description) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByStart(String start) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByDuration(int duration) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByMajorType(MajorType major) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByClassType(ClassType classtype) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByNumStudents(int numStudents) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByIsFunded(boolean isFunded) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByDeadline(String deadline) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByBudget(double budget) throws IOException, SQLException;
	//tested
	public List<ActiveProject> findActiveProjectByCost(double cost) throws IOException, SQLException;
	//tested
	public void editActiveProjectTitle(int project_id, String title) throws IOException, SQLException;
	//tested
	public void editActiveProjectDescription(int project_id, String description) throws IOException, SQLException;
	//tested
	public void editActiveProjectStart(int project_id, String start) throws IOException, SQLException;
	//tested
	public void editActiveProjectDuration(int project_id, int duration) throws IOException, SQLException;
	//tested
	public void editActiveProjectNumStudents(int project_id, int numStudents) throws IOException, SQLException;
	//tested
	public void editActiveProjectCost(int project_id, double cost) throws IOException, SQLException;
	//tested
	public void editActiveProjectDeadline(int project_id, String deadline) throws IOException, SQLException;
	//tested
	public List<Project> findProjectsByTitle(String title) throws IOException, SQLException;
	
	public List<User> findUserByNameWildcard(String name) throws IOException, SQLException;
	public List<User> findUserByAddressWildcard(String address) throws IOException, SQLException;
	
	//tested
	public Integer insertActiveProject(int project_id_copy1, int project_id_copy2, String title, String description, String start, int duration,
			ProjectType projectType, ArrayList<MajorType> majors, ArrayList<ClassType> classes, int numStudents, double cost,
			boolean isFunded, String deadline, double budget) throws IOException, SQLException;
	//tested
	public void deleteActiveProject(int active_project_id) throws IOException, SQLException;
	//tested
	public List<Pair<User, ActiveProject>> findAllUsersByActiveProject(int ProjectID) throws IOException, SQLException;
	//tested
	public List<Pair<User, ActiveProject>> findAllActiveProjectsByUser(int UserID) throws IOException, SQLException;
	
	public List<Pair<Project, Project>> findAllProjectsByProjectID(int project_id) throws IOException, SQLException;
	
	public Integer insertProjectsintoProjectProjects(int project_id_copy3, int project_id_copy4) throws IOException, SQLException;
	
	public void deleteProjectFromProjectProjects(int project_id_copy3, int project_id_copy4) throws IOException, SQLException;
	
	public void editMajorType(int user_id, MajorType major) throws IOException, SQLException;
	
	public void editClassType(int user_id, ClassType classtype) throws IOException, SQLException;
	
 }
