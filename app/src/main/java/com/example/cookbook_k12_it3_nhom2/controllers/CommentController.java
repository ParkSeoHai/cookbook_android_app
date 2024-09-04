package com.example.cookbook_k12_it3_nhom2.controllers;

import com.example.cookbook_k12_it3_nhom2.repositories.CommentRepository;

public class CommentController {
    private final CommentRepository repository;

    public CommentController() {
        repository = new CommentRepository();
    }

    public void initData() {
        repository.initData();
    }

}
