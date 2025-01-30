package ex5.handlers;

public class MethodOrVariableNameException extends Exception{
    private static final String prefixMessage = "Error: ";

    public MethodOrVariableNameException(String massage) {
        super(massage);
    }

    public void printMessage() {
        System.err.println(prefixMessage + super.getMessage());
    }
}
