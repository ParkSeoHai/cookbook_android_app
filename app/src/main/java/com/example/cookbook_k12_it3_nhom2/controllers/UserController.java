package com.example.cookbook_k12_it3_nhom2.controllers;

import com.example.cookbook_k12_it3_nhom2.models.Favorite;
import com.example.cookbook_k12_it3_nhom2.repositories.FavoriteRepository;
import com.example.cookbook_k12_it3_nhom2.repositories.RecipeRepository;
import com.example.cookbook_k12_it3_nhom2.repositories.UserRepository;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.FavoriteDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.UserDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class UserController {
    private final UserRepository repository;
    private final RecipeRepository recipeRepository;
    private final FavoriteRepository favoriteRepository;

    public UserController() {
        this.repository = new UserRepository();
        this.recipeRepository = new RecipeRepository();
        this.favoriteRepository = new FavoriteRepository();
    }

    public void login(String username, String password, FirestoreCallback<UserDto> callback) {
        repository.login(username, password, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto userDto) {
                callback.onSuccess(userDto);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void register(String username, String password, FirestoreCallback<Boolean> callback) {
        repository.register(username, password, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getProfile(String userId, FirestoreCallback<UserDto> callback) {
        repository.getProfile(userId, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getFavouritesRecipe(String userId, FirestoreCallback<List<FavoriteDto>> callback) {
        favoriteRepository.allByUserId(userId, new FirestoreCallback<List<FavoriteDto>>() {
            @Override
            public void onSuccess(List<FavoriteDto> result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getUserById(String userId, FirestoreCallback<UserDto> callback) {
        repository.findById(userId, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void searchRecipeByTitle(String title, FirestoreCallback<List<RecipeDto>> callback) {
        recipeRepository.searchByTitle(title, new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void checkRecipeInFavorite(String recipeId, String userId, FirestoreCallback<Boolean> callback) {
        repository.checkRecipeInFavorite(recipeId, userId, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void addRecipeToFavorite(String recipeId, String userId, FirestoreCallback<Boolean> callback) {
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setRecipeId(recipeId);

        // Lấy ngày và giờ hiện tại
        Date now = new Date();
        System.out.println("Current date and time: " + now);

        // Định dạng ngày giờ
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedNow = formatter.format(now);

        favorite.setAddedAt(formattedNow);

        repository.addRecipeToFavorite(favorite, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void removeRecipeInFavorite(String recipeId, String userId, FirestoreCallback<Boolean> callback) {
        repository.removeRecipeInFavorite(recipeId, userId, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                callback.onSuccess(result);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
}
