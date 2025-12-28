package com.example.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/posts/new")
public class NewPostPageServlet extends HttpServlet {

    // GET /posts/new -> JSP forward
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/new.jsp");
        try {
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
