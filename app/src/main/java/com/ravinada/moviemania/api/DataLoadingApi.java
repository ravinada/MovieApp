package com.ravinada.moviemania.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataLoadingApi {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
