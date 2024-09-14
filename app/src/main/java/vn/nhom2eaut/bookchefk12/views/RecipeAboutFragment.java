package vn.nhom2eaut.bookchefk12.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.controllers.RecipeController;
import vn.nhom2eaut.bookchefk12.repositories.dtos.CommentDto;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeAboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeAboutFragment extends Fragment {
    private String recipeId;
    private RecyclerView recyclerViewComment;
    private CommentAdapter commentAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeAboutFragment(String recipeId) {
        // Required empty public constructor
        this.recipeId = recipeId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeAboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public RecipeAboutFragment newInstance(String param1, String param2) {
        RecipeAboutFragment fragment = new RecipeAboutFragment(recipeId);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_about, container, false);
        showRecipeAbout(view, recipeId);
        return view;
    }

    public void showRecipeAbout(View view, String recipeId) {
        RecipeController controller = new RecipeController();
        controller.getDetail(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto recipe) {
                // Hiển thị dữ liệu
                ImageView recipeBg = (ImageView) view.findViewById(R.id.recipeImage);
                TextView title = (TextView) view.findViewById(R.id.recipeTitle);
                TextView starCount = (TextView) view.findViewById(R.id.starCount);
                TextView servingCount = (TextView) view.findViewById(R.id.servingCount);
                TextView prepTime = (TextView) view.findViewById(R.id.prepTime);
                TextView cookTime = (TextView) view.findViewById(R.id.cookTime);
                TextView recipeDescription = (TextView) view.findViewById(R.id.recipeDescription);

                // Sử dụng glide load ảnh từ url
                Glide.with(view.getContext())
                        .load(recipe.getImageUrl())
                        .placeholder(R.drawable.image_placeholder)  // Hình ảnh tạm trong khi chờ tải
                        .error(R.drawable.image_placeholder)        // Hình ảnh hiển thị khi có lỗi
                        .into(recipeBg);

                title.setText(recipe.getTitle());
                starCount.setText("Đánh giá: " + recipe.getAverage_rating() + " (" + recipe.getNumber_of_ratings() + ")");
                servingCount.setText("Phần ăn: " + recipe.getServings());
                prepTime.setText("Chuẩn bị: " + recipe.getTime().get("prep_time"));
                cookTime.setText("Nấu: " + recipe.getTime().get("cook_time"));
                recipeDescription.setText(recipe.getDescription());

                // Show comment
                recyclerViewComment = view.findViewById(R.id.recyclerViewComments);
                TextView recipeCommentEmpty = view.findViewById(R.id.recipeCommentEmpty);
                if (recipe.getCommentDtos().size() > 0) {
                    // If không có comment thì ẩn text empty
                    recipeCommentEmpty.setVisibility(View.GONE);
                    showComment(view, recipe.getCommentDtos());
                } else {
                    recyclerViewComment.setVisibility(View.GONE);
                    recipeCommentEmpty.setText("Công thức này chưa có đánh giá nào.");
                }

                Log.i("recipe detail", recipe.toString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("getRecipeDetail: error", e.toString());
                Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showComment(View view, List<CommentDto> commentDtos) {
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // Data for comments
        commentAdapter = new CommentAdapter(commentDtos);
        recyclerViewComment.setAdapter(commentAdapter);
    }
}