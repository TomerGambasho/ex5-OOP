package ex5.handlers;

import java.util.ArrayList;
import java.util.List;

public class Func extends Component {

    private static final int ZERO = 0;
    private static final String SPACE = " ";
    private static final String END = ";";
    private static final String COMMA = ",";
    private static final String FINAL = "final";
    private static final String SPACE_EQUALS = " =";
    private static final int ONE = 1;
    private static final String BLANK = "";
    private static final int TWO = 2;
    private static final String EQUALS = "=";
    private final List<List<Var>> variables = new ArrayList<>();
    public Func(String name) {
        super(name);
        variables.add(new ArrayList<>());
    }

    public void addVariable(Var var, int scope) {
        if (variables.size() == ZERO)
            variables.add(new ArrayList<>());
        variables.get(scope).add(var);
    }

    public List<List<Var>> getVariables() {
        return this.variables;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder(super.getName() + " -> \n");
        if (variables.size() > ZERO) {
            if ((variables.get(ZERO)).size() > ZERO) {
                for (Var var : variables.get(ZERO)) {
                    out.append((var.toString() + "\n"));
                }
            }
        }

        return out.toString();
    }

    public void addOrUpdateLocal(String line, int currentScope) throws FinalNotInitializedException,
            GlobalVariableAlreadyExistsException, WrongTypeInitializationException, InitializationWithoutDeclarationException, InitializationOfFinalException {
        String phrases_b = line.split(END)[ZERO];
        String[] phrases = phrases_b.split(COMMA);

        if (isLineOfDeclarations(line)) {
            handleDeclarations(currentScope, phrases);
        } else {
            if (phrases.length > ZERO) {
                for (String phrase : phrases) {
                    String name = phrase.split(SPACE_EQUALS)[ZERO];
                    String value = phrase.split(SPACE_EQUALS)[ONE].replaceAll(SPACE, BLANK);
                    tryUpdateVariable(name, value, currentScope);
                }
            }
        }
    }

    private void handleDeclarations(int currentScope, String[] phrases) throws FinalNotInitializedException, GlobalVariableAlreadyExistsException, WrongTypeInitializationException {
        String type = phrases[ZERO].split(SPACE)[ZERO];
        boolean isFinal = type.equals(FINAL);
        if(isFinal) {
            type = phrases[ZERO].split(SPACE)[ONE];
        }
        for (int i = ZERO; i < phrases.length ; i++) {
            if (i > ZERO) {
                tryAddVariable(type, isFinal, phrases[i], currentScope);
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
                tryAddVariable(type, isFinal, phrase.toString(), currentScope);
            }
        }
    }

    private void tryAddVariable(String type, boolean isFinal, String phrase, int currentScope)
            throws FinalNotInitializedException, GlobalVariableAlreadyExistsException,
            WrongTypeInitializationException {

        String[] words = phrase.split(EQUALS);
        Var temp;
        if (phrase.contains(EQUALS)) {
            String value = words[ONE].replaceAll(SPACE, BLANK);
            temp = new Var(words[ZERO].replaceAll(SPACE, BLANK), type, true, isFinal);
            boolean byVar = false;
            if (!variables.isEmpty()) {
                if (!variables.get(currentScope).isEmpty()) {
                    for (Var v : variables.get(currentScope)) {
                        if (value.equals(v.getName())) {
                            if (v.isInitialized()) {
                                if (v.getType().equals(temp.getType())) {
                                    byVar = true;
                                } else {
                                    System.out.println(ONE);
                                    System.out.println("a");
                                    throw new WrongTypeInitializationException(temp.getName(),
                                            type, value);
                                }
                            }
                        }
                    }
                }
            }
            if (!byVar) {
                if (!VariablesHandler.getGlobalVariables().isEmpty()) {
                    for (Var v : VariablesHandler.getGlobalVariables()) {
                        if (value.equals(v.getName())) {
                            System.out.println("d");
                            if (v.isInitialized()) {
                                System.out.println("e");
                                if (v.getType().equals(temp.getType())) {
                                    byVar = true;
                                } else {
                                    System.out.println(ONE);
                                    System.out.println("b");
                                    throw new WrongTypeInitializationException(temp.getName(),
                                            type, value);
                                }
                            }
                        }
                    }
                }
            }

            if (!byVar && temp.typeNotSuit(value)) {
                System.out.println(ONE);
                System.out.println("c");
                throw new WrongTypeInitializationException(temp.getName(), type, value);
            }
        } else if (isFinal) {
            throw new FinalNotInitializedException();
        } else {
            temp = new Var(words[ZERO].replaceAll(SPACE, BLANK), type, false, false);
        }
        if (!variables.isEmpty()) {
            if (!variables.get(currentScope).isEmpty()) {
                for (Var var : variables.get(currentScope)) {
                    if (var.getName().equals(temp.getName())) {
                        throw new GlobalVariableAlreadyExistsException();
                    }
                }
            }
        }

        variables.get(currentScope).add(temp);
    }

    private void tryUpdateVariable(String name, String value, int currentScope) throws InitializationWithoutDeclarationException, InitializationOfFinalException, WrongTypeInitializationException {

        boolean didIt = false;
        for (Var var : variables.get(currentScope)) {
            if (name.equals(var.getName())) {
                boolean byVar = false;
                if(var.isFinal()) {
                    System.out.println(ONE);
                    throw new InitializationOfFinalException();
                }
                byVar = isByVar(value, currentScope, var, byVar);
                if (!byVar)
                    var.tryToInitialize(value);
                didIt = true;
            }
        }
        if (!didIt) {
            for (Var var : VariablesHandler.getGlobalVariables()) {
                if (name.equals(var.getName())) {
                    boolean byVar = false;
                    byVar = isByVar(value, currentScope, var, byVar);
                    if (!byVar)
                        var.tryToInitialize(value);
                    didIt = true;
                }
            }
        }
        if (!didIt) {
            System.out.println(ONE);
            throw new InitializationWithoutDeclarationException();
        }
    }

    private boolean isByVar(String value, int currentScope, Var var, boolean byVar) {
        for (Var temp : variables.get(currentScope)) {
            if (value.equals(temp.getName())) {
                if (temp.isInitialized()) {
                    if (var.getType().equals(temp.getType())) {
                        var.initialize();
                        byVar = true;
                    }
                }
            }
        }
        if (!byVar) {
            for (Var temp : VariablesHandler.getGlobalVariables()) {
                if (value.equals(temp.getName())) {
                    if (temp.isInitialized()) {
                        if (var.getType().equals(temp.getType())) {
                            var.initialize();
                            byVar = true;
                        }
                    }
                }
            }
        }
        return byVar;
    }

    private boolean isLineOfDeclarations(String line) {
        return line.matches("^\\s*(?:final\\s+)?(char|int|double|String|boolean)\\s+" +
                "([a-zA-Z_](?!__)[\\w]*\\s*(?:=\\s*(?:[+-]?\\d+|\\d*\\.\\d+|\".*?\"|'.'|true" +
                "|false|[a-zA-Z_](?!__)[\\w]*))?\\s*(?:,\\s*[a-zA-Z_](?!__)[\\w]*\\s*(?:=\\s*" +
                "(?:[+-]?\\d+|\\d*\\.\\d+|\".*?\"|'.'|true|false|[a-zA-Z_](?!__)[\\w]*))?\\s*" +
                ")*)\\s*;\\s*$");
    }

    public void down() {
        System.out.println("removing, " + variables.size());
        variables.remove(variables.size() - ONE);
    }

    public void up() {
        System.out.println("adding, " + variables.size());
        variables.add(new ArrayList<>());
    }
}
