package com.ravinada.moviemania;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ravinada.moviemania.Data.Movie;

public class ItemDetailActivity extends AppCompatActivity {

    private static final String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    public Context context = this;
    String movieId;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        movieId = getIntent().getStringExtra(ItemDetailFragment.MOVIE_ID);

        assert movieId != null;
        ItemListActivity.viewModel.getMovieDetails(Integer.parseInt(movieId)).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movieData) {
                movie = movieData;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        updateFavorite();

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.MOVIE_ID,
                    getIntent().getStringExtra(ItemDetailFragment.MOVIE_ID));
            arguments.putString(ItemDetailFragment.POSTER_PATH,
                    getIntent().getStringExtra(ItemDetailFragment.POSTER_PATH));
            arguments.putString(ItemDetailFragment.BACKDROP_PATH,
                    getIntent().getStringExtra(ItemDetailFragment.BACKDROP_PATH));
            arguments.putString(ItemDetailFragment.TITLE,
                    getIntent().getStringExtra(ItemDetailFragment.TITLE));
            arguments.putString(ItemDetailFragment.OVERVIEW,
                    getIntent().getStringExtra(ItemDetailFragment.OVERVIEW));
            arguments.putString(ItemDetailFragment.RELEASE_DATE,
                    getIntent().getStringExtra(ItemDetailFragment.RELEASE_DATE));
            arguments.putBoolean(ItemDetailFragment.HAS_TRAILERS,
                    getIntent().getBooleanExtra(ItemDetailFragment.HAS_TRAILERS,false));
            arguments.putDouble(ItemDetailFragment.VOTES,
                    getIntent().getDoubleExtra(ItemDetailFragment.VOTES,0.0));

            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    private void updateFavorite() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movieStatus(Integer.parseInt(movieId))){
                    ItemListActivity.viewModel.delete(Integer.parseInt(movieId));
                    fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
                }else{
                    ItemListActivity.viewModel.insert(movie);
                    fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_fill));
                }
            }
        });
        if(movieStatus(Integer.parseInt(movieId))){
            fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_fill));
        }else{
            fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
        }
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    fab.hide();
                } else if (verticalOffset == 0) {
                    fab.show();
                } else {
                    fab.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Boolean movieStatus(int id){
        Movie movie = ItemListActivity.viewModel.getMovieThroughId(id);
        if(movie == null){
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.mShare) {
            Intent i = new Intent(
                    Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, YOUTUBE_VIDEO_URL);
            startActivity(Intent.createChooser(i, "Title of your share dialog"));
        }
        return super.onOptionsItemSelected(item);
    }
}
