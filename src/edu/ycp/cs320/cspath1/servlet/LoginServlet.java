package edu.ycp.cs320.cspath1.servlet;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.cspath1.persist.DatabaseProvider;
import edu.ycp.cs320.cspath1.persist.YCPDatabase;
import edu.ycp.cs320.cspath1.enums.UserType;
import edu.ycp.cs320.cspath1.persist.IDatabase;
import edu.ycp.cs320.cspath1.user.User;



public class LoginServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
private IDatabase db;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DatabaseProvider.setInstance(new YCPDatabase());
		db = DatabaseProvider.getInstance();	
		String errorMessage = null;
		String username;
		String password;
		boolean validLogin = false;
		User user = null;

		username = req.getParameter("username");
		password = req.getParameter("password");
			
		if (username == null || password == null || username.equals("") || password.equals("")) {
			errorMessage = "Please specify username and password";
		} else {
			try {
				user = db.findUserByUsernameAndPassword(username, password);
				System.out.println(user.getUsername() + ", " + user.getPassword());
				System.out.println(username + ", " + password);
				
				if(user.getUsername() != null && user.getPassword() != null && user.getUsername().equals(username) && user.getPassword().equals(password)){
					validLogin = true;
				}
				else {
					errorMessage = "Username or Password may be incorrect";
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Add result objects as request attributes
		req.setAttribute("errorMessage", errorMessage);
		System.out.println(validLogin);
		if(req.getParameter("SignUp") != null){
			resp.sendRedirect(req.getContextPath() + "/accountCreationStudent");
		}
		else if(validLogin){
			req.getSession().setAttribute("username", username);
			req.getSession().setAttribute("password", password);
			System.out.println(user.getUsername() + ", " + user.getPassword());
			UserType userType = user.getUsertype();
			System.out.println(userType);

			if(userType == UserType.STUDENT){
				resp.sendRedirect(req.getContextPath() + "/studentHome");
			}
			else if(userType == UserType.BUSINESS){
				resp.sendRedirect(req.getContextPath() + "/businessHome");
			}
			else if(userType == UserType.FACULTY){
				resp.sendRedirect(req.getContextPath() + "/facultyHome");
			}			
			
		} else {
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		}
	}
	
	
	
}