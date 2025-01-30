package ex5.handlers;

import ex5.checkers.ContentValidator;

import java.util.ArrayList;
import java.util.List;

public class VariablesHandler implements Handler {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final String COMMA = ",";
    private static final String SPACE = " ";
    private static final String FINAL = "final";
    private static final int TWO = 2;
    private static final String COMMA_PLUS = ",";
    private static final String SPACE_EQUALS = " =";
    private static final String EQUALS = "=";
    private static final String BLANK = "";
    private static final String END = ";";
    private static List<Var> globalVarList = new ArrayList<>();
    private static List<Func> functionsVariables = new ArrayList<>();

    public VariablesHandler() {
    }

    @Override
    public void add(String line) throws FinalNotInitializedException,
            GlobalVariableAlreadyExistsException, WrongTypeInitializationException, InitializationOfFinalException {

        String phrases_b = line.split(END)[ZERO];
        String[] phrases = phrases_b.split(COMMA);

        if (isLineOfDeclarations(line)) {
            String type = phrases[ZERO].split(SPACE)[ZERO];
            boolean isFinal = type.equals(FINAL);
            if(isFinal) {
                type = phrases[ZERO].split(SPACE)[ONE];
            }

            for (int i = ZERO ; i < phrases.length ; i++) {
                if (i > ZERO) {
                    tryAddVariable(type, isFinal, phrases[i]);
                } else {
                    String[] withoutPre = phrases[i].split(SPACE);
                    StringBuilder phrase = new StringBuilder(BLANK);
                    if (isFinal) {
                        for (int j = ZERO ; j < withoutPre.length ; j++) {
                            if (j >= TWO) {
                                phrase.append(withoutPre[j]);
                            }
                        }
                    } else {
                        for (int j = ZERO ; j < withoutPre.length ; j++) {
                            if (j >= ONE) {
                                phrase.append(withoutPre[j]);
                            }
                        }
                    }
                    tryAddVariable(type, isFinal, phrase.toString());
                }
            }
        } else {
            for (String phrase : phrases) {
                String name = phrase.split(SPACE_EQUALS)[ZERO];
                String value = phrase.split(SPACE_EQUALS)[ONE];
                tryUpdateVariable(name, value);
            }
        }
    }

    private void tryUpdateVariable(String name, String value) throws WrongTypeInitializationException, InitializationOfFinalException {

        for (Var var : globalVarList) {
            if (name.equals(var.getName())) {
                boolean byVar = false;
                for (Var temp : globalVarList) {
                    if (value.equals(temp.getName())) {
                        if (temp.isInitialized()) {
                            if (var.getType().equals(temp.getType())) {
                                var.initialize();
                                byVar = true;
                            }
                        }
                    }
                }
                if (!byVar)
                    var.tryToInitialize(value);
            }
        }
    }

    private void tryAddVariable(String type, boolean isFinal, String phrase)
            throws FinalNotInitializedException, GlobalVariableAlreadyExistsException,
            WrongTypeInitializationException {

        String[] words = phrase.split(EQUALS);
        Var temp;
        if (phrase.contains(EQUALS)) {
            String value = words[ONE].replaceAll(SPACE, BLANK);
            temp = new Var(words[ZERO].replaceAll(SPACE, BLANK), type, true, isFinal);
            boolean byVar = false;
            for (Var v : globalVarList) {
                if (value.equals(v.getName())) {
                    if (v.isInitialized()) {
                        if (v.getType().equals(temp.getType())) {
                            byVar = true;
                        }
                        else {
                            System.out.println(ONE);
                            throw new WrongTypeInitializationException(temp.getName(), type, value);
                        }
                    }
                }
            }
            if (!byVar && temp.typeNotSuit(value)) {
                System.out.println(ONE);
                throw new WrongTypeInitializationException(temp.getName(), type, value);
            }
        } else if (isFinal) {
            throw new FinalNotInitializedException();
        } else {
            temp = new Var(words[ZERO].replaceAll(SPACE, BLANK), type, false, false);
        }

        for (Var var : globalVarList) {
            if (var.getName().equals(temp.getName())) {
                throw new GlobalVariableAlreadyExistsException();
            }
        }
        globalVarList.add(temp);
    }

    @Override
    public boolean validateName(String line) {

        boolean valid = isLineOfDeclarations(line);
        return valid || isLineOfInitializations(line);
    }

    private boolean isLineOfDeclarations(String line) {
        return line.matches("^\\s*(?:final\\s+)?(char|int|double|String|boolean)\\s+" +
                "([a-zA-Z_](?!__)[\\w]*\\s*(?:=\\s*(?:[+-]?\\d+|\\d*\\.\\d+|\".*?\"|'.'|true" +
                "|false|[a-zA-Z_](?!__)[\\w]*))?\\s*(?:,\\s*[a-zA-Z_](?!__)[\\w]*\\s*(?:=\\s*" +
                "(?:[+-]?\\d+|\\d*\\.\\d+|\".*?\"|'.'|true|false|[a-zA-Z_](?!__)[\\w]*))?\\s*" +
                ")*)\\s*;\\s*$");
    }

    private boolean isLineOfInitializations(String line) {
        return line.matches(ContentValidator.INIT_CHAR_REGEX) ||
                line.matches(ContentValidator.INIT_DOUBLE_REGEX) ||
                line.matches(ContentValidator.INIT_STRING_REGEX) ||
                line.matches(ContentValidator.INIT_BOOLEAN_REGEX) ||
                line.matches(ContentValidator.INIT_INT_REGEX);
    }

    public void addNewFunction(String line, Func func) {

        String betweenBraces = line.split("[(]")[ONE];
        betweenBraces = betweenBraces.split("[)]")[ZERO];
        String[] params = betweenBraces.split(COMMA);

        if (!params[ZERO].equals(BLANK)) {
            for (String param : params) {
                String[] words = param.split(SPACE);
                boolean isFinal = words[ZERO].equals(FINAL);
                String type, name;
                if (isFinal) {
                    type = words[ONE];
                    name = words[TWO];
                } else {
                    type = words[ZERO];
                    name = words[ONE];
                }
                func.addVariable(new Var(name, type, true, isFinal), ZERO);
            }
        }
        functionsVariables.add(func);
    }

    public static List<Var> getGlobalVariables() {
        return globalVarList;
    }
}
