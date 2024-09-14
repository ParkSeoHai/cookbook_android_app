package vn.nhom2eaut.bookchefk12.views;

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

import vn.nhom2eaut.bookchefk12.R;

import vn.nhom2eaut.bookchefk12.controllers.UserController;
import vn.nhom2eaut.bookchefk12.repositories.dtos.FavoriteDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {
    private String user_id;
    private RecyclerView mRecyclerViewRecipe;
    private FavoriteAdapter mAdapterFavorite;
    private List<FavoriteDto> mFavoriteList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteFragment(String user_id) {
        // Required empty public constructor
        this.user_id = user_id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment(this.user_id);
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        // Show danh sách recipe
        mRecyclerViewRecipe = view.findViewById(R.id.recipeListFavorite);
        // Hiển thị 2 recipe trên 1 hàng
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        mRecyclerViewRecipe.setLayoutManager(gridLayoutManager);
        // Hiển thị recipe favorites
        showRecipesFavorites(view, user_id);

        return view;
    }

    // Hiển thị danh sách recipe trong trang chủ
    public void showRecipesFavorites(View view, String userId) {
        UserController userController = new UserController();
        userController.getFavouritesRecipe(userId, new FirestoreCallback<List<FavoriteDto>>() {
            @Override
            public void onSuccess(List<FavoriteDto> result) {
                mFavoriteList = new ArrayList<>();
                mFavoriteList = result;
                // Set adapter hiển thị item
                mAdapterFavorite = new FavoriteAdapter(requireContext(), mFavoriteList);
                mRecyclerViewRecipe.setAdapter(mAdapterFavorite);
                // Hiển thị text recipeCount
                TextView recipeCount = view.findViewById(R.id.recipeCount);
                recipeCount.setText("Hiển thị " + result.size() + " / " + result.size() + " công thức yêu thích");
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("getRecipes: Error", e.toString());
                Toast.makeText(requireContext(), "Lỗi khi hiển thị danh sách công thức nấu ăn yêu thích", Toast.LENGTH_LONG).show();
            }
        });
    }
}