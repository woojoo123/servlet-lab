package com.example.common;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
    private static final Gson gson = new Gson();

    public static void ok(HttpServletResponse resp, Object data) throws IOException {
        write(resp, HttpServletResponse.SC_OK, successBody(data));
    }

    public static void created(HttpServletResponse resp, Object data) throws IOException {
        write(resp, HttpServletResponse.SC_CREATED, successBody(data));
    }

    public static void error(HttpServletRequest req, HttpServletResponse resp,
                             int status, String code, String message) throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);

        Map<String, Object> err = new HashMap<>();
        err.put("code", code);
        err.put("message", message);
        err.put("path", req.getRequestURI());

        body.put("error", err);

        write(resp, status, body);
    }

    private static Map<String, Object> successBody(Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("data", data);
        return body;
    }

    private static void write(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().print(gson.toJson(body));
    }
}
