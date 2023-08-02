package com.liaoxuefeng.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 01:22
 */
	@WebServlet(urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {
	private Map<String, String> users = Map.of("bob", "bob123", "alice", "alice123", "tom", "tomcat");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		pw.write("<h1>Sign In</h1>");
		pw.write("<form action=\"/signin\" method=\"post\">");
		pw.write("<p>Username: <input name=\"username\"></p>");
		pw.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
		pw.write("<p><button type=\"submit\">Sign In</button> <a href=\"/\">Cancel</a></p>");
		pw.write("</form>");
		pw.flush();
	}

	// POST请求时处理用户登录:
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("username");
		String password = req.getParameter("password");
		String expectedPassword = users.get(name.toLowerCase());
		if (expectedPassword != null && expectedPassword.equals(password)) {
			// 登录成功:
			req.getSession().setAttribute("name", name);
			resp.sendRedirect("/");
		} else {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
}
