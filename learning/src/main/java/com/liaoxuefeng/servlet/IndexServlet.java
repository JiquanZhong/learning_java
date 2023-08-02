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
 * @create 03/08/2023 - 01:24
 */
@WebServlet("/")
public class IndexServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 设置响应类型:
		resp.setContentType("text/html");
		String name = (String) req.getSession().getAttribute("name");
		name = (name == null) ? "" : name;
		// 获取输出流:
		PrintWriter pw = resp.getWriter();
		// 写入响应:
		pw.write("<h1>你好 !</h1>" + name);
		// 最后不要忘记flush强制输出:
		pw.flush();
	}
}
