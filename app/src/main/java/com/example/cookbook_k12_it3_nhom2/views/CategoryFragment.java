package com.example.cookbook_k12_it3_nhom2.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.controllers.CategoryController;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CategoryDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    GridView gridView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        getAllCategories(view);

        return view;
    }

    public void getAllCategories(View view) {
        CategoryController controller = new CategoryController();
        controller.getAllCategories(new FirestoreCallback<List<CategoryDto>>() {
            @Override
            public void onSuccess(List<CategoryDto> categories) {
                gridView = view.findViewById(R.id.gridViewCategory);
                // Sử dụng custom adapter để hiển thị danh mục món ăn
                CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categories);
                gridView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("getAllCategories: error", e.toString());
            }
        });
    }
}