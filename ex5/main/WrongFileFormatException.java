package ex5.main;

public class WrongFileFormatException extends Exception{
    private static final String WRONG_FILE_FORMAT =
            "Error: wrong file format, the file type should have be 'sjava', instead - ";
    private final String message;

    public WrongFileFormatException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return WRONG_FILE_FORMAT + message;
    }
}
