package com.example.cookbook_k12_it3_nhom2.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.controllers.CategoryController;
import com.example.cookbook_k12_it3_nhom2.controllers.RecipeController;
import com.example.cookbook_k12_it3_nhom2.controllers.UserController;
import com.example.cookbook_k12_it3_nhom2.models.Recipe;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CategoryDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.UserDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewRecipe;
    private RecipeAdapter mAdapterRecipe;
    private List<RecipeDto> mRecipeList;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        mRecyclerViewRecipe = findViewById(R.id.recipeList);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Hiển thị 2 recipe trên 1 hàng
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerViewRecipe.setLayoutManager(gridLayoutManager);

        showRecipes();      // Hiển thị danh sách recipe khi chạy chương trình

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Xử lý cho home
                showRecipes();
                return true;
            } else if (id == R.id.nav_favourites) {
                // Xử lý cho Favourites
                return true;
            } else if (id == R.id.nav_menu) {
                // Xử lý cho menu
                return true;
            } else if (id == R.id.nav_profile) {
                // Xử lý cho profile
                return true;
            }
            return false;
        });

//        searchRecipeByTitle("Bún Thịt Nướng");
//        getRecipeDetail("C3gIrwbppYZBYqMHNFbQ");
//        getAllCategories();
//        getCategoryDetail("GW2H6A2DdrFm1CgAyLfO");
//        getUserById("9vQ00cgYviaraZuulQiT");
//        getProfile("9vQ00cgYviaraZuulQiT");
//        getRecipes();
//        getRecipeById("VOOCQqhn21Dx4zL7O9OX");

//        Button btn = (Button) findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    String username = sharedPreferences.getString("username", "empty");
//                    String email = sharedPreferences.getString("email", "empty");
//                    String name_display = sharedPreferences.getString("name_display", "empty");
//                    Toast.makeText(MainActivity.this, username + email + name_display, Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    Log.i("error sharedPreferences", e.getMessage());
//                }
//            }
//        });
    }

    public void register(String username, String password) {
        UserController controller = new UserController();
        controller.register(username, password, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Log.i("register", "Register success");
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("register error", e.toString());
            }
        });
    }

    public void getRecipeDetail(String recipeId) {
        RecipeController controller = new RecipeController();
        controller.getDetail(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto recipe) {
                Log.i("getRecipeDetail: detail_" + recipeId, recipe.toString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("getRecipeDetail: error", e.toString());
            }
        });
    }

    public void searchRecipeByTitle(String title) {
        UserController controller = new UserController();
        controller.searchRecipeByTitle(title, new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> recipeDtos) {
                for (RecipeDto recipeDto : recipeDtos) {
                    Log.i("searchRecipeByTitle: search recipe", recipeDto.toString());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("error", e.toString());
            }
        });
    }

    public void getAllCategories() {
        CategoryController controller = new CategoryController();
        controller.getAllCategories(new FirestoreCallback<List<CategoryDto>>() {
            @Override
            public void onSuccess(List<CategoryDto> categories) {
                for (CategoryDto categoryDto : categories) {
                    Log.i("getAllCategories: category_" + categoryDto.getCategoryId(), categoryDto.toString());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("getAllCategories: error", e.toString());
            }
        });
    }

    public void getCategoryDetail(String categoryId) {
        CategoryController controller = new CategoryController();
        controller.getCategoryDetail(categoryId, new FirestoreCallback<CategoryDto>() {
            @Override
            public void onSuccess(CategoryDto result) {
                Log.i("getCategoryDetail: category_" + categoryId, result.toString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("getCategoryDetail: error", e.toString());
            }
        });
    }

    public void getUserById(String userId) {
        UserController userController = new UserController();
        userController.getUserById(userId, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto result) {
                Log.i("getUserById: user by id", result.toString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("getUserById: error", e.toString());
            }
        });
    }

    public void getProfile(String userId) {
        UserController userController = new UserController();
        userController.getProfile(userId, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto userDto) {
                Log.i("getProfile: user_" + userId, userDto.toString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("getProfile: error", e.getMessage());
            }
        });
    }

    // Hiển thị danh sách recipe trong trang chủ
    public void showRecipes() {
        RecipeController recipeController = new RecipeController();
        recipeController.getRecipes(new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> result) {
                mRecipeList = new ArrayList<>();
                mRecipeList = result;
                // Set adapter hiển thị item
                mAdapterRecipe = new RecipeAdapter(MainActivity.this, mRecipeList);
                mRecyclerViewRecipe.setAdapter(mAdapterRecipe);
                // Hiển thị text recipeCount
                TextView recipeCount = findViewById(R.id.recipeCount);
                recipeCount.setText("Hiển thị " + result.size() + " / " + result.size() + " công thức");
//                for (RecipeDto recipe : result) {
//                    Log.i("getRecipes: recipe_" + recipe.getRecipeId(), recipe.toString());
//                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipes: Error", e.toString());
                Toast.makeText(MainActivity.this, "Lỗi khi hiển thị danh sách công thức nấu ăn", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getRecipeById(String recipeId) {
        RecipeController recipeController = new RecipeController();
        recipeController.getRecipeById(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto result) {
                Log.i("getRecipeById: recipe_" + result.getRecipeId(), result.toString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipeById: error", e.toString());
            }
        });
    }
}