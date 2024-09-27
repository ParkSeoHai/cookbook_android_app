package vn.nhom2eaut.bookchefk12.models;

import java.util.List;
import java.util.Map;

public class Recipe {
    private String recipeId;
    private String title;
    private String description;
    private String authorId;
    private String categoryId;
    private List<Recipe_Ingredient> ingredients;
    private List<Step> steps;
    private Map<String, String> time; // Thời gian chuẩn bị, thời gian nấu
    private int servings;
    private String imageUrl;
    private String videoUrl;

    public Recipe() {}

    public Recipe(String title, String description,
                  String authorId, String categoryId, List<Recipe_Ingredient> ingredients,
                  List<Step> steps, Map<String, String> time, int servings, String imageUrl,
                  String videoUrl) {
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.ingredients = ingredients;
        this.steps = steps;
        this.time = time;
        this.servings = servings;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public Recipe(String title) {
        this.title = title;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<Recipe_Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Recipe_Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Map<String, String> getTime() {
        return time;
    }

    public void setTime(Map<String, String> time) {
        this.time = time;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId='" + recipeId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", authorId='" + authorId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", time=" + time +
                ", servings=" + servings +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
