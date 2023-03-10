package co.empathy.academy.search.exceptions;

public class InvalidJsonFileException extends RuntimeException {

    public InvalidJsonFileException(String message) {
        super(message);
    }

    public InvalidJsonFileException() {
        this("Provided file does not have adequate format, so it can not be parsed");
    }
}
