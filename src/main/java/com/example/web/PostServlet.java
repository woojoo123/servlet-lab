package com.example.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.domain.Post;
import com.example.repo.PostStore;
import com.google.gson.Gson;


@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Post> items = PostStore.findAll();

        Map<String, Object> body = new HashMap<>();
        body.put("count", items.size());
        body.put("items", items);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().println(gson.toJson(body));
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

            Map<String, String> err = new HashMap<>();
            err.put("message", "title and content are required");
            resp.getWriter().println(gson.toJson(err));
            return;
        }

        Post saved = PostStore.add(title.trim(), content.trim());

        // 정상 처리 : 201
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().println(gson.toJson(saved));
        // resp.getWriter().println("title=" + title);
        // resp.getWriter().println("content=" + content);
        }
    }