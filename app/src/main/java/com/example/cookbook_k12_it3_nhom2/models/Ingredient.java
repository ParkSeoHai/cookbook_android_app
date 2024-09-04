package com.example.cookbook_k12_it3_nhom2.models;

public class Ingredient {
    private String ingredientId;
    private String name;
    private String unit;    // Đơn vị đo
    private String category;
    private String imageUrl;

    public Ingredient() {}

    public Ingredient(String name, String unit, String category, String imageUrl) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
