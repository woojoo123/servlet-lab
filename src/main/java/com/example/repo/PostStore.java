package com.example.repo;

import java.util.ArrayList;
import java.util.List;

public class PostStore {
    
    // 단순 실습용 (서버 메모리에 저장)
    private static final List<String> posts = new ArrayList<>();
    
    public static void add(String title, String content) {
        posts.add("title=" + title + ", content=" + content);
    }

    public static List<String> findAll() {
        return new ArrayList<>(posts);
    }
}
