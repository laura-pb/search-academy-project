package co.empathy.academy.search.entities;

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
    private Float averageRating;
    private Integer numVotes;
    private List<Director> directors;
    private List<Principal> starring;

    public Movie() {}

    public Movie(String movieId, String type, String primaryTitle, String originalTitle, boolean isAdult, Integer startYear,
                 Integer endYear, Integer runtimeMinutes, String[] genres) {
        this(movieId, type, primaryTitle, originalTitle, null, isAdult, startYear, endYear, runtimeMinutes,
                genres, null,null, null, null);
    }

    public Movie(String movieId, String type, String primaryTitle, String originalTitle, List<Aka> akas,
                 boolean isAdult, Integer startYear, Integer endYear, Integer runtimeMinutes, String[] genres,
                 Integer numVotes, Float averageRating, List<Director> directors, List<Principal> starring) {
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
        this.numVotes = numVotes;
        this.averageRating = averageRating;
        this.directors = directors;
        this.starring = starring;
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

    public void addAka(Aka aka) {
        if (this.akas == null) {
            throw new IllegalStateException();
        }
        this.akas.add(aka);
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public List<Principal> getStarring() {
        return starring;
    }

    public void setStarring(List<Principal> starring) {
        this.starring = starring;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(Integer numVotes) {
        this.numVotes = numVotes;
    }
}
