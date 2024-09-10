package com.example.cookbook_k12_it3_nhom2.repositories.dtos;

public class IngredientDto {
    private String ingredientId;
    private String name;
    private String unit;    // Đơn vị đo
    private String category;
    private int quantity;

    public IngredientDto() {}

    public IngredientDto(String ingredientId, String name, String unit, String category, int quantity) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.category = category;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "IngredientDto{" +
                "ingredientId='" + ingredientId + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
