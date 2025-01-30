package ex5.handlers;

public class BadFunctionCallException extends Exception {

    private static final String MESSAGE = "Error: bad function call";

    public BadFunctionCallException() {
        super();
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
