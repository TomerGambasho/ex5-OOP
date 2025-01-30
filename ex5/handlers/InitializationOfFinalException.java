package ex5.handlers;

public class InitializationOfFinalException extends Exception {
    private static final String MESSAGE = "Error: tried to re-assign a value to a constant";

    public InitializationOfFinalException() {
        super();
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
