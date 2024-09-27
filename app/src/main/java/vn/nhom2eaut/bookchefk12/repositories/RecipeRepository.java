package vn.nhom2eaut.bookchefk12.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import vn.nhom2eaut.bookchefk12.models.Ingredient;
import vn.nhom2eaut.bookchefk12.models.Recipe;
import vn.nhom2eaut.bookchefk12.models.Recipe_Ingredient;
import vn.nhom2eaut.bookchefk12.models.Step;
import vn.nhom2eaut.bookchefk12.repositories.dtos.CommentDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.IngredientDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeRepository {
    private final FirebaseFirestore db;

    public RecipeRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    // Phương thức khởi tạo dữ liệu
    public void initData() {
        List<Recipe> recipes = new ArrayList<>();

        // Recipe 1: Bún Thịt Nướng
        List<Recipe_Ingredient> bunThitNuongIngredients = new ArrayList<>();
        bunThitNuongIngredients.add(new Recipe_Ingredient("ky4mVZGkpIFF4NBewO1c", 200));    // Thịt bò
        bunThitNuongIngredients.add(new Recipe_Ingredient("Gi6KsjCb3wRbAd8jJqzs", 150));    // Bún tươi
        bunThitNuongIngredients.add(new Recipe_Ingredient("yplJ6Xd7s1MRZuedL90z", 1));  // Hành tây
        bunThitNuongIngredients.add(new Recipe_Ingredient("mz5GpSe40Yeh5bGoenDT", 3));   // Tỏi

        List<Step> bunThitNuongSteps = new ArrayList<>();
        bunThitNuongSteps.add(new Step(1, "", "Ướp thịt bò với tỏi và gia vị."));
        bunThitNuongSteps.add(new Step(2, "", "Nướng thịt bò cho đến khi chín."));
        bunThitNuongSteps.add(new Step(3, "", "Luộc bún tươi và xếp lên đĩa."));
        bunThitNuongSteps.add(new Step(4, "", "Đặt thịt bò lên bún và trang trí với hành tây thái lát."));

        Map<String, String> bunThitNuongTime = new HashMap<>();
        bunThitNuongTime.put("prep_time", "20 phút");
        bunThitNuongTime.put("cook_time", "30 phút");

//        Map<String, Object> bunThitNuongRating = new HashMap<>();
//        bunThitNuongRating.put("average_rating", 4.5);
//        bunThitNuongRating.put("number_of_ratings", 120);
//
//        Map<String, Integer> bunThitNuongRatingsByStars = new HashMap<>();
//        bunThitNuongRatingsByStars.put("5", 80);
//        bunThitNuongRatingsByStars.put("4", 30);
//        bunThitNuongRatingsByStars.put("3", 10);

        recipes.add(new Recipe(
                "Bún Thịt Nướng",
                "Món bún thịt nướng thơm ngon, dễ làm với thịt bò nướng và bún tươi.",
                "9vQ00cgYviaraZuulQiT",
                "GW2H6A2DdrFm1CgAyLfO",     // Món chính
                bunThitNuongIngredients,
                bunThitNuongSteps,
                bunThitNuongTime,
                2,
                "https://example.com/bun_thit_nuong.jpg"
        ));

        // Recipe 2: Xôi Gấc
        List<Recipe_Ingredient> xoiGacIngredients = new ArrayList<>();
        xoiGacIngredients.add(new Recipe_Ingredient("DhcgrGNyKBuTPOdolW3M", 300));  // Gạo nếp
        xoiGacIngredients.add(new Recipe_Ingredient("Rixa5H6yrhKuT8a3Xrrq", 1));     // Gấc
        xoiGacIngredients.add(new Recipe_Ingredient("JYe5lRUwdpoLDhcCdfzm", 100));    // Dừa

        List<Step> xoiGacSteps = new ArrayList<>();
        xoiGacSteps.add(new Step(1, "", "Ngâm gạo nếp trong nước qua đêm."));
        xoiGacSteps.add(new Step(2, "", "Trộn gạo nếp với thịt gấc đã làm sạch."));
        xoiGacSteps.add(new Step(3, "", "Hấp xôi cho đến khi chín."));
        xoiGacSteps.add(new Step(4, "", "Rắc dừa nạo lên xôi trước khi ăn."));

        Map<String, String> xoiGacTime = new HashMap<>();
        xoiGacTime.put("prep_time", "10 phút");
        xoiGacTime.put("cook_time", "40 phút");

//        Map<String, Object> xoiGacRating = new HashMap<>();
//        xoiGacRating.put("average_rating", 4.7);
//        xoiGacRating.put("number_of_ratings", 95);
//
//        Map<String, Integer> xoiGacRatingsByStars = new HashMap<>();
//        xoiGacRatingsByStars.put("5", 70);
//        xoiGacRatingsByStars.put("4", 20);
//        xoiGacRatingsByStars.put("3", 5);

        recipes.add(new Recipe(
                "Xôi Gấc",
                "Xôi gấc truyền thống với màu đỏ tươi đẹp mắt và hương vị đặc trưng.",
                "9vQ00cgYviaraZuulQiT",
                "zjUpySjqvXmnmCcqAR4j",     // Ăn sáng
                xoiGacIngredients,
                xoiGacSteps,
                xoiGacTime,
                3,
                "https://example.com/xoi_gac.jpg"
        ));

        // Recipe 3: Cá Hồi Nướng
        List<Recipe_Ingredient> caHoiNuongIngredients = new ArrayList<>();
        caHoiNuongIngredients.add(new Recipe_Ingredient("h4bJrNUBCQSVsNcqgrm9", 250));  // Cá hồi
        caHoiNuongIngredients.add(new Recipe_Ingredient("C8ePV1nXAGzrfkHQkUPa", 2));    // Dầu olive
        caHoiNuongIngredients.add(new Recipe_Ingredient("mz5GpSe40Yeh5bGoenDT", 2));    // Tỏi

        List<Step> caHoiNuongSteps = new ArrayList<>();
        caHoiNuongSteps.add(new Step(1, "", "Ướp cá hồi với dầu olive và tỏi."));
        caHoiNuongSteps.add(new Step(2, "", "Nướng cá hồi cho đến khi chín vàng."));
        caHoiNuongSteps.add(new Step(3, "", "Thưởng thức với rau sống và sốt chanh."));

        Map<String, String> caHoiNuongTime = new HashMap<>();
        caHoiNuongTime.put("prep_time", "15 phút");
        caHoiNuongTime.put("cook_time", "20 phút");

//        Map<String, Object> caHoiNuongRating = new HashMap<>();
//        caHoiNuongRating.put("average_rating", 4.8);
//        caHoiNuongRating.put("number_of_ratings", 150);
//
//        Map<String, Integer> caHoiNuongRatingsByStars = new HashMap<>();
//        caHoiNuongRatingsByStars.put("5", 100);
//        caHoiNuongRatingsByStars.put("4", 40);
//        caHoiNuongRatingsByStars.put("3", 10);

        recipes.add(new Recipe(
                "Cá Hồi Nướng",
                "Cá hồi nướng với hương vị tuyệt vời, phù hợp cho bữa tối nhẹ nhàng.",
                "9vQ00cgYviaraZuulQiT",
                "ihbWcbMTIzYUEaxttCIZ",     // Hải sản
                caHoiNuongIngredients,
                caHoiNuongSteps,
                caHoiNuongTime,
                2,
                "https://example.com/ca_houi_nuong.jpg"
        ));

        // Recipe 4: Gà Rang Mặn
        List<Recipe_Ingredient> gaRangManIngredients = new ArrayList<>();
        gaRangManIngredients.add(new Recipe_Ingredient("JrAAx41In7Dd8R2v471g", 300));    // Thịt gà
        gaRangManIngredients.add(new Recipe_Ingredient("mz5GpSe40Yeh5bGoenDT", 3));     // Tỏi
        gaRangManIngredients.add(new Recipe_Ingredient("JUxYaCYMf8j3xTYKbf5D", 2));   // Dầu mè
        gaRangManIngredients.add(new Recipe_Ingredient("yplJ6Xd7s1MRZuedL90z", 1));   // Hành tây
        gaRangManIngredients.add(new Recipe_Ingredient("yInk6h0mSvKH2Ouddi5K", 2));     // Nước tương

        List<Step> gaRangManSteps = new ArrayList<>();
        gaRangManSteps.add(new Step(1, "", "Ướp thịt gà với tỏi và nước tương."));
        gaRangManSteps.add(new Step(2, "", "Chiên thịt gà với dầu mè cho đến khi chín vàng."));
        gaRangManSteps.add(new Step(3, "", "Thêm hành tây vào và đảo đều cho đến khi hành mềm."));
        gaRangManSteps.add(new Step(4, "", "Cho ra đĩa và thưởng thức."));

        Map<String, String> gaRangManTime = new HashMap<>();
        gaRangManTime.put("prep_time", "20 phút");
        gaRangManTime.put("cook_time", "25 phút");

//        Map<String, Object> gaRangManRating = new HashMap<>();
//        gaRangManRating.put("average_rating", 4.6);
//        gaRangManRating.put("number_of_ratings", 110);
//
//        Map<String, Integer> gaRangManRatingsByStars = new HashMap<>();
//        gaRangManRatingsByStars.put("5", 65);
//        gaRangManRatingsByStars.put("4", 35);
//        gaRangManRatingsByStars.put("3", 10);

        recipes.add(new Recipe(
                "Gà Rang Mặn",
                "Thịt gà rang mặn với hương vị đậm đà, thích hợp cho bữa cơm gia đình.",
                "9vQ00cgYviaraZuulQiT",
                "GW2H6A2DdrFm1CgAyLfO",     // Món chính
                gaRangManIngredients,
                gaRangManSteps,
                gaRangManTime,
                4,
                "https://example.com/ga_rang_man.jpg"
        ));

        // Recipe 5: Salad Trái Cây
        List<Recipe_Ingredient> saladTraiCayIngredients = new ArrayList<>();
        saladTraiCayIngredients.add(new Recipe_Ingredient("csnuA2vNLhxVxYIzWN0q", 2));   // Táo
        saladTraiCayIngredients.add(new Recipe_Ingredient("Omymi9LiUEUcApfD5VFK", 2));   // Chuối
        saladTraiCayIngredients.add(new Recipe_Ingredient("LuqtMHL8wm8BCkOpocl8", 1));    // Dứa
        saladTraiCayIngredients.add(new Recipe_Ingredient("Cb3nYfFVOn1h1br0VtFY", 1));    // Dưa hấu
        saladTraiCayIngredients.add(new Recipe_Ingredient("16gSh2Fafk8ohh6SF43e", 2));    // Nước cốt chanh
        saladTraiCayIngredients.add(new Recipe_Ingredient("oc1gFnq94uI8R4jePmea", 1));    // Mật ong

        List<Step> saladTraiCaySteps = new ArrayList<>();
        saladTraiCaySteps.add(new Step(1, "", "Gọt vỏ và cắt trái cây thành miếng nhỏ."));
        saladTraiCaySteps.add(new Step(2, "", "Trộn tất cả các loại trái cây với nhau."));
        saladTraiCaySteps.add(new Step(3, "", "Thêm nước cốt chanh và mật ong vào trộn đều."));
        saladTraiCaySteps.add(new Step(4, "", "Để lạnh trước khi thưởng thức."));

        Map<String, String> saladTraiCayTime = new HashMap<>();
        saladTraiCayTime.put("prep_time", "15 phút");
        saladTraiCayTime.put("cook_time", "0 phút");

//        Map<String, Object> saladTraiCayRating = new HashMap<>();
//        saladTraiCayRating.put("average_rating", 4.9);
//        saladTraiCayRating.put("number_of_ratings", 85);
//
//        Map<String, Integer> saladTraiCayRatingsByStars = new HashMap<>();
//        saladTraiCayRatingsByStars.put("5", 70);
//        saladTraiCayRatingsByStars.put("4", 15);
//        saladTraiCayRatingsByStars.put("3", 0);

        recipes.add(new Recipe(
                "Salad Trái Cây",
                "Salad trái cây tươi ngon, bổ dưỡng và dễ làm, thích hợp cho bữa ăn nhẹ.",
                "9vQ00cgYviaraZuulQiT",
                "zDllvqM6L8XqtPudpoFu",     // Món ăn vặt
                saladTraiCayIngredients,
                saladTraiCaySteps,
                saladTraiCayTime,
                2,
                "https://example.com/salad_trai_cay.jpg"
        ));

        // Recipe 6: Bánh Flan
        List<Recipe_Ingredient> banhFlanIngredients = new ArrayList<>();
        banhFlanIngredients.add(new Recipe_Ingredient("0uru7erg4vVASVKCPrYY", 500));  // Sữa tươi
        banhFlanIngredients.add(new Recipe_Ingredient("Nw6W4jLpIA5QPSLtkbVw", 4));   // Trứng gà
        banhFlanIngredients.add(new Recipe_Ingredient("22UAJWl83nfj7TNeUbyI", 100));    // Đường
        banhFlanIngredients.add(new Recipe_Ingredient("ompmATag4FVVv13tfCJr", 1));  // Tinh chất vani

        List<Step> banhFlanSteps = new ArrayList<>();
        banhFlanSteps.add(new Step(1, "", "Đun sữa tươi với đường cho đến khi tan chảy."));
        banhFlanSteps.add(new Step(2, "", "Khuấy trứng gà với tinh chất vani."));
        banhFlanSteps.add(new Step(3, "", "Trộn sữa và trứng lại với nhau, rồi đổ vào khuôn."));
        banhFlanSteps.add(new Step(4, "", "Hấp cách thủy trong khoảng 45 phút cho đến khi bánh chín."));

        Map<String, String> banhFlanTime = new HashMap<>();
        banhFlanTime.put("prep_time", "15 phút");
        banhFlanTime.put("cook_time", "45 phút");

//        Map<String, Object> banhFlanRating = new HashMap<>();
//        banhFlanRating.put("average_rating", 4.8);
//        banhFlanRating.put("number_of_ratings", 120);
//
//        Map<String, Integer> banhFlanRatingsByStars = new HashMap<>();
//        banhFlanRatingsByStars.put("5", 85);
//        banhFlanRatingsByStars.put("4", 30);
//        banhFlanRatingsByStars.put("3", 5);

        recipes.add(new Recipe(
                "Bánh Flan",
                "Bánh flan mịn màng với hương vị ngọt ngào, là món tráng miệng lý tưởng.",
                "9vQ00cgYviaraZuulQiT",
                "E1htmV4BrBoZK5OpGTII",     // Các loại bánh
                banhFlanIngredients,
                banhFlanSteps,
                banhFlanTime,
                4,
                "https://example.com/banh_flan.jpg"
        ));

        // Insert data
        for (Recipe recipe : recipes) {
            db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener(documentReference -> {
                        Log.i("insert recipe", recipe.getTitle());
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi
                        Log.e("error", e.getMessage().toString());
                    });
        }
    }

    // Phương thức chuyển đổi dữ liệu models -> dtos
    public RecipeDto convertToDto(@NonNull Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeId(recipe.getRecipeId());
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setServings(recipe.getServings());
        recipeDto.setImageUrl(recipe.getImageUrl());
        recipeDto.setTime(recipe.getTime());
        return recipeDto;
    }

    // Phương thức lấy danh sách công thức nấu ăn
    public Task<Void> all(FirestoreCallback<List<RecipeDto>> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<RecipeDto> recipes = new ArrayList<>();
                            List<Task<Void>> allTasks = new ArrayList<>();  // Danh sách tất cả các Task để chờ
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                recipe.setRecipeId(document.getId());
                                RecipeDto recipeDto = convertToDto(recipe);

                                // Lấy bình luận
                                CommentRepository commentRepository = new CommentRepository();

                                Task<Void> taskComment = commentRepository.allByRecipeId(recipeDto.getRecipeId(), new FirestoreCallback<List<CommentDto>>() {
                                    @Override
                                    public void onSuccess(List<CommentDto> commentDtos) {
                                        recipeDto.setCommentDtos(commentDtos);
                                        updateRatings(recipeDto);  // Tính toán rating
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                });

                                allTasks.add(taskComment);
                                recipes.add(recipeDto);
                            }

                            // Đợi tất cả các task hoàn thành
                            Tasks.whenAll(allTasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> allTask) {
                                    if (allTask.isSuccessful()) {
                                        callback.onSuccess(recipes);
                                    } else {
                                        callback.onFailure(allTask.getException());
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
    // Tính toán rating
    private void updateRatings(RecipeDto recipeDto) {
        int number_of_ratings = 0;      // Tổng số đánh giá
        float totalRating = 0;          // Tổng số điểm đánh giá
        int countOneStar = 0;
        int countTwoStar = 0;
        int countThreeStar = 0;
        int countFourStar = 0;
        int countFiveStar = 0;

        for (CommentDto cmt : recipeDto.getCommentDtos()) {
            switch (cmt.getRating()) {
                case 1:
                    countOneStar++;
                    break;
                case 2:
                    countTwoStar++;
                    break;
                case 3:
                    countThreeStar++;
                    break;
                case 4:
                    countFourStar++;
                    break;
                case 5:
                    countFiveStar++;
                    break;
            }
            totalRating += cmt.getRating();
            number_of_ratings++;
        }

        Map<String, Integer> ratingsByStar = new HashMap<>();
        ratingsByStar.put("1", countOneStar);
        ratingsByStar.put("2", countTwoStar);
        ratingsByStar.put("3", countThreeStar);
        ratingsByStar.put("4", countFourStar);
        ratingsByStar.put("5", countFiveStar);

        // Đặt tổng số đánh giá vào recipeDto
        recipeDto.setNumber_of_ratings(number_of_ratings);

        // Tính toán điểm trung bình
        float average_rating = 0;
        if (number_of_ratings > 0) {
            average_rating = totalRating / number_of_ratings;
        }

        // Làm tròn đến 1 chữ số thập phân: ví dụ 4.66667 -> 4.7
        BigDecimal average_rating_format = new BigDecimal(average_rating);
        average_rating_format = average_rating_format.setScale(1, RoundingMode.HALF_UP);

        // Đặt điểm trung bình và số lượng đánh giá theo sao vào recipeDto
        recipeDto.setAverage_rating(average_rating_format.floatValue());
        recipeDto.setRatingByStars(ratingsByStar);
    }

    // Phương thức tìm kiếm công thức theo id
    public Task<Void> findById(String recipeId, FirestoreCallback<RecipeDto> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("recipes").document(recipeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                recipe.setRecipeId(document.getId());
                                // Convert to recipeDto
                                RecipeDto recipeDto = convertToDto(recipe);
                                recipeDto.setIngredients(recipe.getIngredients());
                                recipeDto.setSteps(recipe.getSteps());
                                callback.onSuccess(recipeDto);
                                taskCompletionSource.setResult(null);
                            } else {
                                Exception e = new Exception("Recipe not found");
                                BugLogRepository.logErrorToDatabase(e, "Method findById id = " + recipeId);
                                callback.onFailure(e);
                            }
                        } else {
                            Exception e = task.getException();
                            if (e != null) {
                                callback.onFailure(e);
                                BugLogRepository.logErrorToDatabase(e, "Method findById");
                            }
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

    // Phuương thức lấy danh sách công thức theo id người dùng
    public Task<Void> allByUserId(String userId, FirestoreCallback<List<RecipeDto>> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("recipes")
                .whereEqualTo("authorId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<RecipeDto> recipes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                recipe.setRecipeId(document.getId());
                                // Convert to RecipeDto
                                RecipeDto recipeDto = convertToDto(recipe);
                                recipes.add(recipeDto);
                            }
                            callback.onSuccess(recipes);
                            taskCompletionSource.setResult(null);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

    // Phương thức lấy danh sách công thức theo id danh mục
    public Task<Void> allByCategoryId(String categoryId, FirestoreCallback<List<RecipeDto>> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("recipes")
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<RecipeDto> recipes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                recipe.setRecipeId(document.getId());
                                // Convert to RecipeDto
                                RecipeDto recipeDto = convertToDto(recipe);
                                Log.i("recipe category", recipeDto.toString());
                                recipes.add(recipeDto);
                            }
                            callback.onSuccess(recipes);
                            taskCompletionSource.setResult(null);
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

    // Phương thức tìm kiếm công thức theo tiêu đề món ăn
    public void searchByTitle(String title, FirestoreCallback<List<RecipeDto>> callback) {
        db.collection("recipes")
                .whereGreaterThanOrEqualTo("title", title)
                .whereLessThanOrEqualTo("title", title + "\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<RecipeDto> recipeDtos = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                recipe.setRecipeId(document.getId());
                                // Convert to Dto
                                RecipeDto recipeDto = convertToDto(recipe);
                                recipeDtos.add(recipeDto);
                            }
                            callback.onSuccess(recipeDtos);
                        } else {
                            callback.onFailure(new Exception("Lỗi khi tìm kiếm công thức nấu ăn"));
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

    // Phương thức lấy dữ liệu chi tiết của công thức nấu ăn theo id
    public Task<Void> detail(String recipeId, FirestoreCallback<RecipeDto> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("recipes")
                .document(recipeId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                List<Task<Void>> tasks = new ArrayList<>();

                                Recipe recipe = document.toObject(Recipe.class);
                                recipe.setRecipeId(document.getId());
                                // Convert to dto
                                RecipeDto recipeDto = convertToDto(recipe);
//                                recipeDto.setIngredients(recipe.getIngredients());
//                                recipeDto.setSteps(recipe.getSteps());

                                // Get author by id
//                                UserRepository userRepository = new UserRepository();
//                                Task<Void> taskAuthor = userRepository.findById(recipe.getAuthorId(), new FirestoreCallback<UserDto>() {
//                                    @Override
//                                    public void onSuccess(UserDto result) {
//                                        recipeDto.setAuthor(result);
//                                    }
//                                    @Override
//                                    public void onFailure(Exception e) {
//                                        callback.onFailure(e);
//                                    }
//                                });
//                                tasks.add(taskAuthor);

                                // Get category by id
//                                CategoryRepository categoryRepository = new CategoryRepository();
//                                Task<Void> taskCategory = categoryRepository.findById(recipe.getCategoryId(), new FirestoreCallback<CategoryDto>() {
//                                    @Override
//                                    public void onSuccess(CategoryDto categoryDto) {
//                                        recipeDto.setCategory(categoryDto);
//                                    }
//                                    @Override
//                                    public void onFailure(Exception e) {
//                                        callback.onFailure(e);
//                                    }
//                                });
//                                tasks.add(taskCategory);

                                CommentRepository commentRepository = new CommentRepository();      // Get comments
                                Task<Void> taskComment = commentRepository.allByRecipeId(recipeDto.getRecipeId(), new FirestoreCallback<List<CommentDto>>() {
                                    @Override
                                    public void onSuccess(List<CommentDto> commentDtos) {
                                        recipeDto.setCommentDtos(commentDtos);
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        callback.onFailure(e);
                                    }
                                });

                                tasks.add(taskComment);

                                // Đợi tất cả các task hoàn thành
                                Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> allTasks) {
                                        if (allTasks.isSuccessful()) {
                                            // Get rating
                                            int number_of_ratings = 0;      // Tổng số đánh giá
                                            float totalRating = 0;            // Tổng số điểm đánh giá
                                            int countOneStar = 0;
                                            int countTwoStar = 0;
                                            int countThreeStar = 0;
                                            int countFourStar = 0;
                                            int countFiveStar = 0;
                                            for (CommentDto cmt : recipeDto.getCommentDtos()) {
                                                switch (cmt.getRating()) {
                                                    case 1:
                                                        countOneStar++;
                                                        break;
                                                    case 2:
                                                        countTwoStar++;
                                                        break;
                                                    case 3:
                                                        countThreeStar++;
                                                        break;
                                                    case 4:
                                                        countFourStar++;
                                                        break;
                                                    case 5:
                                                        countFiveStar++;
                                                        break;
                                                }
                                                totalRating += cmt.getRating();
                                                number_of_ratings++;
                                            }
                                            Map<String, Integer> ratingsByStar = new HashMap<>();
                                            ratingsByStar.put("1", countOneStar);
                                            ratingsByStar.put("2", countTwoStar);
                                            ratingsByStar.put("3", countThreeStar);
                                            ratingsByStar.put("4", countFourStar);
                                            ratingsByStar.put("5", countFiveStar);

                                            recipeDto.setNumber_of_ratings(number_of_ratings);
                                            float average_rating = 0;
                                            if (number_of_ratings > 0) {
                                                average_rating = totalRating / number_of_ratings;
                                            }

                                            BigDecimal average_rating_format = new BigDecimal(average_rating);
                                            average_rating_format = average_rating_format.setScale(1, RoundingMode.HALF_UP);  // Làm tròn đến 1 chữ số thập phân: vd: 4.66667 -> 4.7

                                            recipeDto.setAverage_rating(average_rating_format.floatValue());
                                            recipeDto.setRatingByStars(ratingsByStar);

                                            // Tất cả các dữ liệu đã được get thành công
                                            callback.onSuccess(recipeDto);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

    // Phương thức lấy danh sách nguyên liệu của công thức theo id công thức
    public Task<Void> getIngredientsByRecipeId(String recipeId, FirestoreCallback<List<IngredientDto>> callback) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        findById(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto recipeDto) {
                IngredientRepository ingredientRepository = new IngredientRepository();
                List<IngredientDto> ingredientDtos = new ArrayList<>();
                List<Task<Void>> tasks = new ArrayList<>();

                // Lặp các nguyên liệu trong công thức (recipe)
                for (Recipe_Ingredient ingredient : recipeDto.getIngredients()) {
                    IngredientDto ingredientDto = new IngredientDto();
                    ingredientDto.setIngredientId(ingredient.getIngredientId());
                    ingredientDto.setQuantity(ingredient.getQuantity());

                    // Tạo TaskCompletionSource cho từng ingredient
                    TaskCompletionSource<Void> ingredientTaskSource = new TaskCompletionSource<>();

                    // Get ingredient by id
                    ingredientRepository.findById(ingredientDto.getIngredientId(), new FirestoreCallback<Ingredient>() {
                        @Override
                        public void onSuccess(Ingredient result) {
                            // Cập nhật thông tin nguyên liệu
                            ingredientDto.setName(result.getName());
                            ingredientDto.setCategory(result.getCategory());
                            ingredientDto.setUnit(result.getUnit());

                            // Thêm vào danh sách nguyên liệu
                            ingredientDtos.add(ingredientDto);

                            // Đánh dấu hoàn thành task cho ingredient này
                            ingredientTaskSource.setResult(null);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Báo lỗi cho task này
                            ingredientTaskSource.setException(e);
                        }
                    });

                    // Thêm task của từng ingredient vào danh sách task tổng
                    tasks.add(ingredientTaskSource.getTask());
                }

                // Đợi tất cả các task hoàn thành
                Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> allTasks) {
                        if (allTasks.isSuccessful()) {
                            // Khi tất cả các task đều hoàn thành, trả về danh sách ingredient
                            callback.onSuccess(ingredientDtos);
                            taskCompletionSource.setResult(null);
                        } else {
                            // Nếu có bất kỳ lỗi nào trong các task, báo lỗi
                            callback.onFailure(allTasks.getException());
                            taskCompletionSource.setException(allTasks.getException());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
                taskCompletionSource.setException(e);
            }
        });

        return taskCompletionSource.getTask();
    }

}
