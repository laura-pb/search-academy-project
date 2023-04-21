package co.empathy.academy.search.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecommendedMovie {
    private int id;
    private double score;
    private int occurrences;

    public RecommendedMovie() {}

    public RecommendedMovie(int id, double score) {
        this.id = id;
        this.score = score;
        this.occurrences = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendedMovie that = (RecommendedMovie) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public void incrementOcurrences() {
        this.occurrences = ++occurrences;
    }
}
