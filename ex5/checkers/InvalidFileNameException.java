package ex5.checkers;

public class InvalidFileNameException {
    private static final int TWO = 2;
    private static final String WRONG_FILE_FORMAT = "The file is unreachable or not exist";

    public InvalidFileNameException() {}

    public void printMessage() {
        System.out.println(TWO);
        System.err.println(WRONG_FILE_FORMAT);
    }
}
