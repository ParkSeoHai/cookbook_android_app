package vn.nhom2eaut.bookchefk12.models;

public class Recipe_Ingredient {
    private String ingredientId;
    private int quantity;

    public Recipe_Ingredient() {}

    public Recipe_Ingredient(String ingredientId, int quantity) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
