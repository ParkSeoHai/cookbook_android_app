package com.example.cookbook_k12_it3_nhom2.repositories.dtos;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private String userId;
    private String name_display;
    private String username;
    private String email;
    private String profile_picture;
    private String bio; // Mô tả ngắn
    private List<RecipeDto> recipeDtos;
    private List<CommentDto> commentDtos;

    public UserDto() {
        recipeDtos = new ArrayList<>();
        commentDtos = new ArrayList<>();
    }

    public UserDto(String userId, String name_display, String username,
                   String email, String profile_picture, String bio,
                   List<RecipeDto> recipeDtos, List<CommentDto> commentDtos) {
        this.userId = userId;
        this.name_display = name_display;
        this.username = username;
        this.email = email;
        this.profile_picture = profile_picture;
        this.bio = bio;
        this.recipeDtos = recipeDtos;
        this.commentDtos = commentDtos;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName_display() {
        return name_display;
    }

    public void setName_display(String name_display) {
        this.name_display = name_display;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<RecipeDto> getRecipeDtos() {
        return recipeDtos;
    }

    public void setRecipeDtos(List<RecipeDto> recipeDtos) {
        this.recipeDtos = recipeDtos;
    }

    public List<CommentDto> getCommentDtos() {
        return commentDtos;
    }

    public void setCommentDtos(List<CommentDto> commentDtos) {
        this.commentDtos = commentDtos;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId='" + userId + '\'' +
                ", name_display='" + name_display + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", bio='" + bio + '\'' +
                ", recipeDtos=" + recipeDtos.toString() +
                ", commentDtos=" + commentDtos.toString() +
                '}';
    }
}
