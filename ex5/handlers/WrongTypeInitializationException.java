package ex5.handlers;

public class WrongTypeInitializationException {
    private static final int ONE = 1;
    private static final String prefixMessage = "Variable initialized with wrong type: ";

    public WrongTypeInitializationException() {}

    public void printMessage(String message) {
        System.out.println(ONE);
        System.err.println(prefixMessage + message);
    }
}
