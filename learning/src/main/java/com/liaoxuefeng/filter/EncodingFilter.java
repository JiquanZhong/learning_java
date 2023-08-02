package com.liaoxuefeng.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;

import java.io.IOException;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 14:27
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    System.out.println("EncodingFilter: doFilter");
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    chain.doFilter(request, response);
  }
}
