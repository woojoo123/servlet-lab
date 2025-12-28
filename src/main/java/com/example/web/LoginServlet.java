package com.example.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 로그인 처리 서블릿
 * POST /login 요청을 처리하여 사용자 인증 후 세션을 생성하고 적절한 페이지로 리다이렉트합니다.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /** 세션에 저장할 로그인 사용자 정보의 키 */
    public static final String SESSION_KEY = "LOGIN_USER";

    /**
     * POST /login -> 로그인 처리 후 redirect
     * 
     * 동작:
     * 1. 사용자명, 비밀번호, 원래 가려던 페이지(next) 파라미터 읽기
     * 2. 로그인 정보 검증 (실습용: admin/1234)
     * 3. 실패 시: 로그인 페이지로 리다이렉트 (에러 메시지 + next 정보 유지)
     * 4. 성공 시: 세션에 사용자 정보 저장 후 원래 가려던 페이지로 리다이렉트
     */

    // GET /login -> JSP forward
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/login.jsp");
        try {
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 폼에서 전송된 파라미터 읽기
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String next = req.getParameter("next"); // 로그인 후 이동할 페이지

        // 실습용 하드코딩 계정 (원하면 바꿔도 됨)
        // 실제 프로젝트에서는 데이터베이스에서 확인해야 함
        boolean ok = "admin".equals(username) && "1234".equals(password);

        // 로그인 실패 처리
        if (!ok) {
            // next 파라미터가 있으면 URL 인코딩하여 쿼리 파라미터로 추가
            // 로그인 실패 후에도 원래 가려던 페이지 정보를 유지하기 위함
            String qp = (next != null && !next.isBlank()) 
                ? ("&next=" + urlEncode(next)) 
                : "";
            
            // 로그인 페이지로 리다이렉트 (에러 메시지 표시 + next 정보 유지)
            resp.sendRedirect(req.getContextPath() + "/login?error=1" + qp);
            return;
        }

        // 로그인 성공 처리
        // getSession(true): 세션이 없으면 새로 생성, 있으면 기존 세션 반환
        HttpSession session = req.getSession(true);
        
        // 세션에 사용자명 저장 (다른 페이지에서 로그인 여부 확인용)
        session.setAttribute(SESSION_KEY, username);

        // Open Redirect 공격 방지: next는 /로 시작하는 내부 경로만 허용
        // 외부 URL(예: http://악성사이트.com)로 리다이렉트하는 것을 방지
        // next가 없거나 외부 URL이면 기본 페이지(/posts/page)로 이동
        String target = (next != null && next.startsWith("/")) 
            ? next                    // 내부 경로면 그대로 사용
            : "/posts/page";          // 아니면 기본 페이지로
        
        // 원래 가려던 페이지 또는 기본 페이지로 리다이렉트
        resp.sendRedirect(req.getContextPath() + target);
    }

    /**
     * URL 인코딩 헬퍼 메서드
     * URL에 포함될 문자열을 안전하게 인코딩합니다.
     * 예: "/posts/page" -> "%2Fposts%2Fpage"
     * 
     * @param s 인코딩할 문자열
     * @return URL 인코딩된 문자열
     */
    private String urlEncode(String s) {
        try {
            return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return ""; // 인코딩 실패 시 빈 문자열 반환
        }
    }
}
