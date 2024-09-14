package vn.nhom2eaut.bookchefk12.repositories;

import androidx.annotation.NonNull;

import vn.nhom2eaut.bookchefk12.models.Favorite;
import vn.nhom2eaut.bookchefk12.repositories.dtos.FavoriteDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;
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

public class FavoriteRepository {
    private final FirebaseFirestore db;

    public FavoriteRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public FavoriteDto convertToDto(@NonNull Favorite favorite) {
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setFavoriteId(favorite.getFavoriteId());
        favoriteDto.setAddedAt(favorite.getAddedAt());
        return favoriteDto;
    }

    public Task<Void> allByUserId(String userId, FirestoreCallback<List<FavoriteDto>> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("favorites").whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Tạo danh sách Task bất đồng bộ
                            List<Task<Void>> tasks = new ArrayList<>();
                            List<FavoriteDto> favoriteDtos = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Favorite favorite = document.toObject(Favorite.class);
                                // Convert to FavoriteDto
                                FavoriteDto favoriteDto = convertToDto(favorite);

                                // Get recipe by recipeId
                                RecipeRepository recipeRepository = new RecipeRepository();
                                Task<Void> recipeTask = recipeRepository.findById(favorite.getRecipeId(), new FirestoreCallback<RecipeDto>() {
                                    @Override
                                    public void onSuccess(RecipeDto result) {
                                        favoriteDto.setRecipeDto(result);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                }).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        favoriteDtos.add(favoriteDto);
                                    } else {
                                        callback.onFailure(task1.getException());
                                    }
                                });

                                tasks.add(recipeTask);  // Thêm Task vào danh sách
                            }
                            // Chờ tất cả các Task hoàn thành
                            Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        callback.onSuccess(favoriteDtos);
                                        taskCompletionSource.setResult(null);
                                    } else {
                                        callback.onFailure(task.getException());
                                    }
                                }
                            });
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
        return taskCompletionSource.getTask();
    }
}
