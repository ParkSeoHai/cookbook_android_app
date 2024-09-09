package com.example.cookbook_k12_it3_nhom2.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.controllers.CategoryController;
import com.example.cookbook_k12_it3_nhom2.controllers.RecipeController;
import com.example.cookbook_k12_it3_nhom2.controllers.UserController;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CategoryDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.UserDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.i("start get data", "get profile");

        register("parkseohai", "2803");

//        searchRecipeByTitle("Bún Thịt Nướng");
//        getRecipeDetail("C3gIrwbppYZBYqMHNFbQ");
//        getAllCategories();
//        getCategoryDetail("GW2H6A2DdrFm1CgAyLfO");
//        getUserById("9vQ00cgYviaraZuulQiT");
//        getProfile("9vQ00cgYviaraZuulQiT");
//        getRecipes();
//        getRecipeById("VOOCQqhn21Dx4zL7O9OX");

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = sharedPreferences.getString("username", "empty");
                    String email = sharedPreferences.getString("email", "empty");
                    String name_display = sharedPreferences.getString("name_display", "empty");
                    Toast.makeText(MainActivity.this, username + email + name_display, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.i("error sharedPreferences", e.getMessage());
                }
            }
        });
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

    public void getRecipes() {
        RecipeController recipeController = new RecipeController();
        recipeController.getRecipes(new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> result) {
                for (RecipeDto recipe : result) {
                    Log.i("getRecipes: recipe_" + recipe.getRecipeId(), recipe.toString());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipes: Error", e.toString());
            }
        });
    }

    public void getRecipeById(String recipeId) {
        RecipeController recipeController = new RecipeController();
        recipeController.getRecipeById(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto result) {
                TextView view = (TextView) MainActivity.this.findViewById(R.id.text_view);
                view.setText(result.getTitle());
                Log.i("getRecipeById: recipe_" + result.getRecipeId(), result.toString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipeById: error", e.toString());
            }
        });
    }
}