package vn.nhom2eaut.bookchefk12.controllers;

import vn.nhom2eaut.bookchefk12.repositories.CommentRepository;

public class CommentController {
    private final CommentRepository repository;

    public CommentController() {
        repository = new CommentRepository();
    }

    public void initData() {
        repository.initData();
    }

}
