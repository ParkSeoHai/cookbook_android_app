package com.example.cookbook_k12_it3_nhom2.views;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.FavoriteDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.RecipeViewHolder> {

    private Context mContext;
    private List<FavoriteDto> mFavorites;

    public FavoriteAdapter(Context context, List<FavoriteDto> favoriteDtos) {
        mContext = context;
        mFavorites = favoriteDtos;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        FavoriteDto favoriteDto = mFavorites.get(position);
        holder.recipeTitle.setText(favoriteDto.getRecipeDto().getTitle());
        holder.recipeId = favoriteDto.getRecipeDto().getRecipeId();

        // Sử dụng glide load ảnh từ url
        Glide.with(mContext)
                .load(favoriteDto.getRecipeDto().getImageUrl())
                .placeholder(R.drawable.image_placeholder)  // Hình ảnh tạm trong khi chờ tải
                .error(R.drawable.image_placeholder)        // Hình ảnh hiển thị khi có lỗi
                .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        String recipeId = null;
        ImageView recipeImage;
        TextView recipeTitle;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);

            recipeImage.setOnClickListener(v -> {
                // Xử lý sự kiện click image recipe
                Intent intent = new Intent(itemView.getContext(), RecipeDetailActivity.class);
                if (recipeId != null) {
                    intent.putExtra("recipeId", recipeId);
                    // Mở activity recipe detail
                    itemView.getContext().startActivity(intent);
                } else {
                    Toast.makeText(itemView.getContext(), "Recipe id is null", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}