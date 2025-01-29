package ex5.handlers;

public class MethodOrVariableNameException {
    private static final int ONE = 1;
    private static final String prefixMessage = "Method or Variable error: ";

    public MethodOrVariableNameException() {}

    public void printMessage(String message) {
        System.out.println(ONE);
        System.err.println(prefixMessage + message);
    }
}
