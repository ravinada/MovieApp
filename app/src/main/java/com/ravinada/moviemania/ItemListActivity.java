package com.ravinada.moviemania;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ravinada.moviemania.Data.Movie;
import com.ravinada.moviemania.details.EndlessScrollViewListener;
import com.ravinada.moviemania.details.ViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public final Context context = this;
    static ViewModel viewModel;
    private int pageNumberPopularMovies = 1;
    private int PageNumberTopRatedMovies = 1;
    public static final int POPULAR_INDEX = 0;
    public static final int TOP_RATED_INDEX = 1;
    public static final int FAVOURITE_INDEX = 2;
    public static int SORT_VALUE = POPULAR_INDEX;
    private SimpleItemRecyclerViewAdapter adapter;
    public RecyclerView recyclerView;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.init();
        getMovies();
    }

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable();
    }

    private void getTopRatedMovies(int page) {
        viewModel.getTopRatedMovies(page).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> topRatedMovies) {
                setupRecyclerView(topRatedMovies);
            }
        });
    }

    private void getPopularMovies(int page) {
        viewModel.getPopularMovies(page).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> popularMovies) {
                setupRecyclerView(popularMovies);
            }
        });
    }

    private void setupRecyclerView(List<Movie> movies) {
        if (adapter == null) {
            adapter = new SimpleItemRecyclerViewAdapter(this, movies, mTwoPane);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);
            EndlessScrollViewListener scrollListener = new EndlessScrollViewListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    switch (SORT_VALUE) {
                        case POPULAR_INDEX: {
                            getPopularMovies(pageNumberPopularMovies++);
                            break;
                        }
                        case TOP_RATED_INDEX: {
                            getTopRatedMovies(PageNumberTopRatedMovies++);
                            break;
                        }
                    }
                }
            };
            recyclerView.setOnScrollListener(scrollListener);
        } else {
            adapter.mValuesMovies = movies;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popular_id:
                        if (isOnline()) {
                            noInternetDialog();
                            return false;
                        }
                        if (SORT_VALUE == POPULAR_INDEX) {
                            return false;
                        }
                        SORT_VALUE = POPULAR_INDEX;
                        getMovies();
                        return true;
                    case R.id.top_rated_id:
                        if (isOnline()) {
                            noInternetDialog();
                            return false;
                        }
                        if (SORT_VALUE == TOP_RATED_INDEX) {
                            return false;
                        }
                        SORT_VALUE = TOP_RATED_INDEX;
                        getMovies();
                        return true;
                    case R.id.favorite_id:
                        if (SORT_VALUE == FAVOURITE_INDEX) {
                            return false;
                        }
                        SORT_VALUE = FAVOURITE_INDEX;
                        getMovies();
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_item);
        sortMenu.show();
        return false;
    }

    private void noInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("You are offline please check your internet connection");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getMovies() {
        if (SORT_VALUE != FAVOURITE_INDEX && isOnline()) {
            noInternetDialog();
            return;
        }
        switch (SORT_VALUE) {
            case POPULAR_INDEX: {
                pageNumberPopularMovies = 1;
                getPopularMovies(pageNumberPopularMovies++);
                break;
            }
            case TOP_RATED_INDEX: {
                PageNumberTopRatedMovies = 1;
                getTopRatedMovies(PageNumberTopRatedMovies++);
                break;
            }
            case FAVOURITE_INDEX: {
                getFavorites();
                break;
            }
        }
    }

    private void getFavorites() {
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                setupRecyclerView(movies);
            }
        });
    }

    public static class SimpleItemRecyclerViewAdapter extends
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final ItemListActivity mParentActivity;
        private List<Movie> mValuesMovies;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(Context parent, List<Movie> items, boolean twoPane) {
            mValuesMovies = items;
            mParentActivity = (ItemListActivity) parent;
            mTwoPane = twoPane;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            Picasso.get().load(IMAGE_BASE_URL + mValuesMovies.get(position).getPosterPath()).fit().into(holder.mPosterImageView);
            holder.itemView.setTag(mValuesMovies.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String movieId = String.valueOf(mValuesMovies.get(position).getId());
                    String posterPath = mValuesMovies.get(position).getPosterPath();
                    String releaseDate = mValuesMovies.get(position).getReleaseDate();
                    double votes = mValuesMovies.get(position).getVoteAverage();
                    String title = mValuesMovies.get(position).getTitle();
                    String overview = mValuesMovies.get(position).getOverview();
                    String backdropPath = mValuesMovies.get(position).getBackdropPath();

                    if (mTwoPane) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ItemDetailFragment.MOVIE_ID, movieId);
                        bundle.putString(ItemDetailFragment.TITLE, title);
                        bundle.putString(ItemDetailFragment.BACKDROP_PATH, backdropPath);
                        bundle.putString(ItemDetailFragment.RELEASE_DATE, releaseDate);
                        bundle.putString(ItemDetailFragment.POSTER_PATH, posterPath);
                        bundle.putString(ItemDetailFragment.OVERVIEW, overview);
                        bundle.putDouble(ItemDetailFragment.VOTES, votes);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(bundle);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.TITLE, title);
                        intent.putExtra(ItemDetailFragment.MOVIE_ID, movieId);
                        intent.putExtra(ItemDetailFragment.BACKDROP_PATH, backdropPath);
                        intent.putExtra(ItemDetailFragment.RELEASE_DATE, releaseDate);
                        intent.putExtra(ItemDetailFragment.POSTER_PATH, posterPath);
                        intent.putExtra(ItemDetailFragment.OVERVIEW, overview);
                        intent.putExtra(ItemDetailFragment.VOTES, votes);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mValuesMovies == null) {
                return 0;
            }
            return mValuesMovies.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView mPosterImageView;

            ViewHolder(View view) {
                super(view);
                mPosterImageView = view.findViewById(R.id.posterImageView);
            }
        }
    }
}