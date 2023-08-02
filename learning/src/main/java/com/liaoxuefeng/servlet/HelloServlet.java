package com.liaoxuefeng.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 00:51
 */
// WebServlet注解表示这是一个Servlet，并映射到地址/:
@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String name = req.getParameter("name");
		if (name == null) name = "";
		// 设置响应类型:
		resp.setContentType("text/html");
		// 获取输出流:
		PrintWriter pw = resp.getWriter();
		// 写入响应:
		pw.write("<h1>Hello, world!</h1>" + name);
		// 最后不要忘记flush强制输出:
		pw.flush();
	}
}