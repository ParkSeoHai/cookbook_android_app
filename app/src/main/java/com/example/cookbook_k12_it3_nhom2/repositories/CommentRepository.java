package com.example.cookbook_k12_it3_nhom2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook_k12_it3_nhom2.models.Comment;
import com.example.cookbook_k12_it3_nhom2.models.Recipe;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.CommentDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.RecipeDto;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.UserDto;
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

public class CommentRepository {
    private final FirebaseFirestore db;

    public CommentRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void initData() {
        List<Comment> comments = new ArrayList<>();

        comments.add(new Comment(
                "OG8Zo4BYcxH0tFgZWOZm",  // Recipe 1: Bún Thịt Nướng
                "9vQ00cgYviaraZuulQiT",
                "Món này ngon lắm, cả nhà mình đều thích!",
                4,
                "2024-08-18 10:15:30"
        ));

        comments.add(new Comment(
                "mxlYa95bv45KQboeHaNq",  // Recipe 2: Salad Trái Cây
                "9vQ00cgYviaraZuulQiT",
                "Phở bò vị chuẩn, dễ nấu, mình sẽ thử lại lần nữa.",
                5,
                "2024-08-18 11:20:45"
        ));

        comments.add(new Comment(
                "kZWdcauUWkdty3oEB8xk",  // Recipe 4: Cá Hồi Nướng
                "9vQ00cgYviaraZuulQiT",
                "Gà rang mặn rất thơm và đậm đà, gia đình mình rất thích.",
                2,
                "2024-08-18 12:30:00"
        ));

        comments.add(new Comment(
                "oFSjvQM9bfm0DI4iGpOR",  // Recipe 5: Bánh Flan
                "9vQ00cgYviaraZuulQiT",
                "Salad trái cây tươi mát và ngon miệng, rất phù hợp cho mùa hè.",
                5,
                "2024-08-18 13:45:10"
        ));

        comments.add(new Comment(
                "ow0FxC0LwYMZMHS2c5n5",  // Recipe 6: Gà Rang Mặn
                "9vQ00cgYviaraZuulQiT",
                "Bánh flan mềm mịn, hương vị thơm ngon, đúng kiểu mình thích.",
                5,
                "2024-08-18 14:50:55"
        ));

        // Insert data
        for (Comment cmt : comments) {
            db.collection("comments")
                    .add(cmt)
                    .addOnSuccessListener(documentReference -> {
                        Log.i("insert comment", cmt.getContent());
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi
                        Log.e("error", e.getMessage().toString());
                    });
        }
    }

    public CommentDto convertToDto(@NonNull Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(comment.getCommentId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setRating(comment.getRating());
        return commentDto;
    }

    public Task<Void> allByUserId(String userId, FirestoreCallback<List<CommentDto>> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("comments")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Tạo danh sách Task bất đồng bộ
                            List<Task<Void>> tasks = new ArrayList<>();
                            List<CommentDto> commentDtos = new ArrayList<>();

                            for (DocumentSnapshot document : task.getResult()) {
                                Comment comment = document.toObject(Comment.class);
                                comment.setCommentId(document.getId());

                                // Convert to CommentDto
                                CommentDto commentDto = convertToDto(comment);

                                // Get recipe by recipeId
                                RecipeRepository recipeRepository = new RecipeRepository();
                                Task<Void> recipeTask = recipeRepository.findById(comment.getRecipeId(), new FirestoreCallback<RecipeDto>() {
                                    @Override
                                    public void onSuccess(RecipeDto result) {
                                        commentDto.setRecipeDto(result);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                }).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        commentDtos.add(commentDto);
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
                                        callback.onSuccess(commentDtos);
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
                });
        return taskCompletionSource.getTask();
    }

    public Task<Void> allByRecipeId(String recipeId, FirestoreCallback<List<CommentDto>> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("recipes").document(recipeId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot recipe = task.getResult();
                            if (recipe != null && recipe.exists()) {
                                db.collection("comments").whereEqualTo("recipeId", recipeId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> taskComment) {
                                                if (taskComment.isSuccessful()) {
                                                    // Tạo danh sách Task bất đồng bộ
                                                    List<Task<Void>> tasks = new ArrayList<>();
                                                    List<CommentDto> commentDtos = new ArrayList<>();

                                                    for (DocumentSnapshot document : taskComment.getResult()) {
                                                        Comment comment = document.toObject(Comment.class);
                                                        comment.setCommentId(document.getId());
                                                        CommentDto commentDto = convertToDto(comment);

                                                        // Get user comment by id
                                                        UserRepository userRepository = new UserRepository();
                                                        Task<Void> taskUser = userRepository.findById(comment.getUserId(), new FirestoreCallback<UserDto>() {
                                                            @Override
                                                            public void onSuccess(UserDto userDto) {
                                                                commentDto.setUserDto(userDto);
                                                            }
                                                            @Override
                                                            public void onFailure(Exception e) {
                                                                callback.onFailure(e);
                                                            }
                                                        }).addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful()) {
                                                                commentDtos.add(commentDto);
                                                            } else {
                                                                callback.onFailure(task1.getException());
                                                            }
                                                        });
                                                        tasks.add(taskUser);  // Thêm Task vào danh sách
                                                    }

                                                    // Chờ tất cả các Task hoàn thành
                                                    Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                callback.onSuccess(commentDtos);
                                                                taskCompletionSource.setResult(null);
                                                            } else {
                                                                callback.onFailure(task.getException());
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            } else {
                                Exception e = new Exception("Recipe not found");
                                callback.onFailure(e);
                                taskCompletionSource.setException(e);
                                BugLogRepository.logErrorToDatabase(e, "Method allByRecipeId with id = " + recipeId);
                            }
                        }
                    }
                });

        return taskCompletionSource.getTask();
    }
}
