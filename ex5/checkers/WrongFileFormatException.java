package ex5.checkers;

public class WrongFileFormatException {
    private static final int TWO = 2;
    private static final String WRONG_FILE_FORMAT = "Wrong file format: the file type should have be 'sjava'.";

    public WrongFileFormatException() {}

    public void printMessage() {
        System.out.println(TWO);
        System.err.println(WRONG_FILE_FORMAT);
    }
}
