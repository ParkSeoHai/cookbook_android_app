package com.example.cookbook_k12_it3_nhom2.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
    private SharedPreferences sharedPreferences;
    private String user_id, username, email, name_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // sharedPreferences lấy lưu trữ thông tin người dùng đăng nhập
        sharedPreferences = getSharedPreferences("UserRefs", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);
        username = sharedPreferences.getString("username", null);
        email = sharedPreferences.getString("email", null);
        name_display = sharedPreferences.getString("name_display", null);

        // Set default fragment
        loadFragment(new HomeFragment(null, null));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Xử lý cho home
                fragment = new HomeFragment(null, null);
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_favourites) {
                // Xử lý cho Favourites
                fragment = new FavoriteFragment(user_id);
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_menu) {
                // Xử lý cho menu
                fragment = new CategoryFragment(getSupportFragmentManager());
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_profile) {
                // Xử lý cho profile
                fragment = new ProfileFragment(user_id, sharedPreferences);
                loadFragment(fragment);
                return true;
            }
            return false;
        });

        // Click logo quay về trang chủ
        ImageView logoIcon = findViewById(R.id.logoIcon);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
            }
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

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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