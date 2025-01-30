package ex5.handlers;

public class InitializationWithoutDeclarationException extends Exception {

    private static final String MESSAGE = "Error: variable initialized without being declared";

    public InitializationWithoutDeclarationException() {
        super();
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
