package vn.nhom2eaut.bookchefk12.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import vn.nhom2eaut.bookchefk12.models.Comment;
import vn.nhom2eaut.bookchefk12.models.Favorite;
import vn.nhom2eaut.bookchefk12.models.User;
import vn.nhom2eaut.bookchefk12.repositories.dtos.CommentDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.UserDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseFirestore db;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    // Phương thức chuyển đổi models -> dtos
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

    // Phương thức tìm kiếm người dùng theo id
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

    // Phương thức lấy dữ liệu chi tiết về người dùng
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
//                                FavoriteRepository favoriteRepository = new FavoriteRepository();
//                                Task<Void> favTask = favoriteRepository.allByUserId(userDto.getUserId(), new FirestoreCallback<List<FavoriteDto>>() {
//                                    @Override
//                                    public void onSuccess(List<FavoriteDto> result) {
//                                        userDto.setFavoriteDtos(result);
//                                    }
//                                    @Override
//                                    public void onFailure(Exception e) {
//                                        callback.onFailure(e);
//                                    }
//                                });
//                                tasks.add(favTask);

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

    // Phương thức đăng nhập
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

    // Phương thức đăng ký tài khoản
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
                                callback.onFailure(new Exception("Username đã được sử dụng"));
                            } else {
                                User user = new User();
                                user.setName_display(username.toUpperCase());
                                user.setUsername(username);
                                user.setPassword(password);
                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> {
                                            callback.onSuccess(true);
                                        })
                                        .addOnFailureListener(e -> {
                                            callback.onFailure(new Exception("Đăng kí thất bại " + e.getMessage()));
                                        });
                            }
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    // Phương thức kiểm tra công thức có trong danh sách công thức yêu thích hay chưa
    public void checkRecipeInFavorite(String recipeId, String userId, FirestoreCallback<Boolean> callback) {
        db.collection("favorites")
                .whereEqualTo("recipeId", recipeId)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // Công thức nấu ăn đã nằm trong danh sách yêu thích
                                callback.onSuccess(true);
                            } else {
                                // Công thức nấu ăn không nằm trong danh sách yêu thích
                                callback.onSuccess(false);
                            }
                        } else {
                            // Trường hợp không thành công trong quá trình truy vấn (ví dụ: lỗi kết nối)
                            callback.onFailure(task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu truy vấn thất bại hoàn toàn
                        callback.onFailure(e);
                    }
                });
    }

    // Phương thức thêm công thức vào danh sách yêu thích
    public void addRecipeToFavorite(Favorite favorite, FirestoreCallback<Boolean> callback) {
        db.collection("favorites")
                .add(favorite)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onSuccess(true);
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(new Exception("Thêm công thức vào danh sách yêu thích thất bại " + e.getMessage()));
                });
    }

    // Phương thức xóa công thức khỏi danh sách yêu thích
    public void removeRecipeInFavorite(String recipeId, String userId, FirestoreCallback<Boolean> callback) {
        db.collection("favorites")
                .whereEqualTo("recipeId", recipeId)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Lặp qua các tài liệu và xóa tài liệu
                                List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                    DocumentReference docRef = documentSnapshot.getReference();
                                    docRef.delete()
                                            .addOnSuccessListener(aVoid -> {
                                                // Xóa thành công
                                                callback.onSuccess(true);
                                            })
                                            .addOnFailureListener(e -> {
                                                // Xóa thất bại
                                                callback.onSuccess(false);
                                                callback.onFailure(e);
                                            });
                                    break;
                                }
                            } else {
                                // Không tìm thấy tài liệu nào để xóa
                                Log.i("error removeRecipeInFavorite", "No documents found with recipeId: " + recipeId);
                                callback.onSuccess(false); // No documents to remove
                            }
                        } else {
                            // Trường hợp không thành công trong quá trình truy vấn (ví dụ: lỗi kết nối)
                            callback.onFailure(task.getException());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu truy vấn thất bại
                    callback.onFailure(e);
                });
    }

    // Phương thức thêm đánh giá công thức
    public void addComment(Comment comment, FirestoreCallback<Boolean> callback) {
        // Kiểm tra comment tồn tại chưa, mỗi user chỉ comment 1 lần / recipe
        db.collection("comments")
                .whereEqualTo("recipeId", comment.getRecipeId())
                .whereEqualTo("userId", comment.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isExits = false;
                            for (DocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    isExits = true;
                                }
                                break;
                            }
                            if (!isExits) {
                                db.collection("comments")
                                        .add(comment)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                callback.onSuccess(true);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            callback.onFailure(new Exception("Thêm đánh giá thất bại: " + e.getMessage()));
                                        });
                            } else {
                                callback.onFailure(new Exception("Bạn đã có đánh giá cho công thức này"));
                            }
                        }
                    }
                });
    }

}
