package com.krishna.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Krishna on 06/03/16.
 */
class Movie implements Parcelable {
    private String id;
    private String overView;
    private String title;
    private String releaseDate;
    private String origTitle;
    private String popularity;
    private String voteAvg;
    private String posterPath;

    public Movie() {

    }

    public Movie(Parcel in) {
        id = in.readString();
        overView = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        origTitle = in.readString();
        popularity = in.readString();
        voteAvg = in.readString();
        posterPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(overView);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(origTitle);
        dest.writeString(popularity);
        dest.writeString(voteAvg);
        dest.writeString(posterPath);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOrigTitle() {
        return origTitle;
    }

    public void setOrigTitle(String origTitle) {
        this.origTitle = origTitle;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
