package co.empathy.academy.search.services.imdb;

import co.empathy.academy.search.entities.Aka;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.Rating;

import java.io.*;
import java.util.*;

public class IMDbParser {

    private final File basics;
    private final File akas;
    private final File ratings;

    private int batchNumber = 0;

    private BufferedReader basicsScanner;
    private BufferedReader akasScanner;
    private BufferedReader ratingsScanner;

    private Aka prevAka = null;
    private Rating prevRating = null;

    public IMDbParser(File basics, File akas, File ratings) throws FileNotFoundException {
        this.basics = basics;
        this.basicsScanner = new BufferedReader(new FileReader(basics));
        this.akas = akas;
        this.akasScanner = new BufferedReader(new FileReader(akas));
        this.ratings = ratings;
        this.ratingsScanner = new BufferedReader(new FileReader(ratings));
    }

    public List<Movie> parseData(int batchSize) throws IOException {
        List<Movie> movies = new ArrayList<>();
        String movieData = null;

        int movieNumber = 0;

        if (batchNumber == 0) {
            initializeData();
        }

        while (movieNumber < batchSize && (movieData = basicsScanner.readLine()) != null) {
            Movie movie = parseMovie(movieData);

            //RATING
            while (prevRating != null && lowerMovieID(prevRating.getMovieId(), movie.getMovieId())) {
                prevRating = parseRating(ratingsScanner.readLine());
            }
            if (prevRating != null && prevRating.getMovieId().equals(movie.getMovieId())) {
                movie.setRating(prevRating);
                prevRating = parseRating(ratingsScanner.readLine());
            }

            //AKAS
            List<Aka> akas = new ArrayList<>();

            while (prevAka != null && lowerMovieID(prevAka.getMovieId(), movie.getMovieId())) {
                prevAka = parseAka(akasScanner.readLine());
            }
            while (prevAka != null && prevAka.getMovieId().equals(movie.getMovieId())) {
                akas.add(prevAka);
                prevAka = parseAka(akasScanner.readLine());
            }
            if (!akas.isEmpty()) {
                movie.setAkas(akas);
            }

            movies.add(movie);
            movieNumber++;
        }

        batchNumber++;

        if (movieData == null) {
            basicsScanner.close();
            akasScanner.close();
            ratingsScanner.close();
        }

        return  movies;
    }

    private void initializeData() throws IOException {
        //skip headers
        basicsScanner.readLine();
        akasScanner.readLine();
        ratingsScanner.readLine();

        //initialize data
        prevAka = parseAka(akasScanner.readLine());
        prevRating = parseRating(ratingsScanner.readLine());
    }

    private Movie parseMovie(String line) {
        if (line != null) {
            String[] fields = line.split("\t");
            return new Movie(fields[BASICS_ID], fields[BASICS_TYPE], fields[BASICS_PRIMARYTITLE],
                    fields[BASICS_ORIGINALTITLE], parseBoolean(fields[BASICS_ISADULT]),
                    parseInteger(fields[BASICS_STARTYEAR]), parseInteger(fields[BASICS_ENDYEAR]),
                    parseInteger(fields[BASICS_RUNTIMEMINUTES]), parseStringArray(fields[BASICS_GENRES]));
        } else {
            return null;
        }
    }

    private Rating parseRating(String line) {
        if (line != null) {
            String[] fields = line.split("\t");
            return new Rating(fields[RATINGS_ID], parseFloat(fields[RATINGS_AVERAGE]),
                    parseInteger(fields[RATINGS_NUMVOTES]));
        } else {
            return null;
        }
    }

    private Aka parseAka(String line) {
        if (line != null) {
            String fields[] = line.split("\t");
            return new Aka(fields[AKAS_ID], fields[AKAS_TITLE], fields[AKAS_REGION], fields[AKAS_LANGUAGE],
                    parseBoolean(fields[AKAS_ORIGINALTITLE]));
        } else {
            return null;
        }
    }

    private boolean lowerMovieID(String id1, String id2) {
        return id1.compareTo(id2) < 0;
    }

    private boolean parseBoolean(String value) {
        return value == "1";
    }

    private String[] parseStringArray(String stringArray) {
        String[] array = stringArray.split(",");
        return array;
    }

    private Integer parseInteger(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Float parseFloat(String number) {
        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private final static int BASICS_ID = 0;
    private final static int BASICS_TYPE = 1;
    private final static int BASICS_PRIMARYTITLE = 2;
    private final static int BASICS_ORIGINALTITLE = 3;
    private final static int BASICS_ISADULT = 4;
    private final static int BASICS_STARTYEAR = 5;
    private final static int BASICS_ENDYEAR = 6;
    private final static int BASICS_RUNTIMEMINUTES = 7;
    private final static int BASICS_GENRES= 8;

    private final static int RATINGS_ID = 0;
    private final static int RATINGS_AVERAGE = 1;
    private final static int RATINGS_NUMVOTES = 2;

    private final static int AKAS_ID = 0;
    private final static int AKAS_TITLE = 2;
    private final static int AKAS_REGION = 3;
    private final static int AKAS_LANGUAGE = 4;
    private final static int AKAS_ORIGINALTITLE = 7;
}
