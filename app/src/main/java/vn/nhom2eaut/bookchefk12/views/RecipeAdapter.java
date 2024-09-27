package vn.nhom2eaut.bookchefk12.views;

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
import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;

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
        holder.recipeId = recipe.getRecipeId();
        float rating = recipe.getAverage_rating(); // Lấy số sao từ cơ sở dữ liệu
        String starCountText = String.format("%.1f ", rating) + " ";
        holder.starCount.setText(starCountText);
//        RequestOptions options = new RequestOptions()
//                .transform(new RoundedCorners(20)); // border-radius

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
        String recipeId = null;
        ImageView recipeImage;
        TextView recipeTitle;
        public TextView starCount; // Khai báo TextView cho số sao
        public ImageView starImage;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            starCount = itemView.findViewById(R.id.starCount); // Ánh xạ TextView
            starImage = itemView.findViewById(R.id.starImage);

            recipeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý sự kiện click image recipe
                    Intent intent = new Intent(itemView.getContext(), RecipeDetailActivity.class);
                    if (recipeId != null) {
                        intent.putExtra("recipeId", recipeId);
                        // Mở activity recipe detail
                        itemView.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Recipe id is null", Toast.LENGTH_LONG).show();
                    }
                    Log.i("click image", "Clicked " + recipeTitle);
                }
            });
        }
    }
}