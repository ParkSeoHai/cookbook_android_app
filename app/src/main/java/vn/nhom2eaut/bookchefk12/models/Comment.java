package vn.nhom2eaut.bookchefk12.models;

public class Comment {
    private String commentId;
    private String recipeId;
    private String userId;
    private String content;
    private int rating;
    private String createdAt;

    public Comment() {}

    public Comment(String recipeId, String userId, String content,
                   int rating, String createdAt) {
        this.recipeId = recipeId;
        this.userId = userId;
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

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
