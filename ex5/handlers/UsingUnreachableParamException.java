package ex5.handlers;

public class UsingUnreachableParamException {
    private static final int ONE = 1;
    private static final String prefixMessage = "Unreachable parameter: ";


    public UsingUnreachableParamException() {}

    public void printMessage(String message) {
        System.out.println(ONE);
        System.err.println(prefixMessage + message);
    }
}
