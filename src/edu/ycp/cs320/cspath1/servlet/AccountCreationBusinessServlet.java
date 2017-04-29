package edu.ycp.cs320.cspath1.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.enums.UserType;
import edu.ycp.cs320.cspath1.persist.DatabaseProvider;
import edu.ycp.cs320.cspath1.persist.IDatabase;
import edu.ycp.cs320.cspath1.persist.YCPDatabase;
import edu.ycp.cs320.cspath1.user.Business;



public class AccountCreationBusinessServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
private IDatabase db;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/accountCreationBusiness.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			DatabaseProvider.setInstance(new YCPDatabase());
			db = DatabaseProvider.getInstance();	
			String errorMessage = null;
			
			String email = req.getParameter("email");
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			String password1 = req.getParameter("password1");
			MajorType majortype = null;
			ClassType classtype = null;
			String firstname = null;
			String lastname = null;
			String name = req.getParameter("name");
			String number = req.getParameter("number");
			String address = req.getParameter("address");
			int user_id = 0;
				
			if (username == null || password == null || password1 == null || email == null || name == null || number == null || address == null) {
				errorMessage = "Please specify required fields";
			} else if (!password.equals(password1)) {
				errorMessage = "Passwords do not match";
			} else {
				try {
					user_id = db.insertUser(username, password, email, UserType.STUDENT, firstname, lastname, majortype, classtype, name, address, number);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Add result objects as request attributes
			req.setAttribute("errorMessage", errorMessage);
			
			//See if the user clicked either of the other account types, redirect accordingly
			if (req.getParameter("student") != null){
				resp.sendRedirect(req.getContextPath() + "/accountCreationStudent");
			}
			else if (req.getParameter("faculty") != null){
				resp.sendRedirect(req.getContextPath() + "/accountCreationFaculty");
			}
			else if(req.getParameter("submit") != null && user_id > 0){
				req.getSession().setAttribute("username", username);
				req.getSession().setAttribute("password", password);
				resp.sendRedirect(req.getContextPath() + "/businessHome");
			}
			else {
				req.getRequestDispatcher("/_view/accountCreationBusiness.jsp").forward(req, resp);
			}
		}
}
