package ex5.checkers;

public class MissingBracketException extends Exception{
    private static final String Message = "Error: missing a bracket in the code.";

    public MissingBracketException() {}

    public void printMessage() {
        System.err.println(Message);
    }
}