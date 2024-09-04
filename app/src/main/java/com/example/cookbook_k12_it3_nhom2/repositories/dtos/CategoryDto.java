package com.example.cookbook_k12_it3_nhom2.repositories.dtos;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    private String categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private List<RecipeDto> recipeDtos;

    public CategoryDto() {
        recipeDtos = new ArrayList<>();
    }

    public CategoryDto(String categoryId, String name, String description, String imageUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<RecipeDto> getRecipeDtos() {
        return recipeDtos;
    }

    public void setRecipeDtos(List<RecipeDto> recipeDtos) {
        this.recipeDtos = recipeDtos;
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", recipeDtos=" + recipeDtos.toString() +
                '}';
    }
}
