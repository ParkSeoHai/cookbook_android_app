package vn.nhom2eaut.bookchefk12.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.nhom2eaut.bookchefk12.R;

import vn.nhom2eaut.bookchefk12.controllers.UserController;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String user_id, username, email, name_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // sharedPreferences lấy lưu trữ thông tin người dùng đăng nhập
        sharedPreferences = getSharedPreferences("UserRefs", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);
        username = sharedPreferences.getString("username", null);
        email = sharedPreferences.getString("email", null);
        name_display = sharedPreferences.getString("name_display", null);

        // Set default fragment
        loadFragment(new HomeFragment(null, null));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Xử lý cho home
                fragment = new HomeFragment(null, null);
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_favourites) {
                // Xử lý cho Favourites
                if (user_id == null) {
                    showLoginDialog();
                    return false;
                } else {
                    fragment = new FavoriteFragment(user_id);
                    loadFragment(fragment);
                    return true;
                }
            } else if (id == R.id.nav_menu) {
                // Xử lý cho menu
                fragment = new CategoryFragment(getSupportFragmentManager());
                loadFragment(fragment);
                return true;
            } else if (id == R.id.nav_profile) {
                // Xử lý cho profile
                fragment = new ProfileFragment(user_id, sharedPreferences);
                loadFragment(fragment);
                return true;
            }
            return false;
        });

        // Click logo quay về trang chủ
        ImageView logoIcon = findViewById(R.id.logoIcon);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
            }
        });

        // Click icon search -> focus edit text search
        ImageView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {
            // Kiểm tra nếu fragment hiện tại không phải là HomeFragment thì mới load lại HomeFragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);

            // Nếu fragment hiện tại không phải là HomeFragment thì load HomeFragment
            if (!(currentFragment instanceof HomeFragment)) {
                loadHomeFragmentAndFocus();
            } else {
                // Nếu là HomeFragment rồi, chỉ cần focus vào EditText
                if (currentFragment != null) {
                    ((HomeFragment) currentFragment).focusEditSearch();
                }
            }
        });
    }

    private void showLoginDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Yêu cầu đăng nhập")
                .setMessage("Bạn cần đăng nhập để sử dụng tính năng này. Bạn có muốn chuyển đến trang đăng nhập không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Chuyển người dùng đến trang đăng nhập
                    startActivity(new Intent(MainActivity.this, loginActivity.class));
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }


    private void loadHomeFragmentAndFocus() {
        // Tạo một instance mới của HomeFragment
        HomeFragment homeFragment = new HomeFragment(null, null);

        // Load fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, homeFragment)
                .commit();

        // Thực hiện sau khi fragment được load
        getSupportFragmentManager().executePendingTransactions();

        // Gọi phương thức focus vào EditText
        if (homeFragment != null) {
            homeFragment.focusEditSearch();
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void searchRecipeByTitle(String title) {
        UserController controller = new UserController();
        controller.searchRecipeByTitle(title, new FirestoreCallback<List<RecipeDto>>() {
            @Override
            public void onSuccess(List<RecipeDto> recipeDtos) {
                for (RecipeDto recipeDto : recipeDtos) {
                    Log.i("searchRecipeByTitle: search recipe", recipeDto.toString());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("error", e.toString());
            }
        });
    }
}