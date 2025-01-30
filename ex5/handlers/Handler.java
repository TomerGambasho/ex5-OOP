package ex5.handlers;


public interface Handler {

    public boolean validateName(String line) throws MethodOrVariableNameException;
    public void add(String line) throws MethodOrVariableNameException, FinalNotInitializedException, GlobalVariableAlreadyExistsException, WrongTypeInitializationException, InitializationOfFinalException;
}
