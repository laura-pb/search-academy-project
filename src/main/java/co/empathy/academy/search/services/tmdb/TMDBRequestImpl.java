package co.empathy.academy.search.services.tmdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TMDBRequestImpl implements TMDBRequest {
    @Override
    public int getTMDBMovieId(String tconst) throws IOException, InterruptedException {
        URI uri = URI.create(String.format(TCONST_TO_TMDBID, tconst, API_KEY));
        JSONObject json = makeApiCall(uri);
        int tmdbId = json.getJSONArray(MOVIE_RESULTS).getJSONObject(0).getInt(ID_FIELD);

        return tmdbId;
    }

    @Override
    public JSONArray getMovieRecommendations(int movieId) throws IOException, InterruptedException {
        URI uri = URI.create(String.format(MOVIE_RECS, movieId, API_KEY));
        JSONObject json = makeApiCall(uri);
        JSONArray movies = json.getJSONArray(RECS_RESULTS);
        return movies;
    }

    @Override
    public String getIMDBTconst(Integer tmdbId) throws IOException, InterruptedException {
        URI uri = URI.create(String.format(TMDBID_TO_TCONST, tmdbId, API_KEY));
        JSONObject json = makeApiCall(uri);
        String tconst = json.getString(IMDB_ID_FIELD);

        return tconst;
    }

    private JSONObject makeApiCall(URI uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        return json;
    }

    private static final String  API_KEY = "7c22cf30e2a237deebd8ef2f79bbba7f";  // Ideally this wouldn't be here
    private static final String TCONST_TO_TMDBID = "https://api.themoviedb.org/3/find/%s?api_key=%s&external_source=imdb_id";
    private static final String TMDBID_TO_TCONST = "https://api.themoviedb.org/3/movie/%s?api_key=%s";
    private static final String ID_FIELD = "id";
    private static final String IMDB_ID_FIELD = "imdb_id";
    private static final String MOVIE_RESULTS = "movie_results";
    private static final String RECS_RESULTS = "results";
    private static final String MOVIE_RECS = "https://api.themoviedb.org/3/movie/%s/recommendations?api_key=%s";

}
