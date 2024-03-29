package co.empathy.academy.search.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Aka {
    @JsonIgnore
    private String movieId;
    private String title;
    private String language;
    private String region;
    private boolean originalTitle;

    public Aka() {}

    public Aka(String movieId, String title, String region, String language, boolean originalTitle) {
        this.movieId = movieId;
        this.title = title;
        this.region = region;
        this.language = language;
        this.originalTitle = originalTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(boolean originalTitle) {
        this.originalTitle = originalTitle;
    }

    @Override
    public String toString() {
        return "Aka{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", region='" + region + '\'' +
                ", originalTitle=" + originalTitle +
                '}';
    }
}
