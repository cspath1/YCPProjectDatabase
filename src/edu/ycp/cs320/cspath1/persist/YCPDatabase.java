package edu.ycp.cs320.cspath1.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.enums.ProjectType;
import edu.ycp.cs320.cspath1.enums.SolicitationType;
import edu.ycp.cs320.cspath1.enums.UserType;
import edu.ycp.cs320.cspath1.model.ActiveProjectUsers;
import edu.ycp.cs320.cspath1.model.Pair;
import edu.ycp.cs320.cspath1.model.ProjectUser;
import edu.ycp.cs320.cspath1.model.projectProject;
import edu.ycp.cs320.cspath1.project.ActiveProject;
import edu.ycp.cs320.cspath1.project.PastProject;
import edu.ycp.cs320.cspath1.project.Project;
import edu.ycp.cs320.cspath1.project.Proposal;
import edu.ycp.cs320.cspath1.project.Solicitation;
import edu.ycp.cs320.cspath1.user.Business;
import edu.ycp.cs320.cspath1.user.Faculty;
import edu.ycp.cs320.cspath1.user.Student;
import edu.ycp.cs320.cspath1.user.User;
import edu.ycp.cs320.cspath1.persist.DBUtil;

public class YCPDatabase implements IDatabase {
	static {
	try {
	Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	} catch (Exception e) {
	throw new IllegalStateException("Could not load Derby driver");
	}
	}
	
	
	
	private interface Transaction<ResultType> {
	public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;
	
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
	try {
	return doExecuteTransaction(txn);
	} catch (SQLException e) {
	throw new PersistenceException("Transaction failed", e);
	}
	}
	
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
	Connection conn = connect();
	
	try {
	int numAttempts = 0;
	boolean success = false;
	ResultType result = null;
	
	while (!success && numAttempts < MAX_ATTEMPTS) {
	try {
	result = txn.execute(conn);
	conn.commit();
	success = true;
	} catch (SQLException e) {
	if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
	// Deadlock: retry (unless max retry count has been reached)
	numAttempts++;
	} else {
	// Some other kind of SQLException
	throw e;
	}
	}
	}
	
	if (!success) {
	throw new SQLException("Transaction failed (too many retries)");
	}
	
	// Success!
	return result;
	} finally {
	DBUtil.closeQuietly(conn);
	}
	}

	private Connection connect() throws SQLException {

	Connection conn = DriverManager.getConnection("jdbc:derby:C:/Users/Cody Spath/workspace/project_database.db;create=true");

	
	// Set autocommit to false to allow multiple the execution of
	// multiple queries/statements as part of the same transaction.
	conn.setAutoCommit(true);
	
	return conn;
	}
	
	//Methods to return enums from strings
	private static MajorType getMajorTypeFromParameter(String s){
	MajorType majortype = null;
	if (s == null || s.equals("")){
	return null;
	}
	else if (s.contains("ME")){
	majortype = MajorType.ME;
	}
	else if (s.contains("CE")){
	majortype = MajorType.CE;
	}
	else if(s.contains("CS")){
	majortype = MajorType.CS;
	}
	else if(s.contains("EE")){
	majortype = MajorType.EE;
	}
	else if (s.contains("UN")) {
	majortype = MajorType.UN;
	}
	else if (s.contains("CIV")){
	majortype = MajorType.CIV;
	}
	System.out.println(majortype.toString());
	return majortype;
	}
	
	private static ArrayList<MajorType> getMajorListFromParameters(String s) {
	ArrayList<MajorType> majors = new ArrayList<MajorType>();
	if (s == null || s.equals("")){
	return null;
	}
	if(s.contains("CE")){
	majors.add(MajorType.CE);
	}
	if(s.contains("CS")){
	majors.add(MajorType.CS);
	}
	if(s.contains("EE")){
	majors.add(MajorType.EE);
	
	}
	if(s.contains("ME")) {
	majors.add(MajorType.ME);
	}
	if (s.contains("UN")) {
	majors.add(MajorType.UN);
	}
	if (s.contains("CIV")){
	majors.add(MajorType.CIV);
	}
	return majors;
	}
	
	private static ArrayList<ClassType> getClassListFromParameter(String s){
	ArrayList<ClassType> classes = new ArrayList<ClassType>();
	if (s == null || s.equals("")){
	return null;
	}
	if (s.contains("FRESHMAN")){
	classes.add(ClassType.FRESHMAN);
	}
	if (s.contains("SOPHOMORE")){
	classes.add(ClassType.SOPHOMORE);
	}
	if (s.contains("JUNIOR")){
	classes.add(ClassType.JUNIOR);
	}
	if (s.contains("SENIOR")){
	classes.add(ClassType.SENIOR);
	
	}
	return classes;
	}
	
	private static ClassType getClassTypeFromParameter(String s){
	ClassType classtype = null;
	if(s == null || s.equals("")){
	return null;
	}
	else if (s.equals("FRESHMAN")){
	classtype = ClassType.FRESHMAN;
	}
	else if (s.equals("SOPHOMORE")){
	classtype = ClassType.SOPHOMORE;
	}
	else if (s.equals("JUNIOR")){
	classtype = ClassType.JUNIOR;
	}
	else if (s.equals("SENIOR")){
	classtype = ClassType.SENIOR;
	}
	return classtype;
	}
	
	
	private static UserType getUserTypeFromParameter(String s) {
	if (s == null || s.equals("")){
	return null;
	}
	else if (s.equals("FACULTY")){
	return UserType.FACULTY;
	}
	else if (s.equals("ADMIN")){
	return UserType.ADMIN;
	}
	else if (s.equals("STUDENT")){
	return UserType.STUDENT;
	}
	else if (s.equals("BUSINESS")){
	return UserType.BUSINESS;
	}
	return null;
	}
	
	private static SolicitationType getSolicitationTypeFromParameter(String s){
	if (s == null || s.equals("")){
	return null;
	}
	else if (s.equals("SW_ENGINEERING")){
	return SolicitationType.SW_ENGINEERING;
	}
	else if (s.equals("CivE_CAPSTONE")){
	return SolicitationType.CivE_CAPSTONE;
	}
	else if (s.equals("ME_ECE_CAPSTONE")){
	return SolicitationType.ME_ECE_CAPSTONE;
	}
	else if (s.equals("CS_SENIOR_DESIGN_I")){
	return SolicitationType.CS_SENIOR_DESIGN_I;
	}
	else if (s.equals("CS_SENIOR_DESIGN_II")){
	return SolicitationType.CS_SENIOR_DESIGN_II;
	}
	else if (s.equals("ECE_CAPSTONE")){
	return SolicitationType.ECE_CAPSTONE;
	}
	else if (s.equals("ME_CAPSTONE")){
	return SolicitationType.ME_CAPSTONE;
	}
	else if (s.equals("CS_INTERNSHIP")){
	return SolicitationType.CS_INTERNSHIP;
	}
	else if (s.equals("INDEPENDENT_STUDY")){
	return SolicitationType.INDEPENDENT_STUDY;
	}
	else if (s.equals("ENGINEERING_COOP")){
	return SolicitationType.ENGINEERING_COOP;
	}
	else if (s.equals("CLASS_PROJECT")){
	return SolicitationType.CLASS_PROJECT;
	}
	return null;
	}
	
	private static ProjectType getProjectTypeFromParameter(String s){
	if (s == null || s.equals("")){
	return null;
	}
	else if (s.equals("PROPOSAL")) {
	return ProjectType.PROPOSAL;
	}
	else if (s.equals("SOLICITATION")) {
	return ProjectType.SOLICITATION;
	}
	else if (s.equals("ACTIVE")) {
	return ProjectType.ACTIVE;
	}
	else if (s.equals("PAST")) {
	return ProjectType.PAST;
	}
	return null;
	}
	
	//Method to create a user from database
	private User loadUser(User user, ResultSet resultSet) throws SQLException {
	user.setUserID(resultSet.getInt(1));
	user.setUsername(resultSet.getString(3));
	user.setPassword(resultSet.getString(4));
	user.setEmail(resultSet.getString(5));
	user.setUsertype(getUserTypeFromParameter(resultSet.getString(6)));
	if (user.getUsertype() == UserType.STUDENT) {
	Student student = new Student();
	student.setUserID(user.getUserID());
	student.setUsername(user.getUsername());
	student.setPassword(user.getPassword());
	student.setEmail(user.getEmail());
	student.setUsertype(user.getUsertype());
	student.setFirstname(resultSet.getString(7));
	student.setLastname(resultSet.getString(8));
	student.setMajor(getMajorTypeFromParameter(resultSet.getString(9)));
	student.setClassLevel(getClassTypeFromParameter(resultSet.getString(10)));
	return student;
	}
	else if (user.getUsertype() == UserType.FACULTY){
	Faculty faculty = new Faculty();
	faculty.setUserID(user.getUserID());
	faculty.setUsername(user.getUsername());
	faculty.setPassword(user.getPassword());
	faculty.setEmail(user.getEmail());
	faculty.setUsertype(user.getUsertype());
	faculty.setFirstname(resultSet.getString(7));
	faculty.setLastname(resultSet.getString(8));
	faculty.setMajor(getMajorTypeFromParameter(resultSet.getString(9)));
	return faculty;
	}
	else if (user.getUsertype() == UserType.BUSINESS) {
	Business business = new Business();
	business.setUserID(user.getUserID());
	business.setUsername(user.getUsername());
	business.setPassword(user.getPassword());
	business.setEmail(user.getEmail());
	business.setUsertype(user.getUsertype());
	business.setName(resultSet.getString(11));
	business.setAddress(resultSet.getString(12));
	business.setNumber(resultSet.getString(13));
	return business;
	}
	return null;
	
	}
	
	//Method to create a project from database
	private ActiveProject loadActiveProject(ActiveProject project, ResultSet resultSet) throws SQLException {
		project.setProjectID(resultSet.getInt(1));
		project.setProject_id_copy_1(resultSet.getInt(2));
		project.setProject_id_copy_2(resultSet.getInt(3));
		project.setTitle(resultSet.getString(4));
		project.setDescription(resultSet.getString(5));
		project.setStart(resultSet.getString(6));
		project.setDuration(resultSet.getInt(7));
		project.setProjectType(getProjectTypeFromParameter(resultSet.getString(8)));
		project.setMajors(getMajorListFromParameters(resultSet.getString(9)));
		project.setClasses(getClassListFromParameter(resultSet.getString(10)));
		project.setNumStudents(resultSet.getInt(11));
		project.setCost(resultSet.getDouble(12));
		project.setFunded(Boolean.parseBoolean(resultSet.getString(13)));
		project.setDeadline(resultSet.getString(14));
		project.setBudget(resultSet.getDouble(15));
		return project;
	}
	
	private Project loadProject(Project project, ResultSet resultSet) throws SQLException {
		project.setProjectID(resultSet.getInt(1));
		project.setTitle(resultSet.getString(8));
		project.setDescription(resultSet.getString(9));
		project.setStart(resultSet.getString(10));
		project.setDuration(resultSet.getInt(11));
		project.setProjectType(getProjectTypeFromParameter(resultSet.getString(12)));
		if (project.getProjectType() == ProjectType.SOLICITATION) {
			Solicitation solicitation = new Solicitation();
			solicitation.setProjectID(project.getProjectID());
			solicitation.setUserID(project.getUserID());
			solicitation.setTitle(project.getTitle());
			solicitation.setDescription(project.getDescription());
			solicitation.setStart(project.getStart());
			solicitation.setDuration(project.getDuration());
			solicitation.setProjectType(project.getProjectType());
			solicitation.setSolicitationType(getSolicitationTypeFromParameter(resultSet.getString(13)));
			solicitation.setMajors(getMajorListFromString(resultSet.getString(14)));
			solicitation.setClasses(getClassListFromString(resultSet.getString(15)));
			solicitation.setNumStudents(resultSet.getInt(16));
			solicitation.setCost(resultSet.getInt(17));

			return solicitation;
	}
	else if (project.getProjectType() == ProjectType.PROPOSAL) {
	Proposal proposal = new Proposal();
	proposal.setProjectID(project.getProjectID());
	proposal.setUserID(project.getUserID());
	proposal.setTitle(project.getTitle());
	proposal.setDescription(project.getDescription());
	proposal.setStart(project.getStart());
	proposal.setDuration(project.getDuration());
	proposal.setProjectType(project.getProjectType());
	proposal.setMajors(getMajorListFromString(resultSet.getString(11)));
	proposal.setClasses(getClassListFromString(resultSet.getString(12)));
	proposal.setNumStudents(resultSet.getInt(16));
	proposal.setCost(resultSet.getInt(17));
	proposal.setIsFunded(Boolean.getBoolean(resultSet.getString(18)));
	proposal.setDeadline(resultSet.getString(19));
	return proposal;
	}
	else if (project.getProjectType() == ProjectType.ACTIVE) {
	ActiveProject active = new ActiveProject();
	active.setProjectID(project.getProjectID());
	active.setUserID(project.getUserID());
	active.setTitle(project.getTitle());
	active.setDescription(project.getDescription());
	active.setStart(project.getStart());
	active.setDuration(project.getDuration());
	active.setProjectType(project.getProjectType());
	active.setNumStudents(resultSet.getInt(10));
	active.setCost(resultSet.getInt(11));
	active.setDeadline(resultSet.getString(13));
	active.setBudget(resultSet.getInt(14));
	return active;
	}
	else if (project.getProjectType() == ProjectType.PAST) {
	PastProject past = new PastProject();
	past.setProjectID(project.getProjectID());
	past.setUserID(project.getUserID());
	past.setTitle(project.getTitle());
	past.setDescription(project.getDescription());
	past.setStart(project.getStart());
	past.setDuration(project.getDuration());
	past.setProjectType(project.getProjectType());
	past.setNumStudents(resultSet.getInt(10));
	past.setCost(resultSet.getInt(11));
	past.setDeadline(resultSet.getString(13));
	past.setBudget(resultSet.getInt(14));
	return past;
	}
	return null;
	}
	
	//Methods to convert to and from strings when dealing with the database
	private String getStringFromMajorList(List<MajorType> list) {
	String majors = null;
	int index = 0;
	for (MajorType major: list) {
	major = list.get(index);
	majors = "" + major.toString() + " ";
	index++;
	}
	return majors;
	}
	
	private ArrayList<MajorType> getMajorListFromString(String s) {
	ArrayList<MajorType> majors = new ArrayList<MajorType>();
	if (s == null || s.equals("")) {
	return null;
	} else if (s.contains("CE")) {
	majors.add(MajorType.CE);
	} else if (s.contains("CS")) {
	majors.add(MajorType.CS);
	} else if (s.contains("EE")) {
	majors.add(MajorType.EE);
	} else if (s.contains("ME")) {
	majors.add(MajorType.ME);
	} else if (s.contains("CIV")) {
	majors.add(MajorType.CIV);
	} else if (s.contains("UN")) {
	majors.add(MajorType.UN);
	}
	return majors;
	}
	
	private String getStringFromClassList(List<ClassType> list) {
	String classes = null;
	int index = 0;
	for (ClassType classtype: list) {
	classtype = list.get(index);
	classes = "" + classtype.toString() + " ";
	index++;
	}
	return classes;
	}
	
	private ArrayList<ClassType> getClassListFromString(String s) {
	ArrayList<ClassType> classes = new ArrayList<ClassType>();
	if (s == null || s.equals("")) {
	return null;
	} else if (s.contains("FRESHMAN")) {
	classes.add(ClassType.FRESHMAN);
	} else if (s.contains("SOPHOMORE")) {
	classes.add(ClassType.SOPHOMORE);
	} else if (s.contains("JUNIOR")) {
	classes.add(ClassType.JUNIOR);
	} else if (s.contains("SENIOR")) {
	classes.add(ClassType.SENIOR);
	}
	return classes;
	}
	
	public void createTables() {
	executeTransaction(new Transaction<Boolean>() {
	@Override
	public Boolean execute(Connection conn) throws SQLException {
	PreparedStatement stmt1 = null;
	PreparedStatement stmt2 = null;
	PreparedStatement stmt3 = null;
	PreparedStatement stmt4 = null;
	PreparedStatement stmt5 = null;
	PreparedStatement stmt6 = null;
	PreparedStatement stmt7 = null;
	
	try {
	stmt1 = conn.prepareStatement(
	"create table users (" +
	"	user_id integer primary key " +
	"	generated always as identity (start with 1, increment by 1), " +
	"	user_id_copy integer," +
	"	username varchar(40) not null," +
	"	password varchar(40) not null," +
	"	email varchar(60) not null," +
	"	usertype varchar(10) not null," +
	"	firstname varchar(25)," +
	"	lastname varchar(25)," +
	"	major varchar(3)," +
	"	class varchar(10)," +
	"	name varchar(40)," +
	"	address varchar(100)," +
	"	contactNum varchar(20)" +
	")"
	);	
	stmt1.executeUpdate();
	
	stmt2 = conn.prepareStatement(
	"create table projects (" +
	"	project_id integer primary key " +
	"	generated always as identity (start with 1, increment by 1), " +
	"	project_id_copy1 integer," +
	"	project_id_copy2 integer," +
	"	project_id_copy3 integer," +
	"	project_id_copy4 integer," +
	"	project_id_copy5 integer," +
	"	project_id_copy6 integer," +
	"	title varchar(30) not null," +
	"	description varchar(200) not null," +
	"	start varchar(20) not null," +
	"	duration integer not null," +
	"	projectType varchar(20) not null," +
	"	solicitationType varchar(20)," +
	"	majors varchar(20)," +
	"	classes varchar(30)," +
	"	numStudents integer," +
	"	cost integer," +
	"	isFunded varchar(5)," +
	"	deadline varchar(20)," +
	"	budget integer" +
	")"
	);
	stmt2.executeUpdate();
	
	stmt3 = conn.prepareStatement(
	"create table projectUsers (" +
	"	user_id    integer constraint user_id references users," +
	"	project_id integer constraint project_id references projects" +
	")"
	);
	stmt3.executeUpdate();
	
	stmt4 = conn.prepareStatement(
	"create table project_projects (" +
	"	project_id_copy3 integer constraint project_id_copy3 references projects," +
	"	project_id_copy4 integer constraint project_id_copy4 references projects" +
	")"
	);	
	stmt4.executeUpdate();
	
	stmt5 = conn.prepareStatement(
	"create table activeProjects (" +
	"	active_project_id integer primary key " +
	"	generated always as identity (start with 1, increment by 1), " +
	"	project_id_copy1 integer constraint project_id_copy1 references projects," +
	"	project_id_copy2 integer constraint project_id_copy2 references projects," +
	"	title varchar(30) not null," +
	"	description varchar(200) not null," +
	"	start varchar(20) not null," +
	"	duration integer not null," +
	"	projectType varchar(20) not null," +
	"	majors varchar(20) not null," +
	"	classes varchar(30) not null," +
	"	numStudents integer not null," + 
	"	cost integer," + 
	"	isFunded varchar(5) not null," +
	"	deadline varchar(20) not null," +
	"	budget integer" + 
	")"
	);	
	stmt5.executeUpdate();
	
	stmt6 = conn.prepareStatement(
	"create table activeProjectUsers (" +
	"	user_id integer constraint user_id_copy references users," +
	"	active_project_id integer constraint active_project_id references activeProjects" +
	")"
	);
	stmt6.executeUpdate();
	
	stmt7 = conn.prepareStatement(
	"create table pastProjects (" +
	"	past_project_id integer primary key " +
	"	generated always as identity (start with 1, increment by 1), " +
	"	project_id_copy5 integer constraint project_id_copy5 references projects," +
	"	project_id_copy6 integer constraint project_id_copy6 references projects," +
	"	title varchar(30) not null," +
	"	description varchar(200) not null," +
	"	start varchar(20) not null," +
	"	duration integer not null," +
	"	projectType varchar(20) not null," +
	"	majors varchar(20) not null," +
	"	classes varchar(30) not null," +
	"	numStudents integer not null," + 
	"	cost integer," + 
	"	isFunded varchar(5) not null," +
	"	deadline varchar(20) not null," +
	"	budget integer" + 
	")"
	);	
	stmt7.executeUpdate();
	
	return true;
	} finally {
	DBUtil.closeQuietly(stmt1);
	DBUtil.closeQuietly(stmt2);
	DBUtil.closeQuietly(stmt3);
	DBUtil.closeQuietly(stmt4);
	DBUtil.closeQuietly(stmt5);
	DBUtil.closeQuietly(stmt6);
	}
	}
	});
	}
	
	public void loadInitialData() {
	executeTransaction(new Transaction<Boolean>() {
	@Override
	public Boolean execute(Connection conn) throws SQLException {
	List<User> userList;
	List<Project> projectList;
	List<ProjectUser> projectUserList;
	List<projectProject> projectProjectList;
	List<ActiveProjectUsers> activeProjectUsersList;
	List<ActiveProject> activeProjectList;
	List<PastProject> pastProjectList;
	try {
	userList = InitialData.getUsers();
	projectList = InitialData.getProjects();
	projectUserList = InitialData.getProjectUsers();
	projectProjectList = InitialData.getProjectProject();
	activeProjectUsersList = InitialData.getActiveProjectUsers();
	activeProjectList = InitialData.getActiveProjects();
	pastProjectList = InitialData.getPastProjects();
	} catch (IOException e) {
	throw new SQLException("Couldn't read initial data", e);
	}
	PreparedStatement insertUser = null;
	PreparedStatement insertProject = null;
	PreparedStatement insertProjectUser = null;
	PreparedStatement insertActiveProjects = null;
	PreparedStatement insertProjectProject = null;
	PreparedStatement insertActiveProjectUsers = null;
	PreparedStatement insertPastProjects = null;
	try {
	insertUser = conn.prepareStatement(
	"insert into users" +
	"	(user_id_copy, username, password, email, usertype, firstname, lastname, major, class, name, address, contactNum)" +
	"	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" 
	);
	for (User user : userList) {
	int index = 1;
	insertUser.setInt(1, index);
	insertUser.setString(2, user.getUsername());
	insertUser.setString(3, user.getPassword());
	insertUser.setString(4, user.getEmail());
	insertUser.setString(5, user.getUsertype().toString());
	if (user.getUsertype().equals(UserType.STUDENT)) {
	insertUser.setString(6, ((Student) user).getFirstname());
	insertUser.setString(7, ((Student) user).getLastname());
	insertUser.setString(8, ((Student) user).getMajor().toString());
	insertUser.setString(9, ((Student) user).getClassLevel().toString());
	} else if (user.getUsertype().equals(UserType.FACULTY)) {
	insertUser.setString(6, ((Faculty) user).getFirstname());
	insertUser.setString(7, ((Faculty) user).getLastname());
	insertUser.setString(8, ((Faculty) user).getMajor().toString());
	} else if (user.getUsertype().equals(UserType.BUSINESS)) {
	insertUser.setString(10, ((Business) user).getName());
	insertUser.setString(11, ((Business) user).getAddress());
	insertUser.setString(12, ((Business) user).getNumber());
	}
	index++;
	insertUser.addBatch();
	}
	insertUser.executeBatch();
	
	System.out.println("Users table populated");

	insertProject = conn.prepareStatement(
	"insert into projects" +
	"	(project_id_copy1, project_id_copy2, project_id_copy3, project_id_copy4, project_id_copy5, project_id_copy6, title, description, start, duration, projectType, solicitationType, majors, classes, numStudents, " +
	"	cost, isFunded, deadline)" +
	"	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
	);
	for (Project project : projectList) {
	int index = 1;
	insertProject.setInt(1, index);
	insertProject.setInt(2, index);
	insertProject.setInt(3, index);
	insertProject.setInt(4, index);
	insertProject.setInt(5, index);
	insertProject.setInt(6, index);
	insertProject.setString(7, project.getTitle());
	insertProject.setString(8, project.getDescription());
	insertProject.setString(9, project.getStart());
	insertProject.setInt(10, project.getDuration());
	insertProject.setString(11, project.getProjectType().toString());
	if (project.getProjectType().equals(ProjectType.PROPOSAL)) {
	insertProject.setString(13, ((Proposal) project).getMajors().toString());
	insertProject.setString(14, ((Proposal) project).getClasses().toString());
	insertProject.setInt(15, ((Proposal) project).getNumStudents());
	insertProject.setDouble(16, ((Proposal) project).getCost());
	insertProject.setString(17, Boolean.toString(((Proposal) project).getIsFunded()));
	insertProject.setString(18, ((Proposal) project).getDeadline());
	} else if (project.getProjectType().equals(ProjectType.SOLICITATION)) {
	insertProject.setString(12, ((Solicitation) project).getSolicitationType().toString());
	insertProject.setString(13, ((Solicitation) project).getMajors().toString());
	insertProject.setString(14, ((Solicitation) project).getClasses().toString());
	insertProject.setInt(15, ((Solicitation) project).getNumStudents());
	insertProject.setInt(16, ((int) ((Solicitation) project).getCost()));
	} 
	index++;
	insertProject.addBatch();
	}
	insertProject.executeBatch();
	
	System.out.println("Projects table populated");
	
	insertProjectUser = conn.prepareStatement(
	"insert into projectUsers" +
	"	(user_id, project_id)" +
	"	values (?, ?)"
	);
	for (ProjectUser projectUser : projectUserList) {
	insertProjectUser.setInt(1, projectUser.getUserId());
	insertProjectUser.setInt(2, projectUser.getProjectId());
	insertProjectUser.addBatch();
	}
	insertProjectUser.executeBatch();
	
	System.out.println("ProjectUsers table populated");
	
	insertProjectProject = conn.prepareStatement(
	"insert into project_projects" +
	"	(project_id_copy3, project_id_copy4)" +
	"	values (?, ?)"
	);
	for (projectProject projectRelation : projectProjectList) {
	insertProjectProject.setInt(1, projectRelation.getProject_id_1());
	insertProjectProject.setInt(2, projectRelation.getProject_id_2());
	insertProjectProject.addBatch();
	}
	insertProjectProject.executeBatch();
	
	System.out.println("ProjectProject table populated");
	
	insertActiveProjects = conn.prepareStatement(
	"insert into activeProjects" +
	"	(project_id_copy1, project_id_copy2, title, description, start, duration, projectType, majors, classes, numStudents, cost, isFunded, deadline, budget)" +
	"	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
	);
	for (ActiveProject activeProject : activeProjectList){
	insertActiveProjects.setInt(1, activeProject.getProject_id_copy_1());
	insertActiveProjects.setInt(2, activeProject.getProject_id_copy_2());
	insertActiveProjects.setString(3, activeProject.getTitle());
	insertActiveProjects.setString(4, activeProject.getDescription());
	insertActiveProjects.setString(5, activeProject.getStart());
	insertActiveProjects.setInt(6, activeProject.getDuration());
	insertActiveProjects.setString(7, activeProject.getProjectType().toString());
	insertActiveProjects.setString(8, activeProject.getMajors().toString());
	insertActiveProjects.setString(9, activeProject.getClasses().toString());
	insertActiveProjects.setInt(10, activeProject.getNumStudents());
	insertActiveProjects.setDouble(11, activeProject.getCost());
	insertActiveProjects.setString(12, Boolean.toString(activeProject.isFunded()));
	insertActiveProjects.setString(13, activeProject.getDeadline());
	insertActiveProjects.setDouble(14, activeProject.getBudget());
	insertActiveProjects.addBatch();
	}
	insertActiveProjects.executeBatch();
	System.out.println("activeProject table populated");
	
	insertActiveProjectUsers = conn.prepareStatement(
	"insert into activeProjectUsers" + 
	"	(active_project_id, user_id)" + 
	"	values(?, ?)"
	);
	for (ActiveProjectUsers activeProjectUser : activeProjectUsersList) {
	insertActiveProjectUsers.setInt(1, activeProjectUser.getActiveProjectID());
	insertActiveProjectUsers.setInt(2, activeProjectUser.getUserID());
	insertActiveProjectUsers.addBatch();
	}
	insertActiveProjectUsers.executeBatch();
	
	System.out.println("activeProjectUsers table populated");
	
	insertPastProjects = conn.prepareStatement(
	"insert into pastProjects" +
	"	(project_id_copy5, project_id_copy6, title, description, start, duration, projectType, majors, classes, numStudents, cost, isFunded, deadline, budget)" +
	"	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
	);
	for (PastProject pastProject : pastProjectList){
	insertPastProjects.setInt(1, pastProject.getProject_id_copy_5());
	insertPastProjects.setInt(2, pastProject.getProject_id_copy_6());
	insertPastProjects.setString(3, pastProject.getTitle());
	insertPastProjects.setString(4, pastProject.getDescription());
	insertPastProjects.setString(5, pastProject.getStart());
	insertPastProjects.setInt(6, pastProject.getDuration());
	insertPastProjects.setString(7, pastProject.getProjectType().toString());
	insertPastProjects.setString(8, pastProject.getMajors().toString());
	insertPastProjects.setString(9, pastProject.getClasses().toString());
	insertPastProjects.setInt(10, pastProject.getNumStudents());
	insertPastProjects.setDouble(11, pastProject.getCost());
	insertPastProjects.setString(12, Boolean.toString(pastProject.isFunded()));
	insertPastProjects.setString(13, pastProject.getDeadline());
	insertPastProjects.setDouble(14, pastProject.getBudget());
	insertPastProjects.addBatch();
	}
	insertPastProjects.executeBatch();
	
	System.out.println("pastProject table populated");
	
	return true;
	} finally {
	DBUtil.closeQuietly(insertUser);
	DBUtil.closeQuietly(insertProject);
	DBUtil.closeQuietly(insertProjectUser);
	DBUtil.closeQuietly(insertProjectProject);
	DBUtil.closeQuietly(insertActiveProjects);
	DBUtil.closeQuietly(insertActiveProjectUsers);
	DBUtil.closeQuietly(insertPastProjects);
	}
	}
	});
	}
	
	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
	System.out.println("Creating tables...");
	YCPDatabase db = new YCPDatabase();
	db.createTables();
	
	System.out.println("Loading initial data...");
	db.loadInitialData();
	
	System.out.println("Success!!");
	}

	//IN PROGRESS
	@Override
	public Integer insertUser(String username, String password, String email, UserType usertype, String firstname, String lastname
			, MajorType major, ClassType classtype, String name, String address, String contactNum) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	ResultSet resultSet2 = null;
	PreparedStatement  stmt = null;
	PreparedStatement stmt2 = null;
	PreparedStatement stmt3 = null;
	try {
	stmt = conn.prepareStatement(
	"select user_id" +
	"	from users" +
	"	where username = ? and " +
	"	password = ?"
	);
	stmt.setString(1, username);
	stmt.setString(2, password);
	
	Integer user_id = -1;
	
	resultSet = stmt.executeQuery();
	
	if(resultSet.next()) {
		user_id = resultSet.getInt(1);
	}
	else {
	stmt2 = conn.prepareStatement(
	"insert into users " +
	"	(username, password, email, usertype, firstname, lastname, major, class, name, address, contactNum)" +
	"	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
	);
	stmt2.setString(1, username);
	stmt2.setString(2, password);
	stmt2.setString(3, email);
	stmt2.setString(4, usertype.toString());
	if (!usertype.equals(UserType.BUSINESS)) {
		stmt2.setString(5, firstname);
		stmt2.setString(6, lastname);
		stmt2.setString(7, major.toString());
		stmt2.setString(8, null);
		stmt2.setString(9, null);
		stmt2.setString(10, null);
		stmt2.setString(11, null);
	}
	if (usertype.equals(UserType.STUDENT)) {
		stmt2.setString(8, classtype.toString());
	}
	if(usertype.equals(UserType.BUSINESS)) {
		stmt2.setString(5, null);
		stmt2.setString(6, null);
		stmt2.setString(7, null);
		stmt2.setString(8, null);
		stmt2.setString(9, name);
		stmt2.setString(10, address);
		stmt2.setString(11, contactNum);
	}
	
	stmt2.executeUpdate();
	
	stmt3 = conn.prepareStatement( 
	"select user_id from users" + 
	"	where username = ? and " +
	"	password = ?"
	);
	
	stmt3.setString(1, username);
	stmt3.setString(2, password);
	resultSet2 = stmt3.executeQuery();
	
	if(resultSet2.next()){
		user_id = resultSet2.getInt(1);
	}
	
	}
	return user_id;
	} finally {
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(resultSet2);
		DBUtil.closeQuietly(stmt2);
		DBUtil.closeQuietly(stmt3);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
	}
	}

	//IN PROGRESS
	@Override
	public void deleteUserAndProjects(int user_id) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt1 = null;
	PreparedStatement  stmt2 = null;
	PreparedStatement stmt3 = null;
	PreparedStatement stmt4 = null;
	PreparedStatement stmt5 = null;
	PreparedStatement stmt6 = null;
	PreparedStatement stmt7 = null;
	PreparedStatement stmt8 = null;
	PreparedStatement stmt9 = null;
	
	ResultSet resultSet1 = null;
	ResultSet resultSet2 = null;
	ResultSet resultSet3 = null;
	
	try {
	//Find the user by userID
	stmt1 = conn.prepareStatement( 
	"select users.*" +
	"	from users" + 
	"	where user_id = ?"
	);
	stmt1.setInt(1, user_id);
	resultSet1 = stmt1.executeQuery();
	List<User> users = new ArrayList<User>();
	List<Project> projects = new ArrayList<Project>();
	//Load user into a list
	while (resultSet1.next()) {
	User user = new Student();
	loadUser(user, resultSet1);
	users.add(user);
	}
	//SHOULDN'T HAPPEN
	if (users.size() == 0){
	System.out.println("Ya done goofed somehow...");
	}
	//Find projectID's for the user
	stmt3 = conn.prepareStatement(
	"select project_id" + 
	"	from projectUsers" +
	"	where user_id = ?"
	);
	stmt3.setInt(1, user_id);
	
	resultSet2 = stmt3.executeQuery();
	
	while(resultSet2.next()) {
	Project project = new Project();
	loadProject(project, resultSet2);
	projects.add((Project) projects);
	}
	//Delete entries in relation table for the user
	stmt4 = conn.prepareStatement(
	"delete from projectUsers" +
	"	where user_id = ?"
	);
	
	stmt4.setInt(1, user_id);
	
	stmt4.executeUpdate();
	//NOW I AM CONFUSED
	if (!projects.isEmpty()) {
		for (int i = 0; i < projects.size(); i++){
			stmt5 = conn.prepareStatement(
			"select projects.project_id from projects, projectUsers" +
			"	where projectUsers.user_id = ?"
			);
			stmt5.setInt(1, user_id);
			resultSet3 = stmt5.executeQuery();
			
	}
	
	if(!resultSet3.next()){
		stmt6 = conn.prepareStatement(
		"delete from users " +
		"	where user_id = ?"
		);
		
		stmt6.setInt(1, user_id);
		stmt6.executeUpdate();
		
		DBUtil.closeQuietly(stmt6);
		} 
	DBUtil.closeQuietly(resultSet3);
	DBUtil.closeQuietly(stmt5);
	}
	//ONCE WE HAVE QUERIES FOR THE NEW TABLES, WE MUST ADD TO THIS.........
	
	} finally {
	DBUtil.closeQuietly(stmt1);
	DBUtil.closeQuietly(stmt2);
	DBUtil.closeQuietly(stmt3);
	DBUtil.closeQuietly(stmt4);
	DBUtil.closeQuietly(resultSet2);
	DBUtil.closeQuietly(resultSet1);
	}
	}
	

	
	@Override
	public void editPassword(int UserID, String password) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update users" +
	"	set password = ?" +
	"	where user_id = ?"
	);
	
	stmt.setString(1, password);
	stmt.setInt(2, UserID);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}
	//IN PROGRESS
	@Override
	public void editEmail(int UserID, String email) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update users" +
	"	set email = ?" +
	"	where user_id = ?"
	);
	
	stmt.setString(1, email);
	stmt.setInt(2, UserID);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}
	
	@Override
	public void editUsername(int UserID, String username) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update users" +
	"	set username = ?" +
	"	where user_id = ?"
	);
	
	stmt.setString(1, username);
	stmt.setInt(2, UserID);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}
	
	//TESTED
	@Override
	public User findUserByUserID(int UserID) throws IOException, SQLException{
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement  stmt = null;
	//Placeholder since we can't instantiate the super
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where user_id = ?"
	);
	stmt.setInt(1, UserID);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	
	}
	
	if(!found){
	System.out.println("user_id <" + UserID + "> was not found in the user table");
	}
	return user;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	//TESTED
	@Override
	public User findUserByUsername(String username) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where username = ?"
	);
	stmt.setString(1, username);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	user = loadUser(user, resultSet);
	}
	
	if(!found){
	System.out.println("<" + username + "> was not found in the user table");
	}
	return user;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	//TESTED
	@Override
	public User findUserByUsernameAndPassword(String username, String password) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
		"select users.*" +
		"	from users" +
		"	where username = ? and password = ?"
		);
		stmt.setString(1, username);
		stmt.setString(2, password);
		
		resultSet = stmt.executeQuery();
		
		Boolean found = false;
		
		while (resultSet.next()){
			found = true;
			
			user = loadUser(user, resultSet);
			
		}
		
		if(!found){
			System.out.println("<" + username + "> and <" + password + "> were not found in the user table");
		}
		return user;
	} finally {
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
	}
	}

	//TESTED
	@Override
	public User findUserByEmail(String email) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where email = ?"
	);
	stmt.setString(1, email);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	
	}
	
	if(!found){
	System.out.println("<" + email + "> was not found in the user table");
	}
	return user;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}
	
	//TESTED
	@Override
	public List<User> findUserByUserType(UserType usertype) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	ArrayList<User> users = new ArrayList<User>();
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where usertype = ?"
	);
	stmt.setString(1, usertype.toString());
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	users.add(user);
	
	}
	
	if(!found){
	System.out.println("No users of type <" + usertype.toString() + "> were found in the user table");
	}
	return users;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<User> findUserByFirstname(String firstname) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	ArrayList<User> users = new ArrayList<User>();
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where firstname = ?"
	);
	stmt.setString(1, firstname);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	users.add(user);
	
	}
	
	if(!found){
	System.out.println("No user with the firstname <" + firstname + "> was found in the user table");
	}
	return users;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<User> findUserByLastname(String lastname) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	ArrayList<User> users = new ArrayList<User>();
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where lastname = ?"
	);
	stmt.setString(1, lastname);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	users.add(user);
	
	}
	
	if(!found){
	System.out.println("No user with the lastname <" + lastname + "> was found in the user table");
	}
	return users;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<User> findUserByMajorType(MajorType major) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	ArrayList<User> users = new ArrayList<User>();
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where major = ?"
	);
	stmt.setString(1, major.toString());
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	users.add(user);
	
	}
	
	if(!found){
	System.out.println("No user with the major <" + major + "> was found in the user table");
	}
	return users;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	//TESTED
	@Override
	public List<User> findUserByClassType(ClassType classtype) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	ArrayList<User> users = new ArrayList<User>();
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where class = ?"
	);
	stmt.setString(1, classtype.toString());
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	users.add(user);
	
	}
	
	if(!found){
	System.out.println("No user with the class <" + classtype + "> was found in the user table");
	}
	return users;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public User findUserByName(String name) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where name = ?"
	);
	stmt.setString(1, name);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	
	}
	
	if(!found){
	System.out.println("No business with the name <" + name + "> was found in the user table");
	}
	return user;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public User findUserByAddress(String address) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where address = ?"
	);
	stmt.setString(1, address);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	
	}
	
	if(!found){
	System.out.println("No business with the address <" + address + "> was found in the user table");
	}
	return user;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public User findUserByNumber(String number) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	PreparedStatement stmt = null;
	//Placeholder since we can't instantiate the super
	User user = new Student();
	try {
	stmt = conn.prepareStatement(
	"select users.*" +
	"	from users" +
	"	where contactNum = ?"
	);
	stmt.setString(1, number);
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()){
	found = true;
	
	user = loadUser(user, resultSet);
	
	}
	
	if(!found){
	System.out.println("No business with the number <" + number + "> was found in the user table");
	}
	return user;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}
	
	@Override
	public List<User> findAllUsers() throws IOException, SQLException{
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	
	try { 
	stmt = conn.prepareStatement(
	"select * from users " + 
	"	order by user_id asc"
	);
	
	List<User> result = new ArrayList<User>();
	
	resultSet = stmt.executeQuery();
	
	Boolean found = false;
	
	while (resultSet.next()) {
	found = true;
	
	User user = new Student();
	loadUser(user, resultSet);
	
	result.add(user);
	}
	
	if(!found) {
	System.out.println("No users were found in the database");
	}
	
	return result;
 	} finally {
 	DBUtil.closeQuietly(resultSet);
 	DBUtil.closeQuietly(stmt);
 	
 	}
	}

	@Override
	public Integer insertProject(int UserID, String title, String description, String start, int duration, ProjectType type
			, SolicitationType solicitationType, ArrayList<MajorType> majors, ArrayList<ClassType> classes, int numStudents, double cost, boolean isFunded, String deadline) throws IOException, SQLException {
	Connection conn = connect();
	ResultSet resultSet = null;
	ResultSet resultSet2 = null;
	PreparedStatement  stmt = null;
	PreparedStatement stmt2 = null;
	PreparedStatement stmt3 = null;
	PreparedStatement stmt4 = null;
	PreparedStatement stmt5 = null;
	Integer project_id = 0;
	try {
	stmt = conn.prepareStatement(
	"select projects.project_id" +
	"	from projectUsers, users, projects" +
	"	where users.user_id = ? and" +
	"	projects.title = ? and" +
	"	users.user_id = projectUsers.user_id and" +
	"	projects.project_id = projectUsers.project_id"
	);
	stmt.setInt(1, UserID);
	stmt.setString(2, title);
	
	resultSet = stmt.executeQuery();
	
	if(resultSet.next()) {
	System.out.println("project was found");
	project_id = resultSet.getInt(1);
	} else {
	System.out.println("project must be created");
	stmt2 = conn.prepareStatement(
	"insert into projects" +
	"	(title, description, start, duration, projectType, solicitationType, majors, classes, numStudents, cost, isFunded, deadline)" +
	"	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
	);
	stmt2.setString(1, title);
	stmt2.setString(2, description);
	stmt2.setString(3, start);
	stmt2.setInt(4, duration);
	stmt2.setString(5, type.toString());
	if (solicitationType != null) {
		stmt2.setString(6, solicitationType.toString());
	}
	else {
		stmt2.setString(6, null);
	}
	if (!majors.isEmpty()) {
		stmt2.setString(7, majors.toString());
	}
	else {
		stmt2.setString(7, null);
	}
	if (!classes.isEmpty()) {
		stmt2.setString(8, classes.toString());
	}
	else {
		stmt2.setString(8, null);
	}
	stmt2.setInt(9, numStudents);
	stmt2.setDouble(10, cost);
	stmt2.setString(11, Boolean.toString(isFunded));
	if (deadline == null) {
		stmt2.setString(12, null);
	}
	else {
		stmt2.setString(12, deadline);
	}
	
	stmt2.execute();
	
	stmt3 = conn.prepareStatement(
	"select project_id from projects" +
	"	where title = ?"
	);
	stmt3.setString(1, title);
	
	resultSet2 = stmt3.executeQuery();
	
	if(resultSet2.next()) {
	System.out.println("project was inserted and id retrieved");
	project_id = resultSet2.getInt(1);
	}
	}
	
	stmt4 = conn.prepareStatement(
	"insert into projectUsers" +
	"	(user_id, project_id)" +
	"	values (?, ?)"
	);
	stmt4.setInt(1, UserID);
	stmt4.setInt(2, project_id);
	
	stmt4.execute();
	
	stmt5 = conn.prepareStatement(
	"update projects" + 
	"	set project_id_copy1 = ?," +
	"	project_id_copy2 = ?," +
	"	project_id_copy3 = ?," +
	"	project_id_copy4 = ?," +
	"	project_id_copy5 = ?," +
	"	project_id_copy6 = ?" + 
	"	where project_id = ?"
	);
	stmt5.setInt(1, project_id);
	stmt5.setInt(2, project_id);
	stmt5.setInt(3, project_id);
	stmt5.setInt(4, project_id);
	stmt5.setInt(5, project_id);
	stmt5.setInt(6, project_id);
	stmt5.setInt(7, project_id);
	
	stmt5.execute();
	return project_id;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(resultSet2);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(stmt2);
	DBUtil.closeQuietly(stmt3);
	DBUtil.closeQuietly(stmt4);
	DBUtil.closeQuietly(stmt5);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public void deleteProject(int project_id) throws IOException, SQLException {
	Connection conn = connect();

	PreparedStatement  stmt1 = null;
	PreparedStatement stmt2 = null;
	PreparedStatement stmt3 = null;
	PreparedStatement stmt4 = null;
	
	ResultSet resultSet1 = null;
	ResultSet resultSet2 = null;
	
	

	try {

	stmt1 = conn.prepareStatement(
	"select users.* " +
	"	from users, projects, projectUsers " +
	"	where projects.project_id = projectUsers.project_id " +
	"	and users.user_id = projectUsers.user_id" +
	"	and projects.project_id = ?"
	);
	stmt1.setInt(1, project_id);
	
	resultSet1 = stmt1.executeQuery();
	
	List<User> users = new ArrayList<User>();
	
	while (resultSet1.next()) {
	User user = new Student();
	loadUser(user, resultSet1);
	users.add(user);
	}
	
	if (users.size() == 0) {
	System.out.println("This should not have happened...");
	}
	
	stmt2 = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where projects.project_id = ?"
	);
	
	stmt2.setInt(1, project_id);
	
	resultSet2 = stmt2.executeQuery(); 
	
	List<Project> projects = new ArrayList<Project>();
	
	while(resultSet2.next()) {
	Project project = new Proposal();
	loadProject(project, resultSet2);
	projects.add(project);
	}
	

	
	
	stmt3 = conn.prepareStatement(
	"delete from projectUsers " +
	"	where project_id = ?"
	);

	
	stmt3.setInt(1, projects.get(0).getProjectID());
	stmt3.executeUpdate();

	stmt4 = conn.prepareStatement(
	"delete from projects" + 
	"	where project_id = ?"
	);

	stmt4.setInt(1, project_id);
	stmt4.executeUpdate();
	} finally {


	DBUtil.closeQuietly(stmt1);
	DBUtil.closeQuietly(stmt2);
	DBUtil.closeQuietly(stmt3);
	DBUtil.closeQuietly(stmt4);
	DBUtil.closeQuietly(resultSet1);
	DBUtil.closeQuietly(resultSet2);

	DBUtil.closeQuietly(conn);
	}
	
	}

	@Override
	public void editTitle(int ProjectID, String title) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set title = ?" +
	"	where project_id = ?"
	);
	
	stmt.setString(1, title);
	stmt.setInt(2, ProjectID);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public void editDescription(int ProjectID, String description) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set description = ?" +
	"	where project_id = ?"
	);
	
	stmt.setString(1, description);
	stmt.setInt(2, ProjectID);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public void editStart(int ProjectID, String start) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set start = ?" +
	"	where project_id = ?"
	);
	
	stmt.setString(1, start);
	stmt.setInt(2, ProjectID);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public void editDuration(int ProjectID, int duration) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set duration = ?" +
	"	where project_id = ?"
	);
	
	stmt.setInt(1, duration);
	stmt.setInt(2, ProjectID);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findAllProjects() throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		
		try { 
			stmt = conn.prepareStatement(
			"select * from projects " + 
			"	order by title"
			);
			
			List<Project> result = new ArrayList<Project>();
			
			resultSet = stmt.executeQuery();
			
			Boolean found = false;
			
			while (resultSet.next()) {
				found = true;
				
				Project project = new Proposal();
				loadProject(project, resultSet);
				
				result.add(project);
			}
			
			if(!found) {
				System.out.println("No projects were found in the database");
			}
		return result;
	 	} finally {
		 	DBUtil.closeQuietly(resultSet);
		 	DBUtil.closeQuietly(stmt);	
	 	}
		}
	
		@Override
		public Project findProjectByProjectID(int ProjectID) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Project project = new Proposal();
		try {
		stmt = conn.prepareStatement(
		"select projects.*" +
		"	from projects" +
		"	where project_id = ?"
		);
		stmt.setInt(1, ProjectID);
		
		resultSet = stmt.executeQuery();
		if (resultSet.next()) {
		loadProject(project, resultSet);
		}
	
		return project;
		} finally {
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public Project findProjectByTitle(String title) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where title like '%' || ? || '%'"
	);
	stmt.setString(1, title);
	
	resultSet = stmt.executeQuery();
	if (resultSet.next()) {
	loadProject(project, resultSet);
	}
	return project;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}
	
	@Override
	public List<Project> findProjectsByTitle(String title) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where title like '%' || ? || '%'"
	);
	stmt.setString(1, title);
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public Project findProjectByDescription(String description) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where description = ?"
	);
	stmt.setString(1, description);
	
	resultSet = stmt.executeQuery();
	if (resultSet.next()) {
	loadProject(project, resultSet);
	}
	return project;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByStart(String start) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where start = ?"
	);
	stmt.setString(1, start);
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByDuration(int duration) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<Project> projectList = new ArrayList<Project>();
		Project project = new Proposal();
		try {
			stmt = conn.prepareStatement(
			"select projects.*" +
			"	from projects" +
			"	where duration = ?"
			);
			stmt.setInt(1, duration);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
			project = loadProject(project, resultSet);
			projectList.add(project);
			}
		return projectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<Project> findProjectByProjectType(ProjectType type) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where projectType = ?"
	);
	stmt.setString(1, type.toString());
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectBySolicitationType(SolicitationType type) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where solicitationType = ?"
	);
	stmt.setString(1, type.toString());
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByMajorType(MajorType major) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<Project> projectList = new ArrayList<Project>();
		Project project = new Proposal();
		try {
			stmt = conn.prepareStatement(
			"select projects.*" +
			"	from projects" +
			"	where majors like '%' || ? || '%'"
		);
		stmt.setString(1, major.toString());
		
		resultSet = stmt.executeQuery();
		while (resultSet.next()) {
			project = loadProject(project, resultSet);
			projectList.add(project);
		}
		return projectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<Project> findProjectByClassType(ClassType classtype) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where classes like '%' || ? || '%'"
	);
	stmt.setString(1, classtype.toString());
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByNumStudents(int numStudents) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where numStudents = ?"
	);
	stmt.setInt(1, numStudents);
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByCost(double cost) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where cost = ?"
	);
	stmt.setDouble(1, cost);
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByIsFunded(boolean funded) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where isFunded = ?"
	);
	stmt.setString(1, Boolean.toString(funded));
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByDeadline(String deadline) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where deadline = ?"
	);
	stmt.setString(1, deadline);
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Project> findProjectByBudget(Double budget) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	List<Project> projectList = new ArrayList<Project>();
	Project project = new Proposal();
	try {
	stmt = conn.prepareStatement(
	"select projects.*" +
	"	from projects" +
	"	where budget = ?"
	);
	stmt.setDouble(1, budget);
	
	resultSet = stmt.executeQuery();
	while (resultSet.next()) {
	project = loadProject(project, resultSet);
	projectList.add(project);
	}
	return projectList;
	} finally {
	DBUtil.closeQuietly(resultSet);
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	
	public List<Pair<User, Project>> findAllUsersByProject(int ProjectID) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		List<Pair<User, Project>> list = new ArrayList<Pair<User, Project>>();
		User user = new Student();
		Project project = new Proposal();
		try {
			stmt = conn.prepareStatement(
				"select projects.*" +
				"	from projects" +
				"	where project_id = ?"
			);
			stmt.setInt(1, ProjectID);
			
			resultSet = stmt.executeQuery();
			
			stmt2 = conn.prepareStatement(
				"select users.*" +
				"	from projects, users, projectUsers" +
				"	where projects.project_id = projectUsers.project_id and" +
				"	users.user_id = projectUsers.user_id and" +
				"	projects.project_id = ?" +
				"	order by users.user_id"
				);
			stmt2.setInt(1, ProjectID);
			
			resultSet2 = stmt2.executeQuery();
			
			Boolean found = false;
			
			if (resultSet.next()) {
			found = true;
			
			project = loadProject(project, resultSet);
			}
			while (resultSet2.next()) {
				found = true;
				
				user = loadUser(user, resultSet2);
				
				list.add(new Pair<User, Project>(user, project));
			}
			
			if (!found) {
				System.out.println("No project with project id <" + ProjectID + "> was found in the database");
			}
			
			return list;
	} finally { 
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(resultSet2);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(stmt2);
		DBUtil.closeQuietly(conn);
	}
	}

	public List<Pair<User, Project>> findAllProjectsByUser(int UserID) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		List<Pair<User, Project>> list = new ArrayList<Pair<User, Project>>();
		User user = new Student();
		Project project = new Proposal();
		try {
		stmt = conn.prepareStatement(
		"select users.*" +
		"	from users" +
		"	where user_id = ?"
		);
		stmt.setInt(1, UserID);
		
		resultSet = stmt.executeQuery();
		
		stmt2 = conn.prepareStatement(
		"select projects.*" +
		"	from projects, users, projectUsers" +
		"	where projects.project_id = projectUsers.project_id and" +
		"	users.user_id = projectUsers.user_id and" +
		"	users.user_id = ?" +
		"	order by projects.project_id"
		);
		stmt2.setInt(1, UserID);
		
		resultSet2 = stmt2.executeQuery();
		
		Boolean found = false;
		
		if (resultSet.next()) {
		found = true;
		
		user = loadUser(user, resultSet);
		}
		while (resultSet2.next()) {
		found = true;
		
		project = loadProject(project, resultSet2);
		
		list.add(new Pair<User, Project>(user, project));
		}
		
		if (!found) {
		System.out.println("No user with user id <" + UserID + "> was found in the database");
		}
		
		return list;
		} finally { 
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(resultSet2);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(stmt2);
		DBUtil.closeQuietly(conn);
		}
		}
	
		@Override
		public void editActiveProjectTitle(int project_id, String title) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement  stmt = null;
		try {
		stmt = conn.prepareStatement(
		"update activeProjects" +
		"	set title = ?" +
		"	where active_project_id = ?"
		);
		
		stmt.setString(1, title);
		stmt.setInt(2, project_id);
		
		stmt.executeUpdate();
		} finally {
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
		}
		
	}

	@Override
	public void editActiveProjectDescription(int project_id, String description) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update activeProjects" +
	"	set description = ?" +
	"	where active_project_id = ?"
	);
	
	stmt.setString(1, description);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	
	}

	@Override
	public void editActiveProjectStart(int project_id, String start) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update activeProjects" +
	"	set start = ?" +
	"	where active_project_id = ?"
	);
	
	stmt.setString(1, start);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	
	}

	@Override
	public void editActiveProjectDuration(int project_id, int duration) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update activeProjects" +
	"	set duration = ?" +
	"	where active_project_id = ?"
	);
	
	stmt.setInt(1, duration);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	
	
	}

	@Override
	public void editActiveProjectNumStudents(int project_id, int numStudents) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update activeProjects" +
	"	set numStudents = ?" +
	"	where active_project_id = ?"
	);
	
	stmt.setInt(1, numStudents);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	
	}

	@Override
	public void editActiveProjectCost(int project_id, double cost) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update activeProjects" +
	"	set cost = ?" +
	"	where active_project_id = ?"
	);
	
	stmt.setDouble(1, cost);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	
	}

	@Override
	public void editActiveProjectDeadline(int project_id, String deadline) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update activeProjects" +
	"	set deadline = ?" +
	"	where active_project_id = ?"
	);
	
	stmt.setString(1, deadline);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	
	}

	@Override
	public void editCost(int project_id, double cost) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set cost = ?" +
	"	where project_id = ?"
	);
	
	stmt.setDouble(1, cost);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public void editNumStudents(int project_id, int numStudents) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set numStudents = ?" +
	"	where project_id = ?"
	);
	
	stmt.setInt(1, numStudents);
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public void editFunding(int project_id, boolean isFunded) throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set isFunded = ?" +
	"	where project_id = ?"
	);
	
	stmt.setString(1, Boolean.toString(isFunded));
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public void editSolicitationType(int project_id, SolicitationType solicitationType)
	throws IOException, SQLException {
	Connection conn = connect();
	PreparedStatement  stmt = null;
	try {
	stmt = conn.prepareStatement(
	"update projects" +
	"	set solicitationType = ?" +
	"	where project_id = ?"
	);
	
	stmt.setString(1, solicitationType.toString());
	stmt.setInt(2, project_id);
	
	stmt.executeUpdate();
	} finally {
	DBUtil.closeQuietly(stmt);
	DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<ActiveProject> findAllActiveProjects() throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		
		try { 
			stmt = conn.prepareStatement(
			"select * from activeProjects " + 
			"	order by title"
			);
			
			List<ActiveProject> result = new ArrayList<ActiveProject>();
			
			resultSet = stmt.executeQuery();
			
			Boolean found = false;
			
			while (resultSet.next()) {
				found = true;
				
				ActiveProject project = new ActiveProject();
				loadActiveProject(project, resultSet);
				
				result.add(project);
			}
			
			if(!found) {
				System.out.println("No projects were found in the database");
			}
		return result;
	 	} finally {
		 	DBUtil.closeQuietly(resultSet);
		 	DBUtil.closeQuietly(stmt);	
	 	}
	}

	@Override
	public ActiveProject findActiveProjectByActiveProjectID(int active_project_id)
			throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where active_project_id = ?"
			);
			stmt.setInt(1, active_project_id);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return project;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByProjectIDCopy1(int project_id_copy1)
			throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where project_id_copy1 = ?"
			);
			stmt.setInt(1, project_id_copy1);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByProjectIDCopy2(int project_id_copy2)
			throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where project_id_copy2 = ?"
			);
			stmt.setInt(1, project_id_copy2);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByTitle(String title) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where title = ?"
			);
			stmt.setString(1, title);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByDescriptionWildcard(String description) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where description = ?"
			);
			stmt.setString(1, description);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByStart(String start) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where start = ?"
			);
			stmt.setString(1, start);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByDuration(int duration) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where duration = ?"
			);
			stmt.setInt(1, duration);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByMajorType(MajorType major) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> projectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where majors like '%' || ? || '%'"
		);
		stmt.setString(1, major.toString());
		
		resultSet = stmt.executeQuery();
		while (resultSet.next()) {
			project = loadActiveProject(project, resultSet);
			projectList.add(project);
		}
		return projectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByClassType(ClassType classtype) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> projectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where classes like '%' || ? || '%'"
		);
		stmt.setString(1, classtype.toString());
		
		resultSet = stmt.executeQuery();
		while (resultSet.next()) {
			project = loadActiveProject(project, resultSet);
			projectList.add(project);
		}
		return projectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByNumStudents(int numStudents) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where numStudents = ?"
			);
			stmt.setInt(1, numStudents);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByIsFunded(boolean isFunded) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where isFunded = ?"
			);
			stmt.setString(1, Boolean.toString(isFunded));
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByDeadline(String deadline) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where deadline = ?"
			);
			stmt.setString(1, deadline);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByBudget(double budget) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where budget = ?"
			);
			stmt.setDouble(1, budget);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<ActiveProject> findActiveProjectByCost(double cost) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<ActiveProject> activeProjectList = new ArrayList<ActiveProject>();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
			"select activeProjects.*" +
			"	from activeProjects" +
			"	where cost = ?"
			);
			stmt.setDouble(1, cost);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				project = loadActiveProject(project, resultSet);
				activeProjectList.add(project);
			}
		return activeProjectList;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public Integer insertActiveProject(int project_id_copy1, int project_id_copy2, String title, String description,
			String start, int duration, ProjectType projectType, ArrayList<MajorType> majors,
			ArrayList<ClassType> classes, int numStudents, double cost, boolean isFunded, String deadline,
			double budget) throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		try {
			stmt1 = conn.prepareStatement(
					"insert into activeProjects" +
					"	(project_id_copy1, project_id_copy2, title, description, start, duration," +
					"	projectType, majors, classes, numStudents, cost, isFunded, deadline, budget)" +
					"	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" 
					);
			stmt1.setInt(1, project_id_copy1);
			stmt1.setInt(2, project_id_copy2);
			stmt1.setString(3, title);
			stmt1.setString(4, description);
			stmt1.setString(5, start);
			stmt1.setInt(6, duration);
			stmt1.setString(7, projectType.toString());
			stmt1.setString(8, majors.toString());
			stmt1.setString(9,  classes.toString());
			stmt1.setInt(10, numStudents);
			stmt1.setDouble(11, cost);
			stmt1.setString(12, Boolean.toString(isFunded));
			stmt1.setString(13, deadline);
			stmt1.setDouble(14, budget);
			
			stmt1.executeUpdate();
			
			stmt2 = conn.prepareStatement(
					"select active_project_id" +
					"	from activeProjects" +
					"	where project_id_copy1 = ? and " +
					"	project_id_copy2 = ? and" +
					"	title = ?"
					);
			stmt2.setInt(1, project_id_copy1);
			stmt2.setInt(2, project_id_copy2);
			stmt2.setString(3, title);
			
			resultSet1 = stmt2.executeQuery();
			int active_project_id = -1;
			if(resultSet1.next()) {
				active_project_id = resultSet1.getInt(1);
			}
			
			else {
				System.out.println("Something has gone horribly wrong");
			}
			
			return active_project_id;
			/*stmt3 = conn.prepareStatement(
					"select from project_projects" + 
					"	from activeProjects, project_projects" +
					"	where (activeProjects.project_id_copy1 = project_projects.project_id_copy3 or" +
					"	activeProjects.project_id_copy1 = project_projects.project_id_copy4) and" +
					"	(activeProjects.project_id_copy2 = project_projects.project_id_copy3 or" +
					"	activeProjects.project_id_copy2 = project_projects.project_id_copy4) and " +
					"	activeProjects.project_id_copy1 = ? and" +
					"	activeProjects.project_id_copy2 = ?"
					);
			
			stmt3.setInt(1, project_id_copy1);
			stmt3.setInt(2, project_id_copy2);
			
			resultSet2 = stmt3.executeQuery();
			
			if (resultSet2.next()) {
				stmt4 = conn.prepareStatement(
						)
			}*/
		} finally {
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(stmt1);
			DBUtil.closeQuietly(stmt2);
			DBUtil.closeQuietly(resultSet1);
		}
		
	}

	@Override
	public void deleteActiveProject(int active_project_id) throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		List<ActiveProject> activeProjects = new ArrayList<ActiveProject>();
		List<User> users = new ArrayList<User>();
		try {
			stmt1 = conn.prepareStatement(
					"select users.*" +
					"	from activeProjects, activeProjectUsers, users" +
					"	where activeProjects.active_project_id = activeProjectUsers.active_project_id" +
					"	and users.user_id_copy = activeProjectUsers.user_id" +
					"	and activeProjects.active_project_id = ?"
					);
			stmt1.setInt(1, active_project_id);
			
			resultSet1 = stmt1.executeQuery();
			
			while (resultSet1.next()) {
				User user = new Student();
				loadUser(user, resultSet1);
				users.add(user);
			}
			
			if (users.size() == 0) {
				System.out.println("This should not have happened...");
				}
			
			stmt2 = conn.prepareStatement(
					"select activeProjects.*" +
					"	from activeProjects" +
					"	where active_project_id = ?"
					);
			
			stmt2.setInt(1, active_project_id);
			
			resultSet2 = stmt2.executeQuery();
			
			while (resultSet2.next()) {
				ActiveProject activeProject = new ActiveProject();
				loadActiveProject(activeProject, resultSet2);
				activeProjects.add(activeProject);
			}
			
			for(User user : users) {
				stmt3 = conn.prepareStatement(
						"delete from activeProjectUsers" +
						"	where user_id = ?"
					);
				
				stmt3.setInt(1, user.getUserID());
				
				stmt3.executeUpdate();
			}
			
			stmt4 = conn.prepareStatement(
					"delete from activeProjects" +
					"	where active_project_id = ?"
					);
			
			stmt4.setInt(1, active_project_id);
			
			stmt4.executeUpdate();
		} finally {
			DBUtil.closeQuietly(stmt1);
			DBUtil.closeQuietly(stmt2);
			DBUtil.closeQuietly(stmt3);
			DBUtil.closeQuietly(stmt4);
			DBUtil.closeQuietly(resultSet1);
			DBUtil.closeQuietly(resultSet2);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<Pair<User, ActiveProject>> findAllUsersByActiveProject(int ProjectID) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		List<Pair<User, ActiveProject>> list = new ArrayList<Pair<User, ActiveProject>>();
		User user = new Student();
		ActiveProject project = new ActiveProject();
		try {
			stmt = conn.prepareStatement(
				"select activeProjects.*" +
				"	from activeProjects" +
				"	where active_project_id = ?"
			);
			stmt.setInt(1, ProjectID);
			
			resultSet = stmt.executeQuery();
			
			stmt2 = conn.prepareStatement(
				"select users.*" +
				"	from activeProjects, users, activeProjectUsers" +
				"	where activeProjects.active_project_id = activeProjectUsers.active_project_id and" +
				"	users.user_id_copy = activeProjectUsers.user_id and" +
				"	activeProjects.active_project_id = ?" +
				"	order by users.user_id"
				);
			stmt2.setInt(1, ProjectID);
			
			resultSet2 = stmt2.executeQuery();
			
			Boolean found = false;
			
			if (resultSet.next()) {
				found = true;
				
				project = loadActiveProject(project, resultSet);
			}
			while (resultSet2.next()) {
				found = true;
				
				user = loadUser(user, resultSet2);
				
				list.add(new Pair<User, ActiveProject>(user, project));
			}
			
			if (!found) {
				System.out.println("No project with project id <" + ProjectID + "> was found in the database");
			}
			
			return list;
	} finally { 
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(resultSet2);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(stmt2);
		DBUtil.closeQuietly(conn);
	}
	}

	@Override
	public List<Pair<User, ActiveProject>> findAllActiveProjectsByUser(int UserID) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		List<Pair<User, ActiveProject>> list = new ArrayList<Pair<User, ActiveProject>>();
		User user = new Student();
		ActiveProject project = new ActiveProject();
		try {
		stmt = conn.prepareStatement(
		"select users.*" +
		"	from users" +
		"	where user_id_copy = ?"
		);
		stmt.setInt(1, UserID);
		
		resultSet = stmt.executeQuery();
		
		stmt2 = conn.prepareStatement(
		"select activeProjects.*" +
		"	from activeProjects, users, activeProjectUsers" +
		"	where activeProjects.active_project_id = activeProjectUsers.active_project_id and" +
		"	users.user_id_copy = activeProjectUsers.user_id and" +
		"	users.user_id = ?" +
		"	order by activeProjects.active_project_id"
		);
		stmt2.setInt(1, UserID);
		
		resultSet2 = stmt2.executeQuery();
		
		Boolean found = false;
		
		if (resultSet.next()) {
		found = true;
		
		user = loadUser(user, resultSet);
		}
		while (resultSet2.next()) {
		found = true;
		
		project = loadActiveProject(project, resultSet2);
		
		list.add(new Pair<User, ActiveProject>(user, project));
		}
		
		if (!found) {
		System.out.println("No user with user id <" + UserID + "> was found in the database");
		}
		
		return list;
		} finally { 
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(resultSet2);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(stmt2);
		DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<Pair<Project, Project>> findAllProjectsByProjectID(int project_id)
			throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		Project project1 = new Proposal();
		Project project2 = new Proposal();
		Integer project_id1 = null;
		Integer project_id2 = null;
		int index = 1;
		List<Pair<Project, Project>> projectsList = new ArrayList<Pair<Project, Project>>();
		try {
			stmt1 = conn.prepareStatement(
					"select * from project_projects " +
					"	where project_id_copy3 = ? or " +
					"	project_id_copy4 = ?"
					);
			
			stmt1.setInt(1, project_id);
			stmt1.setInt(2, project_id);
			
			resultSet1 = stmt1.executeQuery();
			
			while (resultSet1.next()) {
				project_id1 = resultSet1.getInt(index++);
				project_id2 = resultSet1.getInt(index++);	
				
				stmt2 = conn.prepareStatement(
						"select * from projects " +
						"	where project_id = ?"
						);
				stmt2.setInt(1, project_id1);
				
				resultSet2 = stmt2.executeQuery();
				
				if (resultSet2.next()) {
					loadProject(project1, resultSet2);
				}
				else {
					System.out.println("Something went wrong boys...");
				}
				
				stmt3 = conn.prepareStatement(
						"select * from projects " +
						"	where project_id = ?"
						);
				stmt3.setInt(1, project_id2);
				
				resultSet3 = stmt3.executeQuery();
				
				if (resultSet3.next()) {
					loadProject(project2, resultSet3);
				}
				else {
					System.out.println("Something went wrong boys...");
				}
				Pair<Project, Project> projects = new Pair<Project, Project>(project1, project2);
				projectsList.add(projects);
			}
			return projectsList;
			
		} finally {
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(resultSet1);
			DBUtil.closeQuietly(resultSet2);
			DBUtil.closeQuietly(resultSet3);
			DBUtil.closeQuietly(stmt1);
			DBUtil.closeQuietly(stmt2);
			DBUtil.closeQuietly(stmt3);
		}
	}

	@Override
	public int findUserIDByUsernameAndPassword(String username, String password) throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		int user_id = -1;
		//Placeholder since we can't instantiate the super
		try {
		stmt = conn.prepareStatement(
			"select user_id" +
			"	from users" +
			"	where username = ? and password = ?"
			);
			stmt.setString(1, username);
			stmt.setString(2, password);
			
			resultSet = stmt.executeQuery();
			
			Boolean found = false;
			
			while (resultSet.next()){
				found = true;
				user_id = resultSet.getInt(1);
			}
			
			if(!found){
				System.out.println("<" + username + "> and <" + password + "> were not found in the user table");
			}
			return user_id;
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<User> findUserByNameWildcard(String name) throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		//Placeholder since we can't instantiate the super
		User user = new Student();
		List<User> users = new ArrayList<User>();
		try {
			stmt = conn.prepareStatement(
				"select users.*" +
				"	from users" +
				"	where name like '%' || ? || '%'"
				);
			stmt.setString(1, name);
			
			resultSet = stmt.executeQuery();
			
			Boolean found = false;
			
			while (resultSet.next()){
				found = true;
				user = loadUser(user, resultSet);
				users.add(user);
			}
			
			if(!found){
				System.out.println("No business with the name <" + name + "> was found in the user table");
			}
			return users;
		} finally {
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public List<User> findUserByAddressWildcard(String address) throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		//Placeholder since we can't instantiate the super
		User user = new Student();
		List<User> users = new ArrayList<User>();
		try {
			stmt = conn.prepareStatement(
				"select users.*" +
				"	from users" +
				"	where address like '%' || ? || '%'"
				);
			stmt.setString(1, address);
			
			resultSet = stmt.executeQuery();
			
			Boolean found = false;
			
			while (resultSet.next()){
				found = true;
				user = loadUser(user, resultSet);
				users.add(user);
			}
			
			if(!found){
				System.out.println("No business with the name <" + address + "> was found in the user table");
			}
			return users;
		} finally {
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
		}
	}

	@Override
	public Integer insertProjectsintoProjectProjects(int project_id_copy3, int project_id_copy4)
			throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		int project_id = -1;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		try {
			stmt1 = conn.prepareStatement(
					"select * from project_projects " +
					"	where (project_id_copy3 = ? and " +
					"	project_id_copy4 = ?) or (project_id_copy3 = ? " +
					"	and project_id_copy4 = ?)"
					);
			
			stmt1.setInt(1, project_id_copy3);
			stmt1.setInt(2, project_id_copy4);
			stmt1.setInt(3, project_id_copy4);
			stmt1.setInt(4, project_id_copy3);
			
			resultSet1 = stmt1.executeQuery();
			
			if(resultSet1.next()) {
				System.out.println("Those projects are already paired...");
				project_id = resultSet1.getInt(1);
			}
			
			else {
				stmt2 = conn.prepareStatement(
						"insert into project_projects(project_id_copy3, project_id_copy4) " +
						"	values(?, ?)"
						);
				stmt2.setInt(1, project_id_copy3);
				stmt2.setInt(2, project_id_copy4);
				
				stmt2.executeUpdate();
				
				stmt3 = conn.prepareStatement(
						"select project_id_copy3 from project_projects " +
							"	where (project_id_copy3 = ? and " +
							"	project_id_copy4 = ?) or (project_id_copy3 = ? " +
							"	and project_id_copy4 = ?)"
					);
				
				stmt3.setInt(1, project_id_copy3);
				stmt3.setInt(2, project_id_copy4);
				stmt3.setInt(3, project_id_copy4);
				stmt3.setInt(4, project_id_copy3);
			
				resultSet2 = stmt3.executeQuery();
				
				if (resultSet2.next()) {
					project_id = resultSet2.getInt(1);
				}
				else {
					System.out.println("Something has gone horribly wrong...");
				}
			}
			
			return project_id;
			
		} finally {
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(resultSet1);
			DBUtil.closeQuietly(resultSet2);
			DBUtil.closeQuietly(stmt1);
			DBUtil.closeQuietly(stmt2);
			DBUtil.closeQuietly(stmt3);
		}
		
		
		
		
	}

	@Override
	public void deleteProjectFromProjectProjects(int project_id_copy3, int project_id_copy4)
			throws IOException, SQLException {
		Connection conn = connect();
		ResultSet resultSet1 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		
		try {
			stmt1 = conn.prepareStatement(
					"select * from project_projects " +
					"	where (project_id_copy3 = ? and " +
					"	project_id_copy4 = ?) or (project_id_copy3 = ? " +
					"	and project_id_copy4 = ?)"
					);
			
			stmt1.setInt(1, project_id_copy3);
			stmt1.setInt(2, project_id_copy4);
			stmt1.setInt(3, project_id_copy4);
			stmt1.setInt(4, project_id_copy3);
			
			resultSet1 = stmt1.executeQuery();
			
			if (resultSet1.next()) {
				stmt2 = conn.prepareStatement(
						"delete from project_projects " +
							"	where (project_id_copy3 = ? and " +
							"	project_id_copy4 = ?) or (project_id_copy3 = ? " +
							"	and project_id_copy4 = ?)"
						);
				stmt2.setInt(1, project_id_copy3);
				stmt2.setInt(2, project_id_copy4);
				stmt2.setInt(3, project_id_copy4);
				stmt2.setInt(4, project_id_copy3);
				
				stmt2.executeUpdate();
				
			}
			
			
			else {
				System.out.println("Relation does not exist in the database...");
			}
		} finally {
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(stmt1);
			DBUtil.closeQuietly(stmt2);
			DBUtil.closeQuietly(resultSet1);
		}
		
	}

	@Override
	public void editMajorType(int user_id, MajorType major) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement  stmt = null;
		try {
			stmt = conn.prepareStatement(
				"update users" +
				"	set major = ?" +
				"	where user_id = ?"
			);
			
			stmt.setString(1, major.toString());
			stmt.setInt(2, user_id);
			
			stmt.executeUpdate();
		} finally {
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
		
	}

	@Override
	public void editClassType(int user_id, ClassType classtype) throws IOException, SQLException {
		Connection conn = connect();
		PreparedStatement  stmt = null;
		try {
			stmt = conn.prepareStatement(
				"update users" +
				"	set class = ?" +
				"	where user_id = ?"
			);
			
			stmt.setString(1, classtype.toString());
			stmt.setInt(2, user_id);
			
			stmt.executeUpdate();
		} finally {
			DBUtil.closeQuietly(stmt);
			DBUtil.closeQuietly(conn);
		}
	}

	
}