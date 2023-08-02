package com.liaoxuefeng.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 01:37
 */
@WebServlet(urlPatterns = "/hi")
public class RedirectServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 构造重定向的路径:
    String name = req.getParameter("name");
    String redirectToUrl = "/hello" + (name == null ? "" : "?name=" + name);
    // 发送重定向响应:
    resp.sendRedirect(redirectToUrl);
  }
}
