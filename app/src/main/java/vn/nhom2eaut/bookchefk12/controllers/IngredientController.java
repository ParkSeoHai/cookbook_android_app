package vn.nhom2eaut.bookchefk12.controllers;

import vn.nhom2eaut.bookchefk12.repositories.IngredientRepository;

public class IngredientController {
    private final IngredientRepository repository;

    public IngredientController() {
        this.repository = new IngredientRepository();
    }

    public void initData() {
        repository.initData();
    }
}
