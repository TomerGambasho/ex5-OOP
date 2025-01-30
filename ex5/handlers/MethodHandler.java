package ex5.handlers;

import java.util.ArrayList;
import java.util.List;

public class MethodHandler implements Handler {

    private final static List<Func> functionsList = new ArrayList<>();
    private static final String SPACE_AND_BRACE = "[\\s(]+";
    private static final int ONE = 1;
    private static final String FUNCTION_ALREADY_EXISTS_PREFIX = "there is already a method of the name - ";
    private static final String VALID_METHOD_NAME_REGEX = "^\\s*void\\s+([a-zA-Z][a-zA-Z0-9_]*" +
            ")\\s*\\(((\\s*(final\\s+)?(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]" +
            "*\\s*)(,\\s*(final\\s+)?(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*)*)?" +
            "\\)\\s*\\{\\s*$";
    private static final int ZERO = 0;
    private static final String SPACE = " ";
    private static final String BLANK = "";
    private static final String COMMA = ",";
    private final VariablesHandler variablesHandler;

    public MethodHandler(VariablesHandler variablesHandler) {
        super();
        this.variablesHandler = variablesHandler;
    }

    @Override
    public void add(String line) throws MethodOrVariableNameException {

        String name = extractName(line);

        if (!functionsList.isEmpty()) {
            for (Func f : functionsList) {
                if (f.getName().equals(name))
                    throw new MethodOrVariableNameException(FUNCTION_ALREADY_EXISTS_PREFIX + name);
            }
        }

        Func temp = new Func(name);
        variablesHandler.addNewFunction(line, temp);
        functionsList.add(temp);
    }

    private String extractName(String line) {

        String[] wordsLine = line.split(SPACE_AND_BRACE);
        return wordsLine[ONE];
    }

    @Override
    public boolean validateName(String line) {
        return line.matches(VALID_METHOD_NAME_REGEX);
    }

    public List<Func> getMethods() {
        return functionsList;
    }

    public Func getMethodName(String line) {
        String name = extractName(line);
        for (Func func : functionsList) {
            if (func.getName().equals(name)) {
                return func;
            }
        }
        return null;
    }

    public void checkMethodCall(String line, Func current) throws BadFunctionCallException {
        String[] separated = line.split("[(]");
        String name = separated[ZERO].replaceAll(SPACE, BLANK);
        String params = separated[ONE].split("[)]")[ZERO];
        String[] paramsLst = params.split(COMMA);

        boolean called = false;
        for (Func func : functionsList) {
            if (func.getName().equals(name)) {
                called = true;
                boolean found = false;
                if (!(func.getVariables().get(ZERO).size() == paramsLst.length)) {
                    System.out.println(ONE);
                    throw new BadFunctionCallException();
                }
                for (int a = ZERO ; a < paramsLst.length ; a++) {

                    String p = paramsLst[a].replaceAll(SPACE, BLANK);
                    Var toPlease = func.getVariables().get(ZERO).get(a);

                    for (List<Var> level : current.getVariables().reversed()) {
                        for (Var var : level) {
                            if (var.getName().equals(p)) {
                                if ((var.isInitialized() && !var.getType().equals(toPlease.getType())) ||
                                        (!var.isInitialized() && var.getType().equals(toPlease.getType()))) {
                                    System.out.println(ONE);
                                    throw new BadFunctionCallException();
                                } else if (var.isInitialized() && var.getType().equals(toPlease.getType())) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (found)
                            break;
                    }
                    if (!found) {
                        for (Var var : VariablesHandler.getGlobalVariables()) {
                            if (var.getName().equals(p)) {
                                if ((var.isInitialized() && !var.getType().equals(toPlease.getType())) ||
                                        (!var.isInitialized() && var.getType().equals(toPlease.getType()))) {
                                    System.out.println(ONE);
                                    throw new BadFunctionCallException();
                                } else if (var.isInitialized() && var.getType().equals(toPlease.getType())) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!found) {
                        if (toPlease.typeNotSuit(p)) {
                            System.out.println(ONE);
                            throw new BadFunctionCallException();
                        } else {
                            found = true;
                        }
                    }
                }
            }
        }
        if (!called) {
            System.out.println(ONE);
            throw new BadFunctionCallException();
        }
    }
}
