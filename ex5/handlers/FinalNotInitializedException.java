package ex5.handlers;

public class FinalNotInitializedException extends Exception {
    private static final String PREFIX_MASSAGE = "Error: a final variable is not initialized";

    public FinalNotInitializedException() {
        super();
    }

    public void printMessage() {
        System.err.println(PREFIX_MASSAGE);
    }
}
