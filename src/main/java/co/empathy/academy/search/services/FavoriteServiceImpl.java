package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.RecommendedMovie;
import co.empathy.academy.search.exceptions.NotAvailableDaily;
import co.empathy.academy.search.services.tmdb.TMDBRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final TMDBRequest tmdbRequest;
    @Autowired
    private SearchService searchService;

    public FavoriteServiceImpl(TMDBRequest tmdbRequest) {
        this.tmdbRequest = tmdbRequest;
    }

    public void addFavoriteMovie(String movieId, HttpSession session) throws IOException, InterruptedException {
        Set<Integer> favorites = (Set<Integer>) session.getAttribute(SESSION_FAVORITES);
        if (favorites == null) {
            favorites = new HashSet<>();
        }

        // Store movies as tmdb ids to reduce number of api calls when computing daily film
        Integer tmdbId = tmdbRequest.getTMDBMovieId(movieId);

        // To avoid adding recommended movies twice from the same movie
        if (!favorites.contains(tmdbId)) {
            favorites.add(tmdbId);
            // HashMap <recommendedMovie.id, recommendedMovie>
            HashMap<Integer, RecommendedMovie> sessionRecommendedMovies = (HashMap<Integer, RecommendedMovie>) session.getAttribute(SESSION_RECOMMENDED);
            if (sessionRecommendedMovies == null) {
                sessionRecommendedMovies = new HashMap<>();
            }

            List<Integer> sessionPastDailys = (List<Integer>) session.getAttribute(SESSION_PAST_DAILYS);
            JSONArray movieRecommendations = tmdbRequest.getMovieRecommendations(tmdbId);
            Iterator iterator = movieRecommendations.iterator();
            while (iterator.hasNext()) {
                JSONObject movie = (JSONObject) iterator.next();
                int id = movie.getInt("id");
                if(sessionRecommendedMovies.containsKey(id)) {
                    sessionRecommendedMovies.get(id).incrementOcurrences();
                } else if (sessionPastDailys.contains(id)){
                    // Movie already was a daily movie
                } else {
                    sessionRecommendedMovies.put(id, new RecommendedMovie(id, movie.getDouble("vote_average")));
                }
            }
            // Don't recommend an already watched movie
            if (sessionRecommendedMovies.containsKey(tmdbId)) {
                sessionRecommendedMovies.remove(tmdbId);
            }
            session.setAttribute(SESSION_RECOMMENDED, sessionRecommendedMovies);
        }

        session.setAttribute(SESSION_FAVORITES, favorites);
    }

    public AcademySearchResponse<Movie> getDaily(HttpSession session, String indexName) throws IOException, InterruptedException {
        LocalDate localDate = (LocalDate) session.getAttribute(SESSION_DATE);
        LocalDate today = LocalDate.now();

        AcademySearchResponse<Movie> dailyMovie;
        // a new daily film needs to be obtained
        if (localDate == null || !localDate.equals(today)) {
            Set<Integer> favoriteIds = (Set<Integer>) session.getAttribute(SESSION_FAVORITES);
            dailyMovie = computeDaily(session, indexName);
        } else {
            dailyMovie = (AcademySearchResponse<Movie>) session.getAttribute(SESSION_DAILY);
        }

        return dailyMovie;
    }

    private AcademySearchResponse<Movie> computeDaily(HttpSession session, String indexName) throws IOException, InterruptedException {
        Map<Integer, RecommendedMovie> sessionRecommendedMovies = (Map<Integer, RecommendedMovie>) session.getAttribute(SESSION_RECOMMENDED);
        if (sessionRecommendedMovies != null) {
            // daily is the one with more occurrences and then by max score
            Comparator<RecommendedMovie> compareByOcurrencesThenScore = Comparator
                    .comparing(RecommendedMovie::getOccurrences).reversed()
                    .thenComparing(RecommendedMovie::getScore).reversed();
            List<RecommendedMovie> sortedRecommendedMovies = new ArrayList<>(sessionRecommendedMovies.values())
                    .stream()
                    .sorted(compareByOcurrencesThenScore)
                    .collect(Collectors.toList());

            if (sessionRecommendedMovies.isEmpty()) {
                throw new NotAvailableDaily();
            }

            RecommendedMovie tmdbDailyMovie = sortedRecommendedMovies.get(0);
            String dailyTconst = tmdbRequest.getIMDBTconst(tmdbDailyMovie.getId());
            AcademySearchResponse<Movie> IMDbdailyMovie = searchService.getMovieByTconst(indexName, dailyTconst);

            session.setAttribute(SESSION_DATE, LocalDate.now());
            session.setAttribute(SESSION_DAILY, IMDbdailyMovie);

            List<Integer> sessionPastDailys = (List<Integer>) session.getAttribute(SESSION_PAST_DAILYS);
            if (sessionPastDailys == null) {
                sessionPastDailys = new ArrayList<>();
            }
            sessionPastDailys.add(tmdbDailyMovie.getId());
            session.setAttribute(SESSION_PAST_DAILYS, sessionPastDailys);

            // Remove movie from recommended so it is not returned as daily film again
            sessionRecommendedMovies.remove(tmdbDailyMovie.getId());

            return IMDbdailyMovie;
        }
        throw new NotAvailableDaily();
    }


    private final static String SESSION_FAVORITES = "session_favorites";
    private final static String SESSION_DATE = "session_date";
    private final static String SESSION_DAILY = "session_daily";
    private final static String SESSION_PAST_DAILYS = "session_past_dailys";
    private final static String SESSION_RECOMMENDED = "session_recommended";
}
