package com.example.cookbook_k12_it3_nhom2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook_k12_it3_nhom2.models.Category;
import com.example.cookbook_k12_it3_nhom2.models.Ingredient;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CategoryDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private final FirebaseFirestore db;

    public CategoryRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void initData() {
        List<Category> categories = new ArrayList<>();

        // Món chính
        categories.add(new Category(
                "Món chính",
                "Các món chính thường được phục vụ trong bữa ăn chính.",
                "https://example.com/main_dish.jpg"
        ));

        // Món ăn sáng
        categories.add(new Category(
                "Món ăn sáng",
                "Các món ăn nhẹ và dinh dưỡng cho bữa sáng.",
                "https://example.com/breakfast.jpg"
        ));

        // Đồ giải khát
        categories.add(new Category(
                "Đồ giải khát",
                "Các loại đồ uống mát lạnh và giải khát.",
                "https://example.com/beverage.jpg"
        ));

        // Món ăn vặt
        categories.add(new Category(
                "Món ăn vặt",
                "Những món ăn nhẹ, thường được ăn giữa các bữa chính.",
                "https://example.com/snack.jpg"
        ));

        // Các loại bánh
        categories.add(new Category(
                "Các loại bánh",
                "Các loại bánh ngọt, bánh mặn, và các loại bánh khác.",
                "https://example.com/cake.jpg"
        ));

        // Insert data
        for (Category category : categories) {
            db.collection("categories")
                    .add(category)
                    .addOnSuccessListener(documentReference -> {
                        Log.i("insert category", category.getName());
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi
                        Log.e("error", e.getMessage().toString());
                    });
        }
    }

    public CategoryDto convertToDto(@NonNull Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setImageUrl(category.getImageUrl());
        return categoryDto;
    }

    public void all(FirestoreCallback<List<CategoryDto>> callback) {
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<CategoryDto> categoryDtos = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                category.setCategoryId(document.getId());
                                // Convert to dto
                                CategoryDto categoryDto = convertToDto(category);
                                categoryDtos.add(categoryDto);
                            }
                            callback.onSuccess(categoryDtos);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public void detail(String categoryId, FirestoreCallback<CategoryDto> callback) {
        db.collection("categories")
                .document(categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Category category = document.toObject(Category.class);
                                category.setCategoryId(document.getId());
                                // Convert to Dto
                                CategoryDto categoryDto = convertToDto(category);

                                // Tạo danh sách Task bất đồng bộ
                                List<Task<Void>> tasks = new ArrayList<>();

                                // Get recipes by category id
                                RecipeRepository recipeRepository = new RecipeRepository();
                                Task<Void> reciTask = recipeRepository.allByCategoryId(categoryDto.getCategoryId(), new FirestoreCallback<List<RecipeDto>>() {
                                    @Override
                                    public void onSuccess(List<RecipeDto> recipeDtos) {
                                        categoryDto.setRecipeDtos(recipeDtos);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                });
                                tasks.add(reciTask);

                                // Đợi tất cả các task hoàn thành
                                Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> allTasks) {
                                        if (allTasks.isSuccessful()) {
                                            // Tất cả các dữ liệu đã được get thành công
                                            callback.onSuccess(categoryDto);
                                        }
                                    }
                                });
                            } else {
                                Exception e = new Exception("Category not found");
                                BugLogRepository.logErrorToDatabase(e, "Method findById id = " + categoryId);
                                callback.onFailure(e);
                            }
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public Task<Void> findById(String categoryId, FirestoreCallback<CategoryDto> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("categories").document(categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Category category = document.toObject(Category.class);
                                category.setCategoryId(document.getId());
                                // Convert to Dto
                                CategoryDto categoryDto = convertToDto(category);
                                callback.onSuccess(categoryDto);
                                taskCompletionSource.setResult(null);
                            } else {
                                Exception ex = new Exception("Category not found");
                                BugLogRepository.logErrorToDatabase(ex, "Method findById with id = " + categoryId);
                                callback.onFailure(ex);
                                taskCompletionSource.setException(ex);
                            }
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }
}
