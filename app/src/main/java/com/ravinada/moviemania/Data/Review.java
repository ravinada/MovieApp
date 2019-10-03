package com.ravinada.moviemania.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Review implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<ReviewData> reviewData = null;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("total_results")
    @Expose
    private int totalResults;

    @Override
    public int describeContents() {
        return 0;
    }

    protected Review(Parcel in) {
        id = in.readInt();
        page = in.readInt();
        totalPages = in.readInt();
        totalResults = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ReviewData> getReviewData() {
        return reviewData;
    }

    public void setReviewData(List<ReviewData> reviewData) {
        this.reviewData = reviewData;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(page);
        parcel.writeInt(totalPages);
        parcel.writeInt(totalResults);
    }
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

}

