package com.revature.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.User;
import com.revature.services.UserService;

public class LoginServlet extends DefaultServlet {
	private Logger log = Logger.getRootLogger();
	private UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.getRequestDispatcher("/static/login.html").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// using JSON
		String json = request.getReader().lines().reduce((acc, cur) -> acc + cur).get();
		log.trace("json " + json);
		ObjectMapper om = new ObjectMapper();
		User credentials = (User) om.readValue(json, User.class);
		log.trace(credentials);
		User u = userService.login(credentials.getUsername(), credentials.getPassword());

		// for default form submit method
		// log.trace("post request made to login servlet");
		// log.trace("username = " + request.getParameter("username"));
		// log.trace("password = " + request.getParameter("password"));
		// User u = userService.login(request.getParameter("username"),
		// request.getParameter("password"));

		if (u != null) {
			HttpSession sess = request.getSession();
			sess.setAttribute("user", u);
		} else {
			response.setStatus(401);
		}
	}
}
