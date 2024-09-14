package vn.nhom2eaut.bookchefk12.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.repositories.dtos.IngredientDto;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<IngredientDto> ingredients;
    private int servings = 1;

    public IngredientAdapter(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientDto ingredient = ingredients.get(position);
        holder.bind(ingredient, servings);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void updateIngredients(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public void updateServings(int servings) {
        this.servings = servings;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientName, ingredientQuantity;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
        }

        public void bind(IngredientDto ingredient, int servings) {
            ingredientName.setText(ingredient.getName());
            int quantity = ingredient.getQuantity() * servings;
            ingredientQuantity.setText(String.valueOf(quantity) + " " + ingredient.getUnit());
        }
    }
}
