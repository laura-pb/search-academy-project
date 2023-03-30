package co.empathy.academy.search.services.imdb;

import co.empathy.academy.search.entities.Aka;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.Rating;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IMDbParser {

    private final File basics;
    private final File akas;
    private final File ratings;

    private int batchNumber = 0;

    private Scanner basicsScanner;
    private Scanner akasScanner;
    private Scanner ratingsScanner;

    private Aka prevAka = null;
    private Rating prevRating = null;

    public IMDbParser(File basics, File akas, File ratings) throws FileNotFoundException {
        this.basics = basics;
        this.basicsScanner = new Scanner(basics);
        this.akas = akas;
        this.akasScanner = new Scanner(akas);
        this.ratings = ratings;
        this.ratingsScanner = new Scanner(ratings);
    }

    public List<Movie> parseData(int batchSize) {
        List<Movie> movies = new ArrayList<>();
        int movieNumber = 0;

            if (batchNumber == 0) {
                //Skip first line (header)
                basicsScanner.nextLine();
                akasScanner.nextLine();
                ratingsScanner.nextLine();
            }

            while (basicsScanner.hasNextLine() && movieNumber <= batchSize) {
                String[] fields = basicsScanner.nextLine().split("\t");
                Movie movie = parseMovie(fields);

                //RATING
                if (prevRating != null) {
                    if (prevRating.getMovieId().equals(movie.getMovieId())) {
                        movie.setRating(prevRating);
                        prevRating = null;
                    }
                    // this movie doesn't have a rating assigned
                } else
                    if (ratingsScanner.hasNextLine()) {
                        String[] ratingFields = ratingsScanner.nextLine().split("\t");
                        Rating rating = parseRating(ratingFields);
                        if (rating.getMovieId().equals(movie.getMovieId())) {
                            movie.setRating(rating);
                        } else {
                            prevRating = rating;
                        }
                }

                //AKAS
                List<Aka> akas = new ArrayList<>();
                if (prevAka != null) {
                    if (prevAka.getMovieId().equals(movie.getMovieId())) {
                        akas.add(prevAka);
                        prevAka = null;
                    }
                }
                while (prevAka == null && akasScanner.hasNextLine()) {
                    String[] akasFields = akasScanner.nextLine().split("\t");
                    Aka aka = parseAka(akasFields);
                    if (aka.getMovieId().equals(movie.getMovieId())) {
                        akas.add(aka);
                    } else {
                        prevAka = aka;
                    }
                }
                if (!akas.isEmpty()) {
                    movie.setAkas(akas);
                }

                movies.add(movie);
            }

            batchNumber++;

            if (!basicsScanner.hasNextLine()) {
                basicsScanner.close();
                akasScanner.close();
                ratingsScanner.close();
            }

        return  movies;
    }

    public List<Movie> parseData(File basics, File akas, File ratings) {
        List<Movie> movies = new ArrayList<>();

        HashMap<String, Movie> movieDict = parseBasicsFile(basics);
        List<Aka> akasList = parseAkasFile(akas);
        List<Rating> ratingsList = parseRatingsFile(ratings);

        for (Aka aka : akasList) {
            movieDict.get(aka.getMovieId()).addAka(aka);
        }

        for (Rating rating : ratingsList) {
            movieDict.get(rating.getMovieId()).setRating(rating);
        }

        return new ArrayList<>(movieDict.values());
    }

    private HashMap<String, Movie> parseBasicsFile(File basics) {
        HashMap<String,Movie> movies = new HashMap<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(basics);
            //Skip first line (header)
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split("\t");
                movies.put(fields[BASICS_ID],
                        new Movie(fields[BASICS_ID], fields[BASICS_TYPE], fields[BASICS_PRIMARYTITLE],
                                fields[BASICS_ORIGINALTITLE], parseBoolean(fields[BASICS_ISADULT]),
                                parseInteger(fields[BASICS_STARTYEAR]), parseInteger(fields[BASICS_ENDYEAR]),
                                parseInteger(fields[BASICS_RUNTIMEMINUTES]), parseStringArray(fields[BASICS_GENRES])));

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
        return movies;
    }

    private Movie parseMovie(String[] fields) {
        return new Movie(fields[BASICS_ID], fields[BASICS_TYPE], fields[BASICS_PRIMARYTITLE],
                fields[BASICS_ORIGINALTITLE], parseBoolean(fields[BASICS_ISADULT]),
                parseInteger(fields[BASICS_STARTYEAR]), parseInteger(fields[BASICS_ENDYEAR]),
                parseInteger(fields[BASICS_RUNTIMEMINUTES]), parseStringArray(fields[BASICS_GENRES]));
    }

    private Rating parseRating(String[] fields) {
        return new Rating(fields[RATINGS_ID], parseFloat(fields[RATINGS_AVERAGE]),
                parseInteger(fields[RATINGS_NUMVOTES]));
    }

    private Aka parseAka(String[] fields) {
        return new Aka(fields[AKAS_ID], fields[AKAS_TITLE], fields[AKAS_REGION], fields[AKAS_LANGUAGE],
                parseBoolean(fields[AKAS_ORIGINALTITLE]));
    }

    private List<Rating> parseRatingsFile(File ratings) {
        List<Rating> ratingsList = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(ratings);
            //Skip first line (header)
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split("\t");
                ratingsList.add(new Rating(fields[RATINGS_ID], parseFloat(fields[RATINGS_AVERAGE]),
                        parseInteger(fields[RATINGS_NUMVOTES])));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }

        return ratingsList;
    }

    private List<Aka> parseAkasFile(File akas) {
        List<Aka> akasList = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(akas);
            //Skip first line (header)
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split("\t");
                akasList.add(new Aka(fields[AKAS_ID], fields[AKAS_TITLE], fields[AKAS_REGION], fields[AKAS_LANGUAGE],
                        parseBoolean(fields[AKAS_ORIGINALTITLE])));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }

        return akasList;
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
