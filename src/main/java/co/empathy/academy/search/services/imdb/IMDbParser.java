package co.empathy.academy.search.services.imdb;

import co.empathy.academy.search.entities.Aka;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.Rating;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IMDbParser {

    public List<Movie> parseData(Optional<File> basics, Optional<File> akas, Optional<File> ratings) {
        List<Movie> movies = new ArrayList<>();

        if (basics.isPresent()) {
            parseBasicsFile(basics.get());
        }

        return movies;
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
