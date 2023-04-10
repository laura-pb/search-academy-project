package co.empathy.academy.search.services.imdb;

import co.empathy.academy.search.entities.*;

import java.io.*;
import java.util.*;

public class IMDbParser {

    private int batchNumber = 0;

    private BufferedReader basicsReader;
    private BufferedReader akasReader;
    private BufferedReader ratingsReader;
    private BufferedReader crewReader;
    private BufferedReader principalsReader;

    private Aka prevAka = null;
    private Rating prevRating = null;
    private Principal prevPrincipal = null;
    private List<Director> prevDirectors = null;
    private String prevDirectorMovieId = null;

    public IMDbParser(File basics, File akas, File ratings, File crew, File principals) throws FileNotFoundException {
        this.basicsReader = new BufferedReader(new FileReader(basics));
        this.akasReader = new BufferedReader(new FileReader(akas));
        this.ratingsReader = new BufferedReader(new FileReader(ratings));
        this.crewReader = new BufferedReader(new FileReader(crew));
        this.principalsReader = new BufferedReader(new FileReader(principals));
    }

    public List<Movie> parseData(int batchSize) throws IOException {
        List<Movie> movies = new ArrayList<>();
        String movieData = null;

        int movieNumber = 0;

        if (batchNumber == 0) {
            initializeData();
        }

        while (movieNumber < batchSize && (movieData = basicsReader.readLine()) != null) {
            Movie movie = parseMovie(movieData);

            //RATING
            while (prevRating != null && lowerMovieID(prevRating.getMovieId(), movie.getMovieId())) {
                prevRating = parseRating(ratingsReader.readLine());
            }
            if (prevRating != null && prevRating.getMovieId().equals(movie.getMovieId())) {
                movie.setAverageRating(prevRating.getRating());
                movie.setNumVotes(prevRating.getNumVotes());
                prevRating = parseRating(ratingsReader.readLine());
            }

            //AKAS
            List<Aka> akas = new ArrayList<>();

            while (prevAka != null && lowerMovieID(prevAka.getMovieId(), movie.getMovieId())) {
                prevAka = parseAka(akasReader.readLine());
            }
            while (prevAka != null && prevAka.getMovieId().equals(movie.getMovieId())) {
                akas.add(prevAka);
                prevAka = parseAka(akasReader.readLine());
            }
            if (!akas.isEmpty()) {
                movie.setAkas(akas);
            }

            //DIRECTORS
            while (prevDirectors != null && lowerMovieID(prevDirectorMovieId, movie.getMovieId())) {
                prevDirectors = parseDirectors(crewReader.readLine());
            }
            if (prevDirectors != null && prevDirectorMovieId.equals(movie.getMovieId())) {
                movie.setDirectors(prevDirectors);
                prevDirectors = parseDirectors(crewReader.readLine());
            }

            //STARRING
            List<Principal> starring = new ArrayList<>();

            while (prevPrincipal != null && lowerMovieID(prevPrincipal.getMovieId(), movie.getMovieId())) {
                prevPrincipal = parsePrincipal(principalsReader.readLine());
            }
            while (prevPrincipal != null && prevPrincipal.getMovieId().equals(movie.getMovieId())) {
                starring.add(prevPrincipal);
                prevPrincipal = parsePrincipal(principalsReader.readLine());
            }
            if (!starring.isEmpty()) {
                movie.setStarring(starring);
            }

            movies.add(movie);
            movieNumber++;
        }

        batchNumber++;

        if (movieData == null) {
            closeReaders();
        }

        return  movies;
    }

    private void initializeData() throws IOException {
        //skip headers
        basicsReader.readLine();
        akasReader.readLine();
        ratingsReader.readLine();
        crewReader.readLine();
        principalsReader.readLine();

        //initialize data
        prevAka = parseAka(akasReader.readLine());
        prevRating = parseRating(ratingsReader.readLine());
        prevDirectors = parseDirectors(crewReader.readLine());
        prevPrincipal = parsePrincipal(principalsReader.readLine());
    }

    private void closeReaders() throws IOException {
        basicsReader.close();
        akasReader.close();
        ratingsReader.close();
        crewReader.close();
        principalsReader.close();
    }

    private List<Director> parseDirectors(String line) {
        if (line != null) {
            List<Director> result = new ArrayList<>();
            String[] fields = line.split("\t");
            prevDirectorMovieId = fields[CREW_ID];
            String[] directors = fields[CREW_DIRECTORS].split(",");
            for (String director : directors) {
                result.add(new Director(director));
            }
            return result;
        } else {
            prevDirectorMovieId = null;
            return null;
        }
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

    private Principal parsePrincipal(String line) {
        if (line != null) {
            String fields[] = line.split("\t");
            String category = fields[PRINCIPALS_CATEGORY];
            if (!category.equals(ACTOR_CATEGORY) && !category.equals(ACTRESS_CATEGORY) && !category.equals(SELF_CATEGORY)) {
                return new Principal("-1");
            } else {
                String[] characters = fields[PRINCIPALS_CHARACTERS].split(",");
                return new Principal(fields[PRINCIPALS_ID], new Name(fields[PRINCIPALS_NCONST]), characters);
            }

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
    private final static int CREW_ID = 0;
    private final static int CREW_DIRECTORS = 1;
    private final static int PRINCIPALS_ID = 0;
    private final static int PRINCIPALS_NCONST = 2;
    private final static int PRINCIPALS_CATEGORY = 3;
    private final static int PRINCIPALS_CHARACTERS = 5;
    private final static String ACTOR_CATEGORY = "actor";
    private final static String ACTRESS_CATEGORY = "actress";
    private final static String SELF_CATEGORY = "self";
}
