package com.ravinada.moviemania.Data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ravinada.moviemania.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyReviewsViewHolder> {
    public List<ReviewData> resultsItems = new ArrayList<>();
    public ReviewAdapter(Context myContext) {
    }

    @NotNull
    @Override
    public ReviewAdapter.MyReviewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyReviewsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_layout, viewGroup, false));
    }
    @Override
    public void onBindViewHolder(final ReviewAdapter.MyReviewsViewHolder viewHolder, int i) {
        String review = resultsItems.get(i).getContent();
        String author = resultsItems.get(i).getAuthor();
        int length = 300 > review.length() ? review.length() : 300;
        review = review.substring(0,length) + "\n\t\t\t\t\t--"+author+"\n\n";
        viewHolder.reviewText.setText(review);
    }
    @Override
    public int getItemCount() {
        if (resultsItems  == null){
            return 0;
        }
        return resultsItems.size();
    }
    class MyReviewsViewHolder extends  RecyclerView.ViewHolder {
        TextView reviewText;
        MyReviewsViewHolder(View view) {
            super(view);
            reviewText = view.findViewById(R.id.review_text_view);
        }
    }
}