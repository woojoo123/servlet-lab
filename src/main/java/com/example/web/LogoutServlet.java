package com.example.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 로그아웃 처리 서블릿
 * POST /logout 요청을 처리하여 세션을 무효화하고 로그인 페이지로 리다이렉트합니다.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * POST /logout -> 세션 무효화 후 로그인 페이지로 redirect
     * 
     * 동작:
     * 1. 기존 세션이 있으면 가져옴 (없으면 null)
     * 2. 세션이 존재하면 무효화 (로그인 정보 삭제)
     * 3. 로그인 페이지로 리다이렉트
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // getSession(false): 세션이 없으면 새로 생성하지 않고 null 반환
        // 세션이 있으면 기존 세션 반환
        HttpSession session = req.getSession(false);
        
        // 세션이 존재하면 무효화 (로그인 정보 삭제)
        if (session != null) {
            session.invalidate(); // 세션 무효화 = 로그아웃
        }

        // 로그인 페이지로 리다이렉트
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
