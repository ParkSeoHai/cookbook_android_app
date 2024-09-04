package com.example.cookbook_k12_it3_nhom2.controllers;

import com.example.cookbook_k12_it3_nhom2.repositories.CategoryRepository;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CategoryDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;

import java.util.List;

public class CategoryController {
    private final CategoryRepository repository;

    public CategoryController() {
        this.repository = new CategoryRepository();
    }

    public void initData() {
        this.repository.initData();
    }

    public void getAllCategories(FirestoreCallback<List<CategoryDto>> callback) {
        repository.all(new FirestoreCallback<List<CategoryDto>>() {
            @Override
            public void onSuccess(List<CategoryDto> result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getCategoryDetail(String categoryId, FirestoreCallback<CategoryDto> callback) {
        repository.detail(categoryId, new FirestoreCallback<CategoryDto>() {
            @Override
            public void onSuccess(CategoryDto result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
}
