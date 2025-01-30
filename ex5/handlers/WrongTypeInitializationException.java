package ex5.handlers;

public class WrongTypeInitializationException extends Exception{
    private static final String prefixMessage1 = "Error: variable: ";
    private static final String prefixMessage2 = " , should be initialized with type: ";
    private final String name;
    private final String type;
    private final String value;
    private static final String prefixMessage3 = " , instead got |";

    public WrongTypeInitializationException(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getMessage() {
        return prefixMessage1 + name + prefixMessage2 +
                type + prefixMessage3 + value + "|";
    }
}
