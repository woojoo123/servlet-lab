package com.example.web;

import com.example.domain.Post;
import com.example.repo.PostStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/posts/page")
public class PostsPageServlet extends HttpServlet {

    // GET /posts/page -> JSP forward(서버 내부 이동)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Post> items = PostStore.findAll();

        req.setAttribute("items", items);
        req.setAttribute("count", items.size());

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/posts.jsp");
        try {
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // POST /posts/page -> 저장 후 redirect(PRG)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        if (title == null || title.trim().isEmpty() ||
            content == null || content.trim().isEmpty()) {

            // 예외 던지지 않고(=JSON 에러 방지), 폼으로 redirect
            resp.sendRedirect(req.getContextPath() + "/posts/new?error=1");
            return;
        }

        PostStore.add(title.trim(), content.trim());

        // PRG: POST 처리 후 redirect -> 새로고침 중복 제출 방지
        resp.sendRedirect(req.getContextPath() + "/posts/page");
    }
}
