package edu.ycp.cs320.cspath1.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FacultyHomeServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String user = (String) req.getSession().getAttribute("user");
		if (user == null) {
			System.out.println("   User: <" + user + "> not logged in or session timed out");
			
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		// now we have the user's User object,
		// proceed to handle request...
		
		System.out.println("   User: <" + user + "> logged in");
		
		req.getRequestDispatcher("/_view/facultyHome.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//See if the user clicked either of the other account types, redirect
		if (req.getParameter("logout")!= null) {
			resp.sendRedirect(req.getContextPath() + "/login");
		} else if (req.getParameter("search") != null) {
			resp.sendRedirect(req.getContextPath() + "/search");
		} else if (req.getParameter("proposal") != null) {
			resp.sendRedirect(req.getContextPath() + "/projectProposal");
		} else if (req.getParameter("solicitation") != null) {
			resp.sendRedirect(req.getContextPath() + "/projectSolicitation");
		}else if (req.getParameter("myProjects") != null) {
			resp.sendRedirect(req.getContextPath() + "myProjects");
		} else if (req.getParameter("myApprovals") != null) {
			resp.sendRedirect(req.getContextPath() + "/myApprovals");
		} else {
			req.getRequestDispatcher("/_view/facultyHome.jsp").forward(req, resp);
		}
	}
	
	

}