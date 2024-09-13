package com.example.cookbook_k12_it3_nhom2.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CategoryDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.FavoriteDto;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private FragmentManager fragmentManager;
    private Context context;
    private final List<CategoryDto> categoryDtos;

    // Constructor
    public CategoryAdapter(Context context, List<CategoryDto> categoryDtos, FragmentManager fragmentManager) {
        this.context = context;
        this.categoryDtos = categoryDtos;
        this.fragmentManager = fragmentManager;
    }

    // Number of items in the GridView
    @Override
    public int getCount() {
        return categoryDtos.size();
    }

    // Not necessary in this case
    @Override
    public Object getItem(int position) {
        return null;
    }

    // Not necessary in this case
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Inflate the view for each grid item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_category_item, parent, false);
        }

        TextView categoryName = convertView.findViewById(R.id.categoryName);
        ImageView categoryImage = convertView.findViewById(R.id.categoryImage);

        CategoryDto categoryDto = categoryDtos.get(position);

        categoryName.setText(categoryDto.getName());

        // Sử dụng glide load ảnh từ url
        Glide.with(context)
                .load(categoryDto.getImageUrl())
                .placeholder(R.drawable.image_placeholder)  // Hình ảnh tạm trong khi chờ tải
                .error(R.drawable.image_placeholder)        // Hình ảnh hiển thị khi có lỗi
                .into(categoryImage);

        // Hiệu ứng màu cho image
        categoryImage.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_OVER));

        // Xử lý sự kiện click category item
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị danh sách công thức theo danh mục
                loadFragment(new HomeFragment(categoryDto.getCategoryId(), categoryDto.getName()));
            }
        });

        return convertView;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}