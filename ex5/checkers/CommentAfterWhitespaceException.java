package ex5.checkers;

public class CommentAfterWhitespaceException extends Exception {

    private static final String Message = "Error: found a comment after whitespaces.";

    public CommentAfterWhitespaceException() {}

    public String getMessage() {
        return Message;
    }
}
