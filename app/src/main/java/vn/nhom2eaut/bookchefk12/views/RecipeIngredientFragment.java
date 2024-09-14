package vn.nhom2eaut.bookchefk12.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.controllers.RecipeController;
import vn.nhom2eaut.bookchefk12.repositories.dtos.IngredientDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeIngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeIngredientFragment extends Fragment {
    private String recipeId;

    private TextView tvServings;
    private RecyclerView recyclerView;
    private IngredientAdapter ingredientAdapter;
    private int servings = 1; // Số phần ăn mặc định

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeIngredientFragment(String recipeId) {
        // Required empty public constructor
        this.recipeId = recipeId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeIngredientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public RecipeIngredientFragment newInstance(String param1, String param2) {
        RecipeIngredientFragment fragment = new RecipeIngredientFragment(recipeId);
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
        View view = inflater.inflate(R.layout.fragment_recipe_ingredient, container, false);

        tvServings = view.findViewById(R.id.servingsCount);
        recyclerView = view.findViewById(R.id.recycler_view_ingredients);
        ImageView btnIncrease = view.findViewById(R.id.btn_increase);
        ImageView btnDecrease = view.findViewById(R.id.btn_decrease);

        // Thiết lập RecyclerView
        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(ingredientAdapter);

        // Load dữ liệu từ recipe
        loadIngredientsByRecipeId(recipeId);

        // Xử lý nút tăng số phần ăn
        btnIncrease.setOnClickListener(v -> {
            servings++;
            tvServings.setText(String.valueOf(servings));
            updateIngredientsForServings();
        });

        // Xử lý nút giảm số phần ăn
        btnDecrease.setOnClickListener(v -> {
            if (servings > 1) {
                servings--;
                tvServings.setText(String.valueOf(servings));
                updateIngredientsForServings();
            }
        });
        return view;
    }

    private void loadIngredientsByRecipeId(String recipeId) {
        RecipeController recipeController = new RecipeController();
        recipeController.getIngredientsByRecipeId(recipeId, new FirestoreCallback<List<IngredientDto>>() {
            @Override
            public void onSuccess(List<IngredientDto> ingredientDtos) {
                ingredientAdapter.updateIngredients(ingredientDtos);
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("error getIngredientsByRecipeId", e.toString());
            }
        });
    }

    // Cập nhật số lượng nguyên liệu dựa trên số phần ăn
    private void updateIngredientsForServings() {
        ingredientAdapter.updateServings(servings);
    }
}