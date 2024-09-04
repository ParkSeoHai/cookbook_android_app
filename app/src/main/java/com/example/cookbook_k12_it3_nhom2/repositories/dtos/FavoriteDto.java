package com.example.cookbook_k12_it3_nhom2.repositories.dtos;

public class FavoriteDto {
    private String favoriteId;
    private RecipeDto recipeDto;
    private String addedAt;

    public FavoriteDto() {}

    public FavoriteDto(String favoriteId, RecipeDto recipeDto, String addedAt) {
        this.favoriteId = favoriteId;
        this.recipeDto = recipeDto;
        this.addedAt = addedAt;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public RecipeDto getRecipeDto() {
        return recipeDto;
    }

    public void setRecipeDto(RecipeDto recipeDto) {
        this.recipeDto = recipeDto;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "FavoriteDto{" +
                "favoriteId='" + favoriteId + '\'' +
                ", recipeDto=" + recipeDto +
                ", addedAd=" + addedAt +
                '}';
    }
}
