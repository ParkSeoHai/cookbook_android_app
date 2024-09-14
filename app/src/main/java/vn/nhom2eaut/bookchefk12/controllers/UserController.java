package vn.nhom2eaut.bookchefk12.controllers;

import vn.nhom2eaut.bookchefk12.models.Comment;
import vn.nhom2eaut.bookchefk12.models.Favorite;
import vn.nhom2eaut.bookchefk12.repositories.FavoriteRepository;
import vn.nhom2eaut.bookchefk12.repositories.RecipeRepository;
import vn.nhom2eaut.bookchefk12.repositories.UserRepository;
import vn.nhom2eaut.bookchefk12.repositories.dtos.FavoriteDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.UserDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import java.text.SimpleDateFormat;
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

    public void addComment(String recipeId, String userId, String content, int rating, FirestoreCallback<Boolean> callback) {
        Comment comment = new Comment();
        comment.setRecipeId(recipeId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setRating(rating);

        // Lấy ngày và giờ hiện tại
        Date now = new Date();
        // Định dạng ngày giờ
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedNow = formatter.format(now);

        comment.setCreatedAt(formattedNow);

        repository.addComment(comment, new FirestoreCallback<Boolean>() {
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
