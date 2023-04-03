package co.empathy.academy.search.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private String movieId;
    private String type;
    private String primaryTitle;
    private String originalTitle;
    private List<Aka> akas;
    private boolean isAdult;
    private Integer startYear;
    private Integer endYear;
    private Integer runtimeMinutes;
    private String[] genres;
    private Rating rating;

    public Movie() {}

    public Movie(String movieId, String type, String primaryTitle, String originalTitle, boolean isAdult, Integer startYear,
                 Integer endYear, Integer runtimeMinutes, String[] genres) {
        this(movieId, type, primaryTitle, originalTitle, new ArrayList<Aka>(), isAdult, startYear, endYear, runtimeMinutes,
                genres, null);
    }

    public Movie(String movieId, String type, String primaryTitle, String originalTitle, List<Aka> akas,
                 boolean isAdult, Integer startYear, Integer endYear, Integer runtimeMinutes, String[] genres, Rating rating) {
        this.movieId = movieId;
        this.type = type;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.akas = akas;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres;
        this.rating = rating;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Aka> getAkas() {
        return akas;
    }

    public void setAkas(List<Aka> akas) {
        this.akas = akas;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void addAka(Aka aka) {
        if (this.akas == null) {
            throw new IllegalStateException();
        }
        this.akas.add(aka);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId='" + movieId + '\'' +
                ", type='" + type + '\'' +
                ", primaryTitle='" + primaryTitle + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", akas=" + akas +
                ", isAdult=" + isAdult +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", runtimeMinutes=" + runtimeMinutes +
                ", genres=" + Arrays.toString(genres) +
                ", rating=" + rating +
                '}';
    }
}
