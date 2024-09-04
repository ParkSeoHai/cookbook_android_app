package com.example.cookbook_k12_it3_nhom2.repositories.dtos;

public class CommentDto {
    private String commentId;
    private RecipeDto recipeDto;
    private UserDto userDto;
    private String content;
    private int rating;
    private String createdAt;

    public CommentDto() {}

    public CommentDto(String commentId, RecipeDto recipeDto, UserDto userDto,
                      String content, int rating, String createdAt) {
        this.commentId = commentId;
        this.recipeDto = recipeDto;
        this.userDto = userDto;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public RecipeDto getRecipeDto() {
        return recipeDto;
    }

    public void setRecipeDto(RecipeDto recipeDto) {
        this.recipeDto = recipeDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "commentId='" + commentId + '\'' +
                ", recipeDto=" + recipeDto +
                ", userDto=" + userDto +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
