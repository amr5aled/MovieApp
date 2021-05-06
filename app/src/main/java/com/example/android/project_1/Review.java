package com.example.android.project_1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amr5aled on 23/03/18.
 */

public class Review implements Parcelable{
    private String author,content,url,id;

    public Review(String author, String content, String url, String id) {
        this.author = author;
        this.content = content;
        this.url = url;
        this.id = id;
    }

    protected Review(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
        id = in.readString();
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(id);
    }
}
