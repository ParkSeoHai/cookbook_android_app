package com.example.cookbook_k12_it3_nhom2.controllers;

import com.example.cookbook_k12_it3_nhom2.repositories.IngredientRepository;

public class IngredientController {
    private final IngredientRepository repository;

    public IngredientController() {
        this.repository = new IngredientRepository();
    }

    public void initData() {
        repository.initData();
    }
}
