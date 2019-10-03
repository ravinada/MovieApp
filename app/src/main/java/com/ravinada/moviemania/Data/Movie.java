package com.ravinada.moviemania.Data;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Movie implements Parcelable {

    @ColumnInfo(name = "video")
    @SerializedName("video")
    @Expose
    private boolean video;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @ColumnInfo(name = "id")
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    private String overview;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    private String releaseDate;


    public Movie(){

    }

    protected Movie(Parcel in) {
        video = in.readByte() != 0;
        posterPath = in.readString();
        id = in.readInt();
        backdropPath = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeString(posterPath);
        parcel.writeInt(id);
        parcel.writeString(backdropPath);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
