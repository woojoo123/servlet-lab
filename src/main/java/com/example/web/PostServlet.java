package com.example.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.repo.PostStore;


@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain; charset=UTF-8");

        List<String> items = PostStore.findAll();

        resp.getWriter().println("POSTS COUNT: " + items.size());
        for (int i = 0; i < items.size(); i++) {
            resp.getWriter().println((i + 1) + ") " + items.get(i));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 파라미터 읽기 전에 인코딩 설정
        // req.setCharacterEncoding("UTF-8");  // Filter에서 처리하므로 주석처리

        String title = req.getParameter("title");
        String content = req.getParameter("content");

        // 둘 중 하나라도 없거나 비면 400
        if(title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().println("BAD REQUEST: title and content are required");
            return;
        }

        PostStore.add(title.trim(), content.trim());

        // 정상 처리 : 201
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().println("CREATED");
        // resp.getWriter().println("title=" + title);
        // resp.getWriter().println("content=" + content);
        }
    }