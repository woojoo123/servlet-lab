package com.example.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 인증(로그인) 필터
 * 
 * 역할: /posts/* 경로에 접근하는 요청을 가로채서 로그인 여부를 확인합니다.
 * 
 * 동작:
 * - 로그인되어 있으면: 요청을 그대로 통과시킵니다.
 * - 로그인되어 있지 않으면: 로그인 페이지로 리다이렉트합니다.
 * 
 * 보호 대상:
 * - /posts/page (게시글 목록 페이지)
 * - /posts/new (새 글 작성 페이지)
 * - /posts/* (posts 하위의 모든 경로)
 * 
 * 주의: /posts 자체는 포함되지 않습니다 (다른 서블릿이 처리)
 */
@WebFilter("/posts/*") // /posts/page, /posts/new 보호 (주의: /posts는 포함 안 됨)
public class AuthFilter implements Filter {

    /**
     * 필터 메인 로직
     * 
     * 모든 /posts/* 요청이 이 메서드를 거쳐갑니다.
     * 로그인 여부를 확인하고, 로그인되어 있지 않으면 로그인 페이지로 보냅니다.
     * 
     * @param request 요청 객체
     * @param response 응답 객체
     * @param chain 필터 체인 (다음 필터 또는 서블릿으로 전달)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // ServletRequest/Response를 HttpServletRequest/Response로 캐스팅
        // (HTTP 관련 기능 사용을 위해)
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 세션 가져오기 (없으면 새로 생성하지 않음)
        // getSession(false): 세션이 없으면 null 반환, 있으면 기존 세션 반환
        HttpSession session = req.getSession(false);
        
        // 세션에서 로그인 사용자 정보 가져오기
        // 세션이 없거나 LOGIN_USER 속성이 없으면 null
        Object user = (session == null) ? null : session.getAttribute(LoginServlet.SESSION_KEY);

        // 로그인되어 있지 않은 경우
        if (user == null) {
            // 원래 가려던 경로를 next 파라미터로 만들어서 로그인 후 돌아오게 함
            // 예: /hello-servlet/posts/page -> /posts/page
            String next = req.getRequestURI().substring(req.getContextPath().length());
            
            // 쿼리 스트링이 있으면 함께 저장 (예: /posts/page?page=2)
            String qs = req.getQueryString();
            if (qs != null && !qs.isBlank()) {
                next = next + "?" + qs;
            }

            // URL 인코딩 (특수문자 안전하게 처리)
            String enc = URLEncoder.encode(next, StandardCharsets.UTF_8);
            
            // 로그인 페이지로 리다이렉트 (원래 가려던 경로 정보 포함)
            // 예: /login?next=%2Fposts%2Fpage
            resp.sendRedirect(req.getContextPath() + "/login?next=" + enc);
            return; // 여기서 요청 처리 종료 (서블릿으로 전달하지 않음)
        }

        // 로그인되어 있으면 요청을 그대로 통과시킴
        // 다음 필터 또는 서블릿으로 요청 전달
        chain.doFilter(request, response);
    }
}
