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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.nhom2eaut.bookchefk12.R;

import vn.nhom2eaut.bookchefk12.controllers.UserController;
import vn.nhom2eaut.bookchefk12.models.Recipe;
import vn.nhom2eaut.bookchefk12.models.Recipe_Ingredient;
import vn.nhom2eaut.bookchefk12.models.Step;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RecipeDetailActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private UserController userController;
    private String recipeId, userId;
    private Boolean recipeIsFavorite = false;

    private Map<String, String> ingredientMap = new HashMap<>();

    private void loadIngredientNames() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ingredients").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String ingredientId = document.getId();
                        String ingredientName = document.getString("name");
                        ingredientMap.put(ingredientId, ingredientName);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Lỗi tải nguyên liệu", Toast.LENGTH_SHORT).show();
                });
    }

    private String getIngredientNameById(String ingredientId) {
        return ingredientMap.getOrDefault(ingredientId, "Không rõ");
    }


    private void shareRecipe(String recipeId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recipes").document(recipeId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Recipe recipe = documentSnapshot.toObject(Recipe.class);

                        if (recipe != null) {
                            StringBuilder shareBody = new StringBuilder();
                            shareBody.append("Công thức: ").append(recipe.getTitle()).append("\n\n");
                            shareBody.append("Mô tả: ").append(recipe.getDescription()).append("\n\n");
                            shareBody.append("Thành phần: \n");

                            for (Recipe_Ingredient ingredient : recipe.getIngredients()) {
                                String ingredientName = getIngredientNameById(ingredient.getIngredientId());
                                shareBody.append("- ").append(ingredientName).append(": ").append(ingredient.getQuantity()).append("\n");
                            }

                            shareBody.append("\nCách làm: \n");
                            for (Step step : recipe.getSteps()) {
                                shareBody.append(step.getStep_number()).append(". ").append(step.getInstruction()).append("\n");
                            }

                            shareBody.append("\nThời gian: Chuẩn bị - ")
                                    .append(recipe.getTime().get("prep_time"))
                                    .append(", Nấu - ")
                                    .append(recipe.getTime().get("cook_time"))
                                    .append("\n");

                            shareBody.append("Khẩu phần: ").append(recipe.getServings()).append("\n");

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Công thức nấu ăn: " + recipe.getTitle());
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());

                            startActivity(Intent.createChooser(shareIntent, "Chia sẻ công thức nấu ăn qua"));
                        } else {
                            Toast.makeText(getApplicationContext(), "Không tìm thấy công thức", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Lỗi khi lấy công thức", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        loadIngredientNames();
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
                    if (userId == null) {
                        showLoginDialog(); // Yêu cầu đăng nhập nếu chưa đăng nhập
                    } else {
                        // Xử lý logic thêm/xóa yêu thích
                        if (recipeIsFavorite) {
                            removeRecipeInFavorite(view, recipeId, userId);
                        } else {
                            addRecipeToFavorite(view, recipeId, userId);
                        }
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_rate) {
                    // Kiểm tra đăng nhập trước khi đánh giá
                    if (userId == null) {
                        showLoginDialog(); // Yêu cầu đăng nhập nếu chưa đăng nhập
                    } else {
                        showRatingDialog(); // Cho phép đánh giá nếu đã đăng nhập
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_share) {
                    // Gọi hàm tạo link chia sẻ khi người dùng chọn chia sẻ
                    shareRecipe(recipeId); // Truyền recipeId vào đây
                    return true;
                }

                return false;
            }
        });

        // Hiển thị PopupMenu
        popup.show();
    }

    private void showLoginDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Yêu cầu đăng nhập")
                .setMessage("Bạn cần đăng nhập để thực hiện chức năng này. Bạn có muốn chuyển đến trang đăng nhập không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Chuyển người dùng đến trang đăng nhập
                    startActivity(new Intent(RecipeDetailActivity.this, loginActivity.class));
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
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