package ex5.checkers;

public class CodeAtWrongPlaceException extends Exception {
    public CodeAtWrongPlaceException(String nestedFunction) {
        super(nestedFunction);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
