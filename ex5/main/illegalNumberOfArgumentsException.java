package ex5.main;

public class illegalNumberOfArgumentsException{
    private static final int TWO = 2;
    private static final String WRONG_FILE_FORMAT = "Wrong number of arguments given. Please provide a single file path.";

    public illegalNumberOfArgumentsException() {}

    public void printMessage() {
        System.out.println(TWO);
        System.err.println(WRONG_FILE_FORMAT);
    }
}
