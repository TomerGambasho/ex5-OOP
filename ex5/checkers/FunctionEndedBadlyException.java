package ex5.checkers;

public class FunctionEndedBadlyException extends Exception {
    private static final String Message = "Error: function ended badly.";

    public FunctionEndedBadlyException() {}

    public String getMessage() {
        return  Message;
    }
}
