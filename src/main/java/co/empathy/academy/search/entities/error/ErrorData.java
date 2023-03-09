package co.empathy.academy.search.entities.error;

public class ErrorData {
    private String message;
    private String path;
    private String code;

    public ErrorData(String message, String path, String code) {
        this.message = message;
        this.path = path.substring(path.lastIndexOf("=") + 1);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
