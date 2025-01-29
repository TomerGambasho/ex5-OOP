package ex5.handlers;

public class SyntaxException {
    private static final int ONE = 1;
    private static final String prefixMessage = "Syntax error: ";

    public SyntaxException() {}

    public void printMessage(String message) {
        System.out.println(ONE);
        System.err.println(prefixMessage + message);
    }
}
