package com.unisound.media.example.base;

public class MusiceBase {
    private String displayName;
    private String url;
    private String artist;
    @Override
    public String toString() {
        return "MusiceBase{" +
                "displayName='" + displayName + '\'' +
                ", url='" + url + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
