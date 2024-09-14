package vn.nhom2eaut.bookchefk12.repositories.dtos;

import vn.nhom2eaut.bookchefk12.models.Recipe_Ingredient;
import vn.nhom2eaut.bookchefk12.models.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeDto {
    private String recipeId;
    private String title;
    private String description;
    private UserDto author;
    private CategoryDto category;
    private List<Recipe_Ingredient> ingredients;
    private List<Step> steps;
    private Map<String, String> time; // Thời gian chuẩn bị, thời gian nấu
    private int servings;
    private String imageUrl;
    private List<CommentDto> commentDtos;
    private float average_rating;   // Đánh giá trung bình
    private int number_of_ratings;  // Tổng số đánh giá
    private Map<String, Integer> ratingByStars;

    public RecipeDto() {
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        commentDtos = new ArrayList<>();
    }

    public RecipeDto(String recipeId, String title, String description,
                     UserDto author, CategoryDto category, List<Recipe_Ingredient> ingredients,
                     List<Step> steps, Map<String, String> time, int servings, String imageUrl,
                     List<CommentDto> commentDtos, float average_rating, int number_of_ratings,
                     Map<String, Integer> ratingByStars) {
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.time = time;
        this.servings = servings;
        this.imageUrl = imageUrl;
        this.commentDtos = commentDtos;
        this.average_rating = average_rating;
        this.number_of_ratings = number_of_ratings;
        this.ratingByStars = ratingByStars;
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

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
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

    public List<CommentDto> getCommentDtos() {
        return commentDtos;
    }

    public void setCommentDtos(List<CommentDto> commentDtos) {
        this.commentDtos = commentDtos;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public int getNumber_of_ratings() {
        return number_of_ratings;
    }

    public void setNumber_of_ratings(int number_of_ratings) {
        this.number_of_ratings = number_of_ratings;
    }

    public Map<String, Integer> getRatingByStars() {
        return ratingByStars;
    }

    public void setRatingByStars(Map<String, Integer> ratingByStars) {
        this.ratingByStars = ratingByStars;
    }

    @Override
    public String toString() {
        return "RecipeDto{" +
                "recipeId='" + recipeId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", category=" + category +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", time=" + time +
                ", servings=" + servings +
                ", imageUrl='" + imageUrl + '\'' +
                ", commentDtos=" + commentDtos +
                ", average_rating=" + average_rating +
                ", number_of_ratings=" + number_of_ratings +
                ", ratingByStars=" + ratingByStars +
                '}';
    }
}
