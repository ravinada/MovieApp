package com.ravinada.moviemania.Data;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ravinada.moviemania.R;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyTrailerViewHolder> {
    public List<TrailerData> resultsItems;
    private Context context;

    public TrailerAdapter(List<TrailerData> trailerData, Context contextt) {
        resultsItems = trailerData;
        context = contextt;
    }

    @NotNull
    @Override
    public TrailerAdapter.MyTrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyTrailerViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_list_content, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyTrailerViewHolder holder, int position) {
        Picasso.get().load(resultsItems.get(position).getImageURL())
                .placeholder(R.drawable.gradient).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (resultsItems == null) {
            return 0;
        }
        return resultsItems.size();
    }

    private static void watchYoutubeVideo(Context myContext, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(id));
        try {
            myContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            myContext.startActivity(webIntent);
        }
    }

    class MyTrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MyTrailerViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.trailer_image_view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String key = resultsItems.get(position).getKey();
                        watchYoutubeVideo(context, key);
                    }
                }
            });
        }
    }
}