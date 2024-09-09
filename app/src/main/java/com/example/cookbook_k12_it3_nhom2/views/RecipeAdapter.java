package com.example.cookbook_k12_it3_nhom2.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.models.Recipe;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context mContext;
    private List<RecipeDto> mRecipes;

    public RecipeAdapter(Context context, List<RecipeDto> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeDto recipe = mRecipes.get(position);
        holder.recipeTitle.setText(recipe.getTitle());
        // Sử dụng glide load ảnh từ url
        Glide.with(mContext)
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.image_placeholder)  // Hình ảnh tạm trong khi chờ tải
                .error(R.drawable.image_placeholder)        // Hình ảnh hiển thị khi có lỗi
                .into(holder.recipeImage);
//        holder.recipeImage.setImageResource(R.drawable.logo);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
        }
    }
}