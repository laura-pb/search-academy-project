package co.empathy.academy.search.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Principal {
    @JsonIgnore
    private String movieId;
    private Name name;
    private String[] characters;

    public Principal() {}

    public Principal(String movieId, Name name, String[] characters) {
        this.movieId = movieId;
        this.name = name;
        this.characters = characters;
    }

    public Principal(String movieId) {
        this(movieId, null, null);
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String[] getCharacters() {
        return characters;
    }

    public void setCharacters(String[] characters) {
        this.characters = characters;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
