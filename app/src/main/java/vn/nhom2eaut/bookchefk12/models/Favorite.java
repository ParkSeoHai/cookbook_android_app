package vn.nhom2eaut.bookchefk12.models;

public class Favorite {
    private String favoriteId;
    private String userId;
    private String recipeId;
    private String addedAt;

    public Favorite() {}

    public Favorite(String favoriteId, String userId, String recipeId, String addedAt) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.recipeId = recipeId;
        this.addedAt = addedAt;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }
}
