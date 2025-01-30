package ex5.checkers;

public class InvalidLineException extends Exception {
    private static final String INVALID_LINE_ERROR = "Error: found an invalid line - |";
    private static final String LINE = "|";

    public InvalidLineException(String line) {
        super(line);
    }

    @Override
    public String getMessage() {
        return INVALID_LINE_ERROR + super.getMessage() + LINE;
    }
}
