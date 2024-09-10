package com.example.cookbook_k12_it3_nhom2.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookbook_k12_it3_nhom2.R;
import com.example.cookbook_k12_it3_nhom2.models.Step;
import com.example.cookbook_k12_it3_nhom2.repositories.dtos.IngredientDto;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {
    private List<Step> steps;

    public InstructionAdapter(List<Step> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instruction, parent, false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void updateInstructions(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    static class InstructionViewHolder extends RecyclerView.ViewHolder {
        private TextView stepNumber, stepContent;
        private ImageView stepImage;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            stepContent = itemView.findViewById(R.id.stepContent);
            stepImage = itemView.findViewById(R.id.stepImage);
        }

        public void bind(Step step) {
            stepNumber.setText(step.getStep_number() + ".");
            stepContent.setText((step.getInstruction()));
            if (step.getImageUrl() != null) {
                // Sử dụng glide load ảnh từ url
                Glide.with(itemView)
                        .load(step.getImageUrl())
                        .placeholder(R.drawable.image_placeholder)  // Hình ảnh tạm trong khi chờ tải
                        .error(R.drawable.image_placeholder)        // Hình ảnh hiển thị khi có lỗi
                        .into(stepImage);
            } else {
                stepImage.setVisibility(View.GONE);
            }
        }
    }
}
