package ex5.handlers;

public class GlobalVariableAlreadyExistsException extends Exception {

    private static final String PREFIX_MASSAGE = "Error: two global variables with the same name";

    public GlobalVariableAlreadyExistsException() {
        super();
    }

    public void printMessage() {
        System.err.println(PREFIX_MASSAGE);
    }
}
