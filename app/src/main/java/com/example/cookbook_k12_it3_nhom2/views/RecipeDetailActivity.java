package com.example.cookbook_k12_it3_nhom2.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookbook_k12_it3_nhom2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecipeDetailActivity extends AppCompatActivity {
    private String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeId");

        Log.i("recipe id" , recipeId);

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
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}