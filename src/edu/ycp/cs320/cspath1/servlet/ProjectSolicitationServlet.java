package edu.ycp.cs320.cspath1.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.enums.ProjectType;
import edu.ycp.cs320.cspath1.enums.SolicitationType;
import edu.ycp.cs320.cspath1.model.ProjectModel;
import edu.ycp.cs320.cspath1.persist.DatabaseProvider;
import edu.ycp.cs320.cspath1.persist.IDatabase;
import edu.ycp.cs320.cspath1.persist.YCPDatabase;
//import edu.ycp.cs320.cspath1.persist.FakeDatabase;
import edu.ycp.cs320.cspath1.project.Solicitation;
import edu.ycp.cs320.cspath1.user.User;

public class ProjectSolicitationServlet extends HttpServlet{
private static final long serialVersionUID = 1L;
private IDatabase db;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = (String) req.getSession().getAttribute("username");
		String password = (String) req.getSession().getAttribute("password");
		if (username == null || password == null)
		{
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		} else {
			req.getRequestDispatcher("/_view/projectSolicitation.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DatabaseProvider.setInstance(new YCPDatabase());
		db = DatabaseProvider.getInstance();	
		String errorMessage = null;
		

		if (req.getParameter("proposal") != null){
			resp.sendRedirect(req.getContextPath() + "/projectProposal");
		}
		else{
		
			User user = null;
			int project_id = -1;
			int user_id = 0;
			String title = req.getParameter("title");
			String description = req.getParameter("description");
			String start = req.getParameter("start");
			String Duration = req.getParameter("duration");
			ProjectType type = ProjectType.SOLICITATION;
			String SolicitationType = req.getParameter("solicitationType");//
			String[] Majors = req.getParameterValues("majors");
			String[] Classes = req.getParameterValues("classes");
			ArrayList<MajorType> majors = new ArrayList<MajorType>();
			ArrayList<ClassType> classes = new ArrayList<ClassType>();
			String NumStudents = req.getParameter("numStudents");//
			String Cost = req.getParameter("cost");//
			boolean isFunded = false;
			String deadline = null;
			
			
			String username = (String) req.getSession().getAttribute("username");
			String password = (String) req.getSession().getAttribute("password");
			try {
				user = db.findUserByUsernameAndPassword(username, password);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			user_id = user.getUserID();
			
			for(int i = 0; i<Majors.length;i++){
				majors.add(getMajorTypeFromParameter(Majors[i]));
			}
			for(int i = 0; i<Classes.length;i++){
				classes.add(getClassTypeFromParameter(Classes[i]));
			}
				
			double cost = Double.parseDouble(Cost);
			int numStudents = Integer.parseInt(NumStudents);
			int duration = Integer.parseInt(Duration);
			
			SolicitationType solicitationType = getSolicitationTypeFromParameter(SolicitationType);
			System.out.println(title + " " + description + " " + start + " " + duration + " " + type);
			if (user_id == 0 || title == null || description == null || start == null || duration == 0 || type == null) {
				errorMessage = "Please specify required fields";
			} else {
				try {
					
					
					project_id = db.insertProject(user_id, title, description, start, duration, type, solicitationType, majors, classes, numStudents, cost, isFunded, deadline);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Add result objects as request attributes
			req.setAttribute("errorMessage", errorMessage);
			
			//See if the user clicked either of the other account types, redirect accordingly
			
			if(req.getParameter("submit") != null && project_id > 0){
				resp.sendRedirect(req.getContextPath() + "/projectProposal");
			}
			else {
				req.getRequestDispatcher("/_view/projectSolicitation.jsp").forward(req, resp);
			}
		}

	}
	
	//Translate parameter to MajorType
	private SolicitationType getSolicitationTypeFromParameter(String s){
		SolicitationType type = null;
		if (s == null || s.equals("")){
			return null;
		}
		else if (s.equals("ME Capstone")){
			type = SolicitationType.ME_CAPSTONE;		}
		else if (s.equals("ECE Capstone")){
			type = SolicitationType.ECE_CAPSTONE;		}
		else if(s.equals("CivE Capstone")){
			type = SolicitationType.CivE_CAPSTONE;		}
		else if(s.equals("ME/ECE Capstone")){
			type = SolicitationType.ME_ECE_CAPSTONE;		}
		else if(s.equals("Software Engineering")){
			type = SolicitationType.SW_ENGINEERING;		}
		else if(s.equals("CS Senior Design 1")){
			type = SolicitationType.CS_SENIOR_DESIGN_I;		}
		else if(s.equals("CS Senior Design 2")){
			type = SolicitationType.CS_SENIOR_DESIGN_II;		}
		else if(s.equals("Independent Study")){
			type = SolicitationType.INDEPENDENT_STUDY;		}
		else if(s.equals("CS Internship")){
			type = SolicitationType.CS_INTERNSHIP;		}
		else if(s.equals("Engineering Co-op")){
			type = SolicitationType.ENGINEERING_COOP;		}
		else if(s.equals("Class Project")){
			type = SolicitationType.CLASS_PROJECT;		}
		return type;

	}
	
	//Translate parameter to MajorType
	private MajorType getMajorTypeFromParameter(String s){
		MajorType majortype = null;
		if (s == null || s.equals("")){
			return null;
		}
		else if (s.equals("ME")){
			majortype = MajorType.ME;
			
		}
		else if (s.equals("CE")){
			majortype = MajorType.CE;
		}
		else if(s.equals("CS")){
			majortype = MajorType.CS;
		}
		else if(s.equals("EE")){
			majortype = MajorType.EE;
		}
		return majortype;
	}
	
	//Translate parameter to ClassType
	private ClassType getClassTypeFromParameter(String s){
		ClassType classtype = null;
		if(s == null || s.equals("")){
			return null;
		}
		else if (s == "FRESHMAN"){
			classtype = ClassType.FRESHMAN;
		}
		else if (s == "SOPHOMORE"){
			classtype = ClassType.SOPHOMORE;
		}
		else if (s == "JUNIOR"){
			classtype = ClassType.JUNIOR;
		}
		else if (s == "SENIOR"){
			classtype = ClassType.SENIOR;
		}
		return classtype;
	}
	
	private Boolean getBooleanFromParameter(String s){
		if (s == null || s.equals("")){
			return null;
		}
		else {
			return Boolean.parseBoolean(s);
		}
	}
	
	private ArrayList <MajorType> getMajorTypesFromParameters(HttpServletRequest req, HttpServletResponse resp){
		MajorType CE = getMajorTypeFromParameter("CE");
		MajorType CS = getMajorTypeFromParameter("CS");
		MajorType EE = getMajorTypeFromParameter("ME");
		MajorType ME = getMajorTypeFromParameter("EE");
		ArrayList <MajorType> majortypes = new ArrayList <MajorType>();
		if (CE != null){
			majortypes.add(CE);
		}
		if (CS != null){
			majortypes.add(CS);
		}
		if (EE != null){
			majortypes.add(EE);
		}
		if (ME != null){
			majortypes.add(ME);
		}
		
		return majortypes;
		
	}
	
	public ArrayList <ClassType> getClassTypesFromParameters(HttpServletRequest req, HttpServletResponse resp){
		ClassType FR = getClassTypeFromParameter("FRESHMAN");
		ClassType SO = getClassTypeFromParameter("SOPHOMORE");
		ClassType JR = getClassTypeFromParameter("JUNIOR");
		ClassType SR = getClassTypeFromParameter("SENIOR");
		ArrayList <ClassType> classtypes = new ArrayList <ClassType>();
		if (FR != null){
			classtypes.add(FR);
		}
		if (SO != null){
			classtypes.add(SO);
		}
		if (JR != null){
			classtypes.add(JR);
		}
		if (SR != null){
			classtypes.add(SR);
		}
		return classtypes;
	}
	
	private SolicitationType getSolicitTypeFromParameter(String s){
		SolicitationType solicitType = null;
		if(s == null || s.equals("")){
			return null;
		}
		else if (s == "ME_CAPSTONE"){
			solicitType = SolicitationType.ME_CAPSTONE;
		}
		else if (s == "ECE_CAPSTONE"){
			solicitType = SolicitationType.ECE_CAPSTONE;
		}
		else if (s == "CivE_CAPSTONE"){
			solicitType = SolicitationType.CivE_CAPSTONE;
		}
		else if (s == "ME_ECE_CAPSTONE"){
			solicitType = SolicitationType.ME_ECE_CAPSTONE;
		}
		else if (s == "SW_ENGINEERING"){
			solicitType = SolicitationType.SW_ENGINEERING;
		}
		else if (s == "CS_SENIOR_DESIGN_I"){
			solicitType = SolicitationType.CS_SENIOR_DESIGN_I;
		}
		else if (s == "CS_SENIOR_DESIGN_II"){
			solicitType = SolicitationType.CS_SENIOR_DESIGN_II;
		}
		else if (s == "INDEPENDENT_STUDY"){
			solicitType = SolicitationType.INDEPENDENT_STUDY;
		}
		else if (s == "CS_INTERNSHIP"){
			solicitType =SolicitationType.CS_INTERNSHIP;
		}
		else if (s == "ENGINEERING_COOP"){
			solicitType = SolicitationType.ENGINEERING_COOP;
		}
		else if (s == "CLASS_PROJECT"){
			solicitType = SolicitationType.CLASS_PROJECT;
		}
		return solicitType;
	}
}
