package ex5.main;

public class WrongNumberOfInputs extends Exception{
    private static final String WRONG_NUMBER_OF_INPUTS =
            "Error: wrong number of inputs, there should be exactly 1 input.";

    public WrongNumberOfInputs() {}

    @Override
    public String getMessage() {
        return WRONG_NUMBER_OF_INPUTS;
    }
}
