package co.empathy.academy.search.services.tmdb;

import org.json.JSONArray;

import java.io.IOException;

public interface TMDBRequest {
    int getTMDBMovieId(String tconst) throws IOException, InterruptedException;
    JSONArray getMovieRecommendations(int movieId) throws IOException, InterruptedException;
    String getIMDBTconst(Integer tmdbId) throws IOException, InterruptedException;
}
