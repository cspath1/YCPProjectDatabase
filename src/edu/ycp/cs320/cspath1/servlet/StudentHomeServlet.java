  package edu.ycp.cs320.cspath1.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StudentHomeServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = (String) req.getSession().getAttribute("username");
		String password = (String) req.getSession().getAttribute("password");
		if (username == null || password == null)
		{
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		} else {
			req.getRequestDispatcher("/_view/studentHome.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//moving around the webpage
		if (req.getParameter("logout") != null) {
			resp.sendRedirect(req.getContextPath() + "/login");
		} else if (req.getParameter("userSearch") != null) {
			resp.sendRedirect(req.getContextPath() + "/userSearch");
		} else if (req.getParameter("proposalSearch") != null) {
			resp.sendRedirect(req.getContextPath() + "/proposalSearch");
		} else if (req.getParameter("solicitationSearch") != null) {
			resp.sendRedirect(req.getContextPath() + "/solicitationSearch");
		} else if (req.getParameter("solicitation") != null) {
			resp.sendRedirect(req.getContextPath() + "/projectSolicitation");
		} else if (req.getParameter("proposal") != null) {
			resp.sendRedirect(req.getContextPath() + "/projectProposal");
		} else if (req.getParameter("myProjects") != null) {
			resp.sendRedirect(req.getContextPath() + "/viewProjects");
		} else {
			req.getRequestDispatcher("/_view/studentHome.jsp").forward(req, resp);
		}
	}
}