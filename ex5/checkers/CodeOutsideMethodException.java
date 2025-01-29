package ex5.checkers;

public class CodeOutsideMethodException {
    private static final int ONE = 1;
    private static final String Message = "There was code outside of the methods, in the global scope.";

    public CodeOutsideMethodException() {}

    public void printMessage() {
        System.out.println(ONE);
        System.err.println(Message);
    }
}