package vn.nhom2eaut.bookchefk12.views;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.controllers.CategoryController;
import vn.nhom2eaut.bookchefk12.controllers.RecipeController;
import vn.nhom2eaut.bookchefk12.controllers.UserController;
import vn.nhom2eaut.bookchefk12.repositories.dtos.CategoryDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private String categoryId, categoryName;

    private RecyclerView mRecyclerViewRecipe;
    private RecipeAdapter mAdapterRecipe;
    private List<RecipeDto> mRecipeList;

    private EditText searchBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment(String categoryId, String categoryName) {
        // Required empty public constructor
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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
    public HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment(categoryId, categoryName);
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

        // Xử lý tìm kiếm công thức
        searchBar = view.findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Gọi trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Gọi khi văn bản đang thay đổi
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Gọi sau khi văn bản đã thay đổi
                UserController userController = new UserController();
                userController.searchRecipeByTitle(s.toString(), new FirestoreCallback<List<RecipeDto>>() {
                    @Override
                    public void onSuccess(List<RecipeDto> result) {
                        showRecipes(view, result);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        if (categoryId != null) {
            showRecipesOfCategory(view);
        } else {
            getRecipes(view);
        }

        return view;
    }

    public void focusEditSearch() {
        if (searchBar != null) {
            searchBar.requestFocus();
            // Hiển thị bàn phím
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void showRecipes(View view, List<RecipeDto> recipeDtos) {
        mRecipeList = new ArrayList<>();
        mRecipeList = recipeDtos;
        // Set adapter hiển thị item
        mAdapterRecipe = new RecipeAdapter(requireContext(), mRecipeList);
        mRecyclerViewRecipe.setAdapter(mAdapterRecipe);
        // Hiển thị text recipeCount
        TextView recipeCount = view.findViewById(R.id.recipeCount);
        recipeCount.setText("Hiển thị " + recipeDtos.size() + " / " + recipeDtos.size() + " công thức");
    }

    // Hiển thị danh sách recipe trong trang chủ
    public void getRecipes(View view) {
        RecipeController recipeController = new RecipeController();
        recipeController.getRecipes(new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> result) {
                showRecipes(view, result);
            }
            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipes: Error", e.toString());
                Toast.makeText(requireContext(), "Lỗi khi hiển thị danh sách công thức", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Hiển thị danh sách recipes by category
    public void showRecipesOfCategory(View view) {
        CategoryController controller = new CategoryController();
        controller.getCategoryDetail(categoryId, new FirestoreCallback<CategoryDto>() {
            @Override
            public void onSuccess(CategoryDto result) {
                mRecipeList = new ArrayList<>();
                mRecipeList = result.getRecipeDtos();
                // Set adapter hiển thị item
                mAdapterRecipe = new RecipeAdapter(requireContext(), mRecipeList);
                mRecyclerViewRecipe.setAdapter(mAdapterRecipe);
                // Hiển thị text recipeCount
                TextView recipeCount = view.findViewById(R.id.recipeCount);
                recipeCount.setText("Danh mục: " + categoryName + "\n" + "Hiển thị " + result.getRecipeDtos().size() + " / " + result.getRecipeDtos().size() + " công thức");
            }
            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipes: Error", e.toString());
                Toast.makeText(requireContext(), "Lỗi khi hiển thị danh sách công thức", Toast.LENGTH_LONG).show();
            }
        });
    }
}