package ex5.handlers;

public class Var extends Component{

    private static final String STRING_REGEX = "\".*\"";
    private static final String CHAR_REGEX = "'.'";
    private static final String INT_REGEX = "[+-]?\\d+";
    private static final String DOUBLE_REGEX = "[+-]?(\\d+\\.?\\d*|\\.\\d+)";
    private static final String BOOLEAN_REGEX =
            "true|false|[+-]?(\\\\d+\\\\.\\\\d*|\\\\.\\\\d+|\\\\d+)";
    private static final String SPACE = "| ";
    private static final int ONE = 1;
    private boolean init = false;
    private boolean isFinal;
    private String type;
    private static final String STRING = "String";
    private static final String CHAR = "char";
    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String BOOLEAN = "boolean";

    public Var(String name, String type, boolean init, boolean isFinal) {
        super(name);
        this.init = init;
        this.type = type;
        this.isFinal = isFinal;
    }

    public void tryToInitialize(String value) throws InitializationOfFinalException, WrongTypeInitializationException {
        if (isFinal) {
            System.out.println(ONE);
            throw new InitializationOfFinalException();
        } else if (typeIsIncompatible(value)) {
            System.out.println(ONE);
            throw new WrongTypeInitializationException(getName(), type, value);
        }
        initialize();
    }

    private boolean typeIsIncompatible(String value) {

        return switch (type) {
            case STRING -> !(value.matches(STRING_REGEX));
            case CHAR -> !(value.matches(CHAR_REGEX));
            case INT -> !(value.matches(INT_REGEX));
            case DOUBLE -> !(value.matches(DOUBLE_REGEX));
            case BOOLEAN -> !(value.matches(BOOLEAN_REGEX));
            default -> false;
        };
    }

    public boolean isInitialized() {
        return init;
    }

    public void initialize() {
        init = true;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "name: |" + super.getName() + SPACE
                + "type: |" + type + SPACE
                + "is initialized: |" + init + SPACE
                + "is final: |" + isFinal + SPACE;
    }

    public boolean typeNotSuit(String value) {
        return typeIsIncompatible(value);
    }

    public boolean isFinal() {
        return isFinal;
    }
}
