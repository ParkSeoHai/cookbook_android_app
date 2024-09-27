package vn.nhom2eaut.bookchefk12.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.controllers.UserController;
import vn.nhom2eaut.bookchefk12.repositories.dtos.UserDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private SharedPreferences sharedPreferences;

    private ImageView profileImage;
    private TextView userName;
    private TextView userEmail;
    private Button btnLogout;

    private String userId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment(String userId, SharedPreferences sharedPreferences) {
        // Required empty public constructor
        this.userId = userId;
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment(userId, sharedPreferences);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Kiểm tra userId, nếu null thì hiển thị dialog
        if (userId == null || userId.isEmpty()) {
            showLoginDialog();
            return null;
        }

        // Khởi tạo các view khác
        profileImage = view.findViewById(R.id.userProfileImage);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        btnLogout = view.findViewById(R.id.logoutButton);

        // Lấy dữ liệu hồ sơ người dùng
        getProfile(userId);

        profileImage.setOnClickListener(v -> openImagePicker());

        btnLogout.setOnClickListener(v -> handleLogout());

        return view;
    }


    private void showLoginDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Thông báo")
                .setMessage("Bạn chưa đăng nhập. Bạn có muốn chuyển về trang đăng nhập không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Chuyển về trang đăng nhập
                    startActivity(new Intent(getContext(), loginActivity.class));
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    // Đóng dialog, có thể để trống nếu không muốn xử lý gì
                    dialog.dismiss();
                })
                .setCancelable(false) // Không cho phép đóng dialog bằng cách nhấn ngoài
                .show();
    }


    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Upload to firebase store
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = storageReference.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveImageUrl(imageUrl);
            })).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveImageUrl(String url) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firestore.collection("users").document(userId);

        userRef.update("profile_picture", url)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Ảnh đại diện đã được cập nhật", Toast.LENGTH_SHORT).show();
                    // Hiển thị lại profile image
                    Glide.with(ProfileFragment.this)
                            .load(url)
                            .transform(new CropCircleTransformation())
                            .placeholder(R.drawable.image_placeholder)
                            .into(profileImage);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
                });
    }

    private void getProfile(String userId) {
        UserController controller = new UserController();
        controller.getProfile(userId, new FirestoreCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto result) {
                // Set data
                userName.setText(result.getUsername());
                userEmail.setText(result.getEmail());

                // Tải hình ảnh từ url
                Glide.with(ProfileFragment.this)
                        .load(result.getProfile_picture())
                        .transform(new CropCircleTransformation())
                        .placeholder(R.drawable.image_placeholder)
                        .into(profileImage);
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("Get profile error", e.toString());
                Toast.makeText(getContext(), "Lấy dữ liệu profile người dùng thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleLogout() {
        // Xóa thông tin lưu trữ trong SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        // Chuyển sang trang đăng nhập
        startActivity(new Intent(getContext(), MainActivity.class));
    }
}