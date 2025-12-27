package com.example.web;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

// 모든 요청에 적용
@WebFilter("/*")
public class EncodingFilter implements Filter{
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 다음단계 서블릿으로 넘김
        chain.doFilter(request, response);
    }
}
