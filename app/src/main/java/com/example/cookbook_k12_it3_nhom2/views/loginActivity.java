package com.example.cookbook_k12_it3_nhom2.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.controllers.UserController;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.UserDto;
import com.example.cookbook_k12_it3_nhom2.repositories.interfaces.FirestoreCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class loginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextInputLayout layoutUsername = findViewById(R.id.inputUsername);
        TextInputLayout layoutPassword = findViewById(R.id.inputPassword);
        TextInputEditText ietUsername = findViewById(R.id.etUsername);
        TextInputEditText ietPassword = findViewById(R.id.etPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ietUsername.getText().toString();
                String password = ietPassword.getText().toString();
                if (username.isEmpty() && password.isEmpty()){
                    layoutUsername.setError("Không để trống mục này");
                    layoutPassword.setError("Không để trống mục này");
                } else if (username.isEmpty()) {
                    layoutUsername.setError("Không để trống mục này");
                } else if (password.isEmpty()) {
                    layoutPassword.setError("Không để trống mục này");
                }
                else {
                    login(username, password);
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, registerActivity.class);
                loginActivity.this.startActivity(intent);
            }
        });
    }

    public void login(String username, String password) {
        UserController controller = new UserController();
        controller.login(username, password, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto userDto) {
                // Lưu thông tin người dùng khi đăng nhập thành công
                sharedPreferences = getSharedPreferences("UserRefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id", userDto.getUserId());
                editor.putString("username", userDto.getUsername());
                editor.putString("email", userDto.getEmail());
                editor.putString("name_display", userDto.getName_display());
                editor.putString("profile_picture", userDto.getProfile_picture());
                editor.apply();
                Log.i("Login", "Login success");
                Toast.makeText(loginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                loginActivity.this.startActivity(intent);
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(loginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                Log.i("login error", e.toString());
            }
        });
    }
}