package com.example.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Object user = (session == null) ? null : session.getAttribute(LoginServlet.SESSION_KEY);

        // 1) 미로그인 -> 로그인 페이지로
        if (user == null) {
            String next = req.getRequestURI().substring(req.getContextPath().length());
            String qs = req.getQueryString();
            if (qs != null && !qs.isBlank()) next = next + "?" + qs;

            String enc = URLEncoder.encode(next, StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?next=" + enc);
            return;
        }

        // 2) 로그인은 했지만 ADMIN이 아님 -> 403
        Object role = session.getAttribute(LoginServlet.ROLE_KEY);
        boolean isAdmin = "ADMIN".equals(role);

        if (!isAdmin) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/forbidden.jsp");
            rd.forward(req, resp);
            return;
        }

        // 3) ADMIN이면 통과
        chain.doFilter(request, response);
    }
}
