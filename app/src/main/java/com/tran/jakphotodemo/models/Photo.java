package com.tran.jakphotodemo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// model used to map the JSON keys to the object by GSON
public class Photo {

    @SerializedName("albumId")
    @Expose
    public Integer albumId;

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("thumbnailUrl")
    @Expose
    public String thumbnailUrl;


    public Integer getAlbumId() {
        return albumId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }


    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
