package com.example.cookbook_k12_it3_nhom2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook_k12_it3_nhom2.models.User;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CommentDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.FavoriteDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.UserDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserRepository {
    private final FirebaseFirestore db;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public UserDto convertToDto(@NonNull User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setName_display(user.getName_display());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setBio(user.getBio());
        userDto.setProfile_picture(user.getProfile_picture());
        return userDto;
    }

    public Task<Void> findById(String userId, FirestoreCallback<UserDto> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                User user = task.getResult().toObject(User.class);
                                user.setUserId(task.getResult().getId());
                                // Convert to UserDto
                                UserDto userDto = convertToDto(user);
                                callback.onSuccess(userDto);
                                taskCompletionSource.setResult(null);
                            } else {
                                Exception e = new Exception("User not found");
                                BugLogRepository.logErrorToDatabase(e,"Method findById - id = " + userId);
                                callback.onFailure(e);
                            }
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

    public void getProfile(String userId, FirestoreCallback<UserDto> callback) {
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                User user = task.getResult().toObject(User.class);
                                user.setUserId(task.getResult().getId());

                                // Convert to UserDto
                                UserDto userDto = convertToDto(user);

                                // Tạo danh sách Task bất đồng bộ
                                List<Task<Void>> tasks = new ArrayList<>();

                                // Get recipes by userId
                                RecipeRepository recipeRepository = new RecipeRepository();
                                Task<Void> reciTask = recipeRepository.allByUserId(userDto.getUserId(), new FirestoreCallback<List<RecipeDto>>() {
                                    @Override
                                    public void onSuccess(List<RecipeDto> recipeDtos) {
                                        userDto.setRecipeDtos(recipeDtos);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                });
                                tasks.add(reciTask);

                                // Get comments
                                CommentRepository commentRepository = new CommentRepository();
                                Task<Void> cmtTask = commentRepository.allByUserId(userDto.getUserId(), new FirestoreCallback<List<CommentDto>>() {
                                    @Override
                                    public void onSuccess(List<CommentDto> commentDtos) {
                                        userDto.setCommentDtos(commentDtos);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                });
                                tasks.add(cmtTask);

                                // Get favorites
                                FavoriteRepository favoriteRepository = new FavoriteRepository();
                                Task<Void> favTask = favoriteRepository.allByUserId(userDto.getUserId(), new FirestoreCallback<List<FavoriteDto>>() {
                                    @Override
                                    public void onSuccess(List<FavoriteDto> result) {
                                        userDto.setFavoriteDtos(result);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                });
                                tasks.add(favTask);

                                // Đợi tất cả các task hoàn thành
                                Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> allTasks) {
                                        if (allTasks.isSuccessful()) {
                                            // Tất cả các dữ liệu đã được get thành công
                                            callback.onSuccess(userDto);
                                        }
                                    }
                                });
                            } else {
                                Exception e = new Exception("User not found");
                                BugLogRepository.logErrorToDatabase(e, "Method getProfile - id = " + userId);
                                callback.onFailure(e);
                            }
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public void login(String username, String password, FirestoreCallback<UserDto> callback) {
        db.collection("users").whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isLogin = false;
                            for (DocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    if (user.getPassword().equals(password)) {
                                        user.setUserId(document.getId());
                                        isLogin = true;
                                        // Convert to dto
                                        UserDto userDto = convertToDto(user);
                                        callback.onSuccess(userDto);
                                    }
                                }
                                break;
                            }

                            if (!isLogin) {
                                Exception e = new Exception("Tài khoản hoặc mật khẩu không chính xác");
                                callback.onFailure(e);
                            }
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
    }

    public void register(String username, String password, FirestoreCallback<Boolean> callback) {
        db.collection("users").whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean userExist = false;
                            for (DocumentSnapshot document : task.getResult()) {
                                userExist = true;
                                break;
                            }

                            if (userExist) {
                                callback.onFailure(new Exception("User already exists"));
                            } else {
                                User user = new User();
                                user.setName_display(username + " " + "user");
                                user.setUsername(username);
                                user.setPassword(password);
                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> {
                                            callback.onSuccess(true);
                                        })
                                        .addOnFailureListener(e -> {
                                            callback.onFailure(new Exception("Register failed " + e.getMessage()));
                                        });
                            }
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public void addComment() {}

    public void updateComment() {}

    public void deleteComment() {}
}
