package vn.nhom2eaut.bookchefk12.views;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.nhom2eaut.bookchefk12.R;

import vn.nhom2eaut.bookchefk12.controllers.UserController;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecipeDetailActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private UserController userController;
    private String recipeId, userId;
    private Boolean recipeIsFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        // Get recipe id từ itent
        recipeId = intent.getStringExtra("recipeId");

        // sharedPreferences lấy lưu trữ thông tin người dùng đăng nhập
        sharedPreferences = getSharedPreferences("UserRefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);

        userController = new UserController();

        loadFragment(new RecipeAboutFragment(recipeId));    // Default fragment

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_about) {
                // Xử lý cho about
                fragment = new RecipeAboutFragment(recipeId);
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_ingredients) {
                // Xử lý cho ingredient
                fragment = new RecipeIngredientFragment(recipeId);
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_instructions) {
                // Xử lý cho instruction
                fragment = new RecipeInstructionFragment(recipeId);
                loadFragment(fragment);
                return true;
            }
            return false;
        });

        // Back to prev activity
        ImageView backIcon = (ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> {
            finish();
        });

        // Kiểm tra recipe hiện tại có trong danh sách yêu thích hay không
        checkRecipeInFavorite(recipeId, userId);

        ImageView moreIcon = (ImageView) findViewById(R.id.moreIcon);
        moreIcon.setOnClickListener(v -> {
            showPopupMenu(v);
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showPopupMenu(View view) {
        // Tạo PopupMenu
        PopupMenu popup = new PopupMenu(RecipeDetailActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_recipe, popup.getMenu());

        // Nếu recipe có trong danh sách yêu thích của người dùng đang đăng nhập thì thay đổi title
        if (recipeIsFavorite) {
            popup.getMenu().findItem(R.id.action_favorite).setTitle("Xóa khỏi yêu thích");
        } else {
            popup.getMenu().findItem(R.id.action_favorite).setTitle("Thêm vào yêu thích");
        }

        // Bắt sự kiện khi một mục trong menu được chọn
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_favorite) {
                    // Xử lý logic thêm/xóa yêu thích
                    if (recipeIsFavorite) {
                        removeRecipeInFavorite(view, recipeId, userId);
                    } else
                        addRecipeToFavorite(view, recipeId, userId);

                    return true;
                } else if (item.getItemId() == R.id.action_rate) {
                    // Xử lý logic đánh giá tại đây
                    showRatingDialog();
                    return true;
                }
                return false;
            }
        });

        // Hiển thị PopupMenu
        popup.show();
    }

    private void showRatingDialog() {
        // Tạo và cấu hình Dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rating);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        EditText ratingContent = dialog.findViewById(R.id.ratingContent);
        Button submitRating = dialog.findViewById(R.id.submitRating);

        // Thiết lập tiêu đề cho Dialog
        dialogTitle.setText("Đánh giá công thức");

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String content = ratingContent.getText().toString();

                // Xử lý dữ liệu đánh giá, thêm dữ liệu vào database
                if(rating > 0) {
                    if(content.trim().isBlank()) {
                        content = "Người dùng không để lại bình luận";
                    }
                    addComment(v, recipeId, userId, content, (int) rating);
                    dialog.dismiss();   // Đóng Dialog sau khi gửi đánh giá
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Vui lòng chọn số sao đánh giá", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show(); // Hiển thị Dialog
    }

    public void checkRecipeInFavorite(String recipeId, String userId) {
        userController.checkRecipeInFavorite(recipeId, userId, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // Gán dữ liệu true nếu có, ngược lại false
                recipeIsFavorite = result;
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("Error checkRecipeInFavorite", e.toString());
            }
        });
    }

    public void addRecipeToFavorite(View view, String recipeId, String userId) {
        userController.addRecipeToFavorite(recipeId, userId, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Toast.makeText(RecipeDetailActivity.this, "Thêm công thức vào yêu thích thành công", Toast.LENGTH_SHORT).show();
                    // Xét recipe tồn tại trong favorite
                    recipeIsFavorite = true;
                    showPopupMenu(view);
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Thêm công thức vào yêu thích thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("Error addRecipeToFavorite", e.toString());
                Toast.makeText(RecipeDetailActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeRecipeInFavorite(View view, String recipeId, String userId) {
        userController.removeRecipeInFavorite(recipeId, userId, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Toast.makeText(RecipeDetailActivity.this, "Xóa công thức khỏi yêu thích thành công", Toast.LENGTH_SHORT).show();
                    // Xét recipe không tồn tại trong favorite
                    recipeIsFavorite = false;
                    showPopupMenu(view);
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Xóa công thức khỏi yêu thích thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("Error removeRecipeInFavorite", e.toString());
                Toast.makeText(RecipeDetailActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addComment(View view, String recipeId, String userId, String content, int rating) {
        userController.addComment(recipeId, userId, content, rating, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Toast.makeText(RecipeDetailActivity.this, "Gửi đánh giá cho công thức này thành công", Toast.LENGTH_LONG).show();
                    // Refresh fragment about recipe
                    loadFragment(new RecipeAboutFragment(recipeId));
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RecipeDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}