package vn.nhom2eaut.bookchefk12.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import vn.nhom2eaut.bookchefk12.R;
import vn.nhom2eaut.bookchefk12.controllers.RecipeController;
import vn.nhom2eaut.bookchefk12.repositories.dtos.RecipeDto;
import vn.nhom2eaut.bookchefk12.repositories.interfaces.FirestoreCallback;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeInstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeInstructionFragment extends Fragment {
    private String recipeId;

    private RecyclerView recyclerView;
    private InstructionAdapter instructionAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeInstructionFragment(String recipeId) {
        // Required empty public constructor
        this.recipeId = recipeId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeInstructionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public RecipeInstructionFragment newInstance(String param1, String param2) {
        RecipeInstructionFragment fragment = new RecipeInstructionFragment(recipeId);
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
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);
        // Thiết lập WebView và hiển thị video
        WebView webView = view.findViewById(R.id.webview);
        String video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/V2KCAfHjySQ?si=BiQKoLpDrwljm1pd\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        // Thiết lập RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_instructions);
        instructionAdapter = new InstructionAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(instructionAdapter);

        showInstructions(view);
        return view;
    }

    public void showInstructions(View view) {
        RecipeController recipeController = new RecipeController();
        recipeController.getRecipeById(recipeId, new FirestoreCallback<RecipeDto>() {
            @Override
            public void onSuccess(RecipeDto result) {
                // Hiển thị danh sách instructions
                instructionAdapter.updateInstructions(result.getSteps());
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("Error showInstructions", e.toString());
            }
        });
    }
}