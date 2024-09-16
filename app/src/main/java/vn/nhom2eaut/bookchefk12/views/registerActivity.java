package vn.nhom2eaut.bookchefk12.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.controllers.UserController;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;
import com.google.android.material.textfield.TextInputLayout;

public class registerActivity extends AppCompatActivity {
    private Button btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnregister = findViewById(R.id.btnRegister);

        TextInputLayout lUsername = findViewById(R.id.layoutUsername);
        TextInputLayout lPass = findViewById(R.id.layoutPassword);
        TextInputLayout lRePass = findViewById(R.id.layoutRePass);

        EditText eUsername = findViewById(R.id.etUsername);
        EditText ePass = findViewById(R.id.etPassword);
        EditText eRePass = findViewById(R.id.etRePass);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vô hiệu hóa nút sau khi nhấn
                btnregister.setEnabled(false);

                String username = eUsername.getText().toString();
                String password = ePass.getText().toString();
                String repass = eRePass.getText().toString();

                if (username.isEmpty() && password.isEmpty() && repass.isEmpty()){
                    lUsername.setError("Không để trống mục này");
                    lPass.setError("Không để trống mục này");
                    lRePass.setError("Không để trống mục này");
                } else if (username.isEmpty()) {
                    lUsername.setError("Không để trống mục này");
                } else if (password.isEmpty()) {
                    lPass.setError("Không để trống mục này");
                } else if (repass.isEmpty()) {
                    lRePass.setError("Không để trống mục này");
                } else if (!password.equals(repass)){
                    lRePass.setError("Mật khẩu không khớp!");
                } else {
                    register(username, repass);
                }
            }
        });

        // Back to login
        ImageView btnBack = findViewById(R.id.iconBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    public void register(String username, String password) {
        UserController controller = new UserController();
        controller.register(username, password, new FirestoreCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Log.i("register", "Register success");
                Toast.makeText(registerActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                registerActivity.this.startActivity(intent);
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("register error", e.toString());
                Toast.makeText(registerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Vô hiệu hóa nút sau khi nhấn
                btnregister.setEnabled(true);
            }
        });
    }
}