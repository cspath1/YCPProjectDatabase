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
import edu.ycp.cs320.cspath1.enums.UserType;
import edu.ycp.cs320.cspath1.model.ProjectModel;
import edu.ycp.cs320.cspath1.user.User;
import edu.ycp.cs320.cspath1.controller.InsertProjectController;
import edu.ycp.cs320.cspath1.controller.SearchProjects;
import edu.ycp.cs320.cspath1.project.Project;

public class ProposalSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private SearchProjects search = null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = (String) req.getSession().getAttribute("username");
		String password = (String) req.getSession().getAttribute("password");
		if (username == null || password == null)
		{
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		} else {
			req.getRequestDispatcher("/_view/proposalSearch.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		

		Project project			 	=null;
		ArrayList<Project> projects	=null;
		String searchField 			=null;
		String errorMessage			=null;
		String title				=null;
		String description			=null;
		String start				=null;
		String deadline				=null;
		int duration;
		int numStudents;
		double cost;
		MajorType majorType			=null;
		ClassType classtype			=null;
		
		
		searchField = req.getParameter("attribute");
		
		search = new SearchProjects();
		projects = new ArrayList<Project>();
		
	/*	System.out.println(searchField);
		System.out.println(req.getParameter("keyword"));*/
		
		if(searchField.equals("title")){
			try {
				title = req.getParameter("keyword");
				projects= search.getProjectsByTitle(title);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(searchField.equals("description")){
			try {
				description = req.getParameter("keyword");
				projects = search.getProjectsByDescription(description);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(searchField.equals("start")){
			try {
				start = req.getParameter("keyword");
				projects = search.getProjectsByStart(start);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(searchField.equals("deadline")){
			try {
				deadline = req.getParameter("keyword");
				projects = search.getProjectsByDeadline(deadline);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(searchField.equals("major")){
			try {
				majorType = getMajorTypeFromParameter(req.getParameter("keyword"));
				projects.addAll(search.getProjectsByMajor(majorType));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(searchField.equals("class")){
			try {
				classtype = getClassTypeFromParameter(req.getParameter("keyword"));
				projects.addAll(search.getProjectsByClassType(classtype));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(searchField.equals("duration")){
			try {
				duration = Integer.parseInt(req.getParameter("keyword"));
				projects = search.getProjectsByDuration(duration);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(searchField.equals("numStudents")){
			try {
				numStudents = Integer.parseInt(req.getParameter("keyword"));
				projects = search.getProjectsByNumberOfStudents(numStudents);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(searchField.equals("cost")){
			try {
				cost = Integer.parseInt(req.getParameter("keyword"));
				projects = search.getProjectsByCost(cost);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if (projects == null) {
			errorMessage = "User/Users not found in library";
		}
		else {
			project  = projects.get(0);
			System.out.println(project.getTitle());
		}
		req.setAttribute("projects", projects);
		req.setAttribute("project", project);
		req.setAttribute("errorMessage", errorMessage);
		
		req.getRequestDispatcher("/_view/proposalSearch.jsp").forward(req, resp);
	}
	
	private MajorType getMajorTypeFromParameter(String s){
		MajorType majortype = null;
		if (s == null || s.equals("")){
			return null;
		}
		else if (s.equals("ME")||s.equals("Mechanical")){
			majortype = MajorType.ME;
			
		}
		else if (s.equals("CE")||s.equals("Computer")){
			majortype = MajorType.CE;
		}
		else if(s.equals("CS")||s.equals("Computer Science")){
			majortype = MajorType.CS;
		}
		else if(s.equals("EE")||s.equals("Electrical")){
			majortype = MajorType.EE;
		}
		return majortype;
	}
	
	private ClassType getClassTypeFromParameter(String s){
		ClassType classtype = null;
		if (s == null || s.equals("")){
			return null;
		}
		else if (s.equals("sophomore")||s.equals("Sophomore")){
			classtype = ClassType.SOPHOMORE;
			
		}
		else if (s.equals("freshman")||s.equals("Freshman")){
			classtype = ClassType.FRESHMAN;
		}
		else if(s.equals("Senior")||s.equals("senior")){
			classtype = ClassType.SENIOR;
		}
		else if(s.equals("junior")||s.equals("Junior")){
			classtype = ClassType.JUNIOR;
		}
		return classtype;
	}
}
