package com.ravinada.moviemania;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.ravinada.moviemania.Data.Movie;
import com.ravinada.moviemania.Data.ReviewData;
import com.ravinada.moviemania.Data.TrailerData;
import com.ravinada.moviemania.Data.ReviewAdapter;
import com.ravinada.moviemania.Data.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class ItemDetailFragment extends Fragment  {

    static final String MOVIE_ID = "movie_id";
    static final String POSTER_PATH = "poster_path";
    static final String BACKDROP_PATH = "backdrop_path";
    static final String TITLE = "title";
    static final String OVERVIEW = "overview";
    static final String RELEASE_DATE = "release_date";
    static final String HAS_TRAILERS = "has_trailers";
    static final String VOTES = "votes";
    private String movieId;
    private String movieTitle;
    private String movieReleaseDate;
    private String movieOverview;
    private String movieBackdropPath;
    private double movieVotes;
    private final ItemDetailFragment activity = this;
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private List<TrailerData> trailerDataAll;
    private List<ReviewData> reviewDataAll;
    private RecyclerView reviewsRecyclerView;
    private RecyclerView trailerRecyclerView;
    private CardView card_movie_videos;
    private CardView card_movie_reviews;
    Movie movie;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (getArguments().containsKey(MOVIE_ID)) {
            movieId = getArguments().getString(MOVIE_ID);
            movieOverview = getArguments().getString(OVERVIEW);
            movieTitle = getArguments().getString(TITLE);
            movieReleaseDate = getArguments().getString(RELEASE_DATE);
            movieBackdropPath = getArguments().getString(BACKDROP_PATH);
            movieVotes = getArguments().getDouble(VOTES);
        }
//        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(movieStatus(Integer.parseInt(movieId))){
//                    ItemListActivity.viewModel.delete(Integer.parseInt(movieId));
//                    fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_border));
//                }else{
//                    ItemListActivity.viewModel.insert(movie);
//                    fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_fill));
//                }
//            }
//        });
//
//        if(movieStatus(Integer.parseInt(movieId))){
//            fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_fill));
//        }else{
//            fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_border));
//        }

        ItemListActivity.viewModel.getTrailerData(Integer.parseInt(movieId)).observe(this, new Observer<List<TrailerData>>() {
            @Override
            public void onChanged(List<TrailerData> trailerData) {
                trailerDataAll = trailerData;
                setupTrailers();
            }
        });
        ItemListActivity.viewModel.getReviewData(Integer.parseInt(movieId)).observe(this, new Observer<List<ReviewData>>() {
            @Override
            public void onChanged(List<ReviewData> reviewData) {
                reviewDataAll = reviewData;
                setupReviews();
            }
        });
    }

    private void setupReviews() {
        ReviewAdapter reviewsAdapter = new ReviewAdapter(getActivity());
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reviewsAdapter.resultsItems = reviewDataAll;
        reviewsAdapter.notifyDataSetChanged();
    }

    private void setupTrailers() {
        TrailerAdapter trailerAdapter = new TrailerAdapter(trailerDataAll, getActivity());
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        trailerAdapter.resultsItems = trailerDataAll;
        trailerAdapter.notifyDataSetChanged();
    }

    private void setupViews() {
        CollapsingToolbarLayout appBarLayout = Objects.requireNonNull(activity.getActivity())
                .findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(movieTitle);
            Picasso.get().load(IMAGE_BASE_URL + movieBackdropPath).fit()
                    .into((ImageView) activity.getActivity().findViewById(R.id.movieDetailsBackdrop));
        }
//        if (!isOnline()) {
//            card_movie_videos.setVisibility(View.GONE);
//            card_movie_reviews.setVisibility(View.GONE);
//        }
    }
//    Boolean movieStatus(int id){
//        Movie movie = ItemListActivity.viewModel.getMovieThroughId(id);
//        return movie != null;
//    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupViews();
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        movieReleaseDate = movieReleaseDate.substring(0, 4);
        ((TextView) rootView.findViewById(R.id.item_movie_release_date)).setText(movieReleaseDate);
        ((TextView) rootView.findViewById(R.id.text_movie_title)).setText(movieTitle);
        ((RatingBar) rootView.findViewById(R.id.movie_rating)).setRating((float) movieVotes / 2);
        ((TextView) rootView.findViewById(R.id.text_movie_overview)).setText(movieOverview);
        card_movie_videos = rootView.findViewById(R.id.card_movie_videos);
        card_movie_reviews = rootView.findViewById(R.id.card_movie_reviews);
        trailerRecyclerView = rootView.findViewById(R.id.movie_videos);
        reviewsRecyclerView = rootView.findViewById(R.id.movie_reviews);
        return rootView;
    }
}
