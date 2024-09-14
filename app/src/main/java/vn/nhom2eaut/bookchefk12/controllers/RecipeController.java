package vn.nhom2eaut.bookchefk12.controllers;

import vn.nhom2eaut.bookchefk12.repositories.RecipeRepository;
import vn.nhom2eaut.bookchefk12.repositories.dtos.IngredientDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import java.util.List;

public class RecipeController {
    private final RecipeRepository recipeRepository;

    public RecipeController() {
        this.recipeRepository = new RecipeRepository();
    }

    public void initData() {
        this.recipeRepository.initData();
    }

    public void getRecipes(FirestoreCallback<List<RecipeDto>> callback) {
        recipeRepository.all(new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> recipes) {
                callback.onSuccess(recipes);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getRecipeById(String recipeId, FirestoreCallback<RecipeDto> callback) {
        recipeRepository.findById(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getDetail(String recipeId, FirestoreCallback<RecipeDto> callback) {
        recipeRepository.detail(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getIngredientsByRecipeId(String recipeId, FirestoreCallback<List<IngredientDto>> callback) {
        recipeRepository.getIngredientsByRecipeId(recipeId, new FirestoreCallback<List<IngredientDto>>() {
            @Override
            public void onSuccess(List<IngredientDto> result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
}
