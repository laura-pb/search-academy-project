package co.empathy.academy.search.exceptions;

public class NotAvailableDaily extends RuntimeException {

    public NotAvailableDaily(String message) {
        super(message);
    }

    public NotAvailableDaily() {
        this("There are not enough favorite movies to compute a daily film");
    }
}
