package com.example.cookbook_k12_it3_nhom2.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.controllers.RecipeController;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerViewRecipe;
    private RecipeAdapter mAdapterRecipe;
    private List<RecipeDto> mRecipeList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Show danh sách recipe
        mRecyclerViewRecipe = view.findViewById(R.id.recipeList);
        // Hiển thị 2 recipe trên 1 hàng
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        mRecyclerViewRecipe.setLayoutManager(gridLayoutManager);
        showRecipes(view);

        return view;
    }

    // Hiển thị danh sách recipe trong trang chủ
    public void showRecipes(View view) {
        RecipeController recipeController = new RecipeController();
        recipeController.getRecipes(new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> result) {
                mRecipeList = new ArrayList<>();
                mRecipeList = result;
                // Set adapter hiển thị item
                mAdapterRecipe = new RecipeAdapter(requireContext(), mRecipeList);
                mRecyclerViewRecipe.setAdapter(mAdapterRecipe);
                // Hiển thị text recipeCount
                TextView recipeCount = view.findViewById(R.id.recipeCount);
                recipeCount.setText("Hiển thị " + result.size() + " / " + result.size() + " công thức");
//                for (RecipeDto recipe : result) {
//                    Log.i("getRecipes: recipe_" + recipe.getRecipeId(), recipe.toString());
//                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipes: Error", e.toString());
                Toast.makeText(requireContext(), "Lỗi khi hiển thị danh sách công thức nấu ăn", Toast.LENGTH_LONG).show();
            }
        });
    }
}