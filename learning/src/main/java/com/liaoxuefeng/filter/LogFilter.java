package com.liaoxuefeng.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 14:30
 */
@WebFilter("/*")
public class LogFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    System.out.println("LogFilter: process " + ((HttpServletRequest) request).getRequestURI());
    chain.doFilter(request, response);
  }
}
