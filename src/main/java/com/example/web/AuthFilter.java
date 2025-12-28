package com.example.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebFilter("/posts/*") // /posts/page, /posts/new 보호 (주의: /posts는 포함 안 됨)
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Object user = (session == null) ? null : session.getAttribute(LoginServlet.SESSION_KEY);

        if (user == null) {
            // 원래 가려던 경로를 next로 붙여서 로그인 후 돌아오게 함
            String next = req.getRequestURI().substring(req.getContextPath().length());
            String qs = req.getQueryString();
            if (qs != null && !qs.isBlank()) next = next + "?" + qs;

            String enc = URLEncoder.encode(next, StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?next=" + enc);
            return;
        }

        chain.doFilter(request, response);
    }
}
