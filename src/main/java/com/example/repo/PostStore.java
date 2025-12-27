package com.example.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import com.example.domain.Post;
public class PostStore {
    
    // 단순 실습용 (서버 메모리에 저장)
    // private static final List<String> posts = new ArrayList<>();
    
    // public static void add(String title, String content) {
    //     posts.add("title=" + title + ", content=" + content);
    // }

    // public static List<String> findAll() {
    //     return new ArrayList<>(posts);
    // }

    // AtomicLong : 동시성 증가해도 ID가 꼬이지 않음
    private static final AtomicLong seq = new AtomicLong(0);
    // CopyOnWriteArrayList : 동시에 add/조회가 발생해도 안전
    private static final CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();

    public static Post add(String title, String content) {
        long id = seq.incrementAndGet();
        Post p = new Post(id, title, content);
        posts.add(p);
        return p;
    }

    public static List<Post> findAll() {
        return new ArrayList<>(posts);
    }
}
