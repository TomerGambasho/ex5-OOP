package ex5.main;

public class InvalidFileNameException extends Exception{
    private static final String WRONG_FILE_FORMAT =
            "Error: The file is unreachable or not exist - ";
    private final String message;

    public InvalidFileNameException(String message) {
        this.message = message;
    }

    public String getMessage() {
       return WRONG_FILE_FORMAT + message;
    }
}
