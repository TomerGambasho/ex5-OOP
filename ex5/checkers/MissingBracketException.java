package ex5.checkers;

public class MissingBracketException {
    private static final int ONE = 1;
    private static final String Message = "Missing a bracket in the code.";

    public MissingBracketException() {}

    public void printMessage() {
        System.out.println(ONE);
        System.err.println(Message);
    }
}