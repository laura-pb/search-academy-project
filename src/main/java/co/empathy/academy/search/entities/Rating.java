package co.empathy.academy.search.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Rating {
    @JsonIgnore
    private String movieId;
    private Float rating;
    private Integer numVotes;

    public Rating() {}

    public Rating(String movieId, Float rating, Integer numVotes) {
        this.movieId = movieId;
        this.rating = rating;
        this.numVotes = numVotes;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(Integer numVotes) {
        this.numVotes = numVotes;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "movieId='" + movieId + '\'' +
                ", rating=" + rating +
                ", numVotes=" + numVotes +
                '}';
    }
}
