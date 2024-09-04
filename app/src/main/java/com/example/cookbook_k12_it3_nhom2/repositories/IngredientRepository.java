package com.example.cookbook_k12_it3_nhom2.repositories;

import android.util.Log;

import com.example.cookbook_k12_it3_nhom2.models.Ingredient;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class IngredientRepository {
    private final FirebaseFirestore db;

    public IngredientRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void initData() {
        List<Ingredient> ingredients = new ArrayList<>();

        // Rau củ quả
        ingredients.add(new Ingredient("Cà rốt", "gram", "Rau củ quả", "https://example.com/carrot.jpg"));
        ingredients.add(new Ingredient("Khoai tây", "gram", "Rau củ quả", "https://example.com/potato.jpg"));
        ingredients.add(new Ingredient("Hành tây", "gram", "Rau củ quả", "https://example.com/onion.jpg"));
        ingredients.add(new Ingredient("Tỏi", "tép", "Rau củ quả", "https://example.com/garlic.jpg"));
        ingredients.add(new Ingredient("Cải bó xôi", "gram", "Rau củ quả", "https://example.com/spinach.jpg"));
        ingredients.add(new Ingredient("Cà chua", "quả", "Rau củ quả", "https://example.com/tomato.jpg"));
        ingredients.add(new Ingredient("Ớt chuông", "quả", "Rau củ quả", "https://example.com/bell_pepper.jpg"));
        ingredients.add(new Ingredient("Bí đỏ", "gram", "Rau củ quả", "https://example.com/pumpkin.jpg"));
        ingredients.add(new Ingredient("Bắp cải", "gram", "Rau củ quả", "https://example.com/cabbage.jpg"));
        ingredients.add(new Ingredient("Cà tím", "quả", "Rau củ quả", "https://example.com/eggplant.jpg"));

        // Thịt
        ingredients.add(new Ingredient("Thịt bò", "gram", "Thịt", "https://example.com/beef.jpg"));
        ingredients.add(new Ingredient("Thịt gà", "gram", "Thịt", "https://example.com/chicken.jpg"));
        ingredients.add(new Ingredient("Thịt lợn", "gram", "Thịt", "https://example.com/pork.jpg"));
        ingredients.add(new Ingredient("Thịt cừu", "gram", "Thịt", "https://example.com/lamb.jpg"));
        ingredients.add(new Ingredient("Thịt vịt", "gram", "Thịt", "https://example.com/duck.jpg"));
        ingredients.add(new Ingredient("Thịt thỏ", "gram", "Thịt", "https://example.com/rabbit.jpg"));
        ingredients.add(new Ingredient("Thịt dê", "gram", "Thịt", "https://example.com/goat.jpg"));

        // Hải sản
        ingredients.add(new Ingredient("Cá hồi", "gram", "Hải sản", "https://example.com/salmon.jpg"));
        ingredients.add(new Ingredient("Tôm", "gram", "Hải sản", "https://example.com/shrimp.jpg"));
        ingredients.add(new Ingredient("Mực", "gram", "Hải sản", "https://example.com/squid.jpg"));
        ingredients.add(new Ingredient("Cua", "gram", "Hải sản", "https://example.com/crab.jpg"));
        ingredients.add(new Ingredient("Nghêu", "gram", "Hải sản", "https://example.com/clam.jpg"));
        ingredients.add(new Ingredient("Sò điệp", "gram", "Hải sản", "https://example.com/scallop.jpg"));
        ingredients.add(new Ingredient("Cá thu", "gram", "Hải sản", "https://example.com/mackerel.jpg"));

        // Các loại đậu và hạt
        ingredients.add(new Ingredient("Đậu xanh", "gram", "Đậu và hạt", "https://example.com/green_bean.jpg"));
        ingredients.add(new Ingredient("Đậu đỏ", "gram", "Đậu và hạt", "https://example.com/red_bean.jpg"));
        ingredients.add(new Ingredient("Đậu đen", "gram", "Đậu và hạt", "https://example.com/black_bean.jpg"));
        ingredients.add(new Ingredient("Đậu nành", "gram", "Đậu và hạt", "https://example.com/soybean.jpg"));
        ingredients.add(new Ingredient("Hạt điều", "gram", "Đậu và hạt", "https://example.com/cashew.jpg"));
        ingredients.add(new Ingredient("Hạt óc chó", "gram", "Đậu và hạt", "https://example.com/walnut.jpg"));
        ingredients.add(new Ingredient("Hạt hạnh nhân", "gram", "Đậu và hạt", "https://example.com/almond.jpg"));
        ingredients.add(new Ingredient("Hạt chia", "gram", "Đậu và hạt", "https://example.com/chia_seed.jpg"));

        // Ngũ cốc
        ingredients.add(new Ingredient("Gạo", "gram", "Ngũ cốc", "https://example.com/rice.jpg"));
        ingredients.add(new Ingredient("Nếp", "gram", "Ngũ cốc", "https://example.com/sticky_rice.jpg"));
        ingredients.add(new Ingredient("Lúa mì", "gram", "Ngũ cốc", "https://example.com/wheat.jpg"));
        ingredients.add(new Ingredient("Yến mạch", "gram", "Ngũ cốc", "https://example.com/oat.jpg"));
        ingredients.add(new Ingredient("Hạt quinoa", "gram", "Ngũ cốc", "https://example.com/quinoa.jpg"));
        ingredients.add(new Ingredient("Bắp", "gram", "Ngũ cốc", "https://example.com/corn.jpg"));

        // Sản phẩm từ sữa
        ingredients.add(new Ingredient("Sữa tươi", "ml", "Sản phẩm từ sữa", "https://example.com/milk.jpg"));
        ingredients.add(new Ingredient("Sữa chua", "hộp", "Sản phẩm từ sữa", "https://example.com/yogurt.jpg"));
        ingredients.add(new Ingredient("Phô mai", "gram", "Sản phẩm từ sữa", "https://example.com/cheese.jpg"));
        ingredients.add(new Ingredient("Bơ", "gram", "Sản phẩm từ sữa", "https://example.com/butter.jpg"));
        ingredients.add(new Ingredient("Kem", "ml", "Sản phẩm từ sữa", "https://example.com/cream.jpg"));

        // Trứng
        ingredients.add(new Ingredient("Trứng gà", "quả", "Trứng", "https://example.com/chicken_egg.jpg"));
        ingredients.add(new Ingredient("Trứng vịt", "quả", "Trứng", "https://example.com/duck_egg.jpg"));
        ingredients.add(new Ingredient("Trứng cút", "quả", "Trứng", "https://example.com/quail_egg.jpg"));

        // Dầu và mỡ
        ingredients.add(new Ingredient("Dầu olive", "ml", "Dầu và mỡ", "https://example.com/olive_oil.jpg"));
        ingredients.add(new Ingredient("Dầu mè", "ml", "Dầu và mỡ", "https://example.com/sesame_oil.jpg"));
        ingredients.add(new Ingredient("Dầu dừa", "ml", "Dầu và mỡ", "https://example.com/coconut_oil.jpg"));
        ingredients.add(new Ingredient("Dầu hạt cải", "ml", "Dầu và mỡ", "https://example.com/canola_oil.jpg"));
        ingredients.add(new Ingredient("Mỡ lợn", "gram", "Dầu và mỡ", "https://example.com/lard.jpg"));

        // Sản phẩm từ đậu nành
        ingredients.add(new Ingredient("Đậu hũ", "gram", "Sản phẩm từ đậu nành", "https://example.com/tofu.jpg"));
        ingredients.add(new Ingredient("Tương đậu nành", "ml", "Sản phẩm từ đậu nành", "https://example.com/soy_sauce.jpg"));
        ingredients.add(new Ingredient("Chao", "gram", "Sản phẩm từ đậu nành", "https://example.com/fermented_tofu.jpg"));
        ingredients.add(new Ingredient("Đậu nành lên men", "gram", "Sản phẩm từ đậu nành", "https://example.com/natto.jpg"));

        // Các loại mì và bún
        ingredients.add(new Ingredient("Mì gói", "gói", "Mì và bún", "https://example.com/instant_noodles.jpg"));
        ingredients.add(new Ingredient("Mì sợi", "gram", "Mì và bún", "https://example.com/noodles.jpg"));
        ingredients.add(new Ingredient("Bún tươi", "gram", "Mì và bún", "https://example.com/vermicelli.jpg"));
        ingredients.add(new Ingredient("Phở", "gram", "Mì và bún", "https://example.com/rice_noodles.jpg"));
        ingredients.add(new Ingredient("Bánh đa", "cái", "Mì và bún", "https://example.com/rice_paper.jpg"));

        // Trái cây
        ingredients.add(new Ingredient("Chuối", "quả", "Trái cây", "https://example.com/banana.jpg"));
        ingredients.add(new Ingredient("Táo", "quả", "Trái cây", "https://example.com/apple.jpg"));
        ingredients.add(new Ingredient("Nho", "chùm", "Trái cây", "https://example.com/grape.jpg"));
        ingredients.add(new Ingredient("Dứa", "quả", "Trái cây", "https://example.com/pineapple.jpg"));
        ingredients.add(new Ingredient("Cam", "quả", "Trái cây", "https://example.com/orange.jpg"));
        ingredients.add(new Ingredient("Bưởi", "quả", "Trái cây", "https://example.com/grapefruit.jpg"));
        ingredients.add(new Ingredient("Dâu tây", "quả", "Trái cây", "https://example.com/strawberry.jpg"));
        ingredients.add(new Ingredient("Kiwi", "quả", "Trái cây", "https://example.com/kiwi.jpg"));
        ingredients.add(new Ingredient("Xoài", "quả", "Trái cây", "https://example.com/mango.jpg"));

        // Insert data
        for (Ingredient ingredient : ingredients) {
            db.collection("ingredients")
                    .add(ingredient)
                    .addOnSuccessListener(documentReference -> {
                        Log.i("insert ingredient", ingredient.getName());
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi
                        Log.e("error", e.getMessage().toString());
                    });
        }
    }
}
