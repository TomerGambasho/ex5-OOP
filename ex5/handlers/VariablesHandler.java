package ex5.handlers;

import java.util.List;

public class VariablesHandler implements Handler {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static List<Var> globalVarList;

    public VariablesHandler() {
    }

    @Override
    public void add(String line) {

        String[] phrases = line.split(",;");
        String type = phrases[ZERO].split(" ")[ZERO];

        boolean isFinal = type.equals("final");
        if(isFinal) {
            type = phrases[ZERO].split(" ")[ONE];
        }

        for (String phrase : phrases) {
            tryAddVariable(type, isFinal, phrase);
        }
    }

    private void tryAddVariable(String type, boolean isFinal, String phrase) {

        String[] words = phrase.split(" =");
        Var temp = null;
        if (phrase.contains("=")) {
            temp = new Var(words[ZERO], type, true, isFinal);
        } else if (isFinal) {
            // TODO ex
        } else {
            temp = new Var(words[ZERO], type, false, false);
        }
        for (Var var : globalVarList) {
            assert temp != null;
            if (var.getName().equals(temp.getName())) {
                // TODO ex
            }
        }
        globalVarList.add(temp);
    }

    @Override
    public boolean validateName(String line) {

        boolean valid = line.matches("^\\s*(?:final\\s+)?(char|int|double|String|boolean)\\s+" +
                "([a-zA-Z_](?!__)[\\w]*\\s*(?:=\\s*(?:[+-]?\\d+|\\d*\\.\\d+|\".*?\"|'.'|true" +
                "|false|[a-zA-Z_](?!__)[\\w]*))?\\s*(?:,\\s*[a-zA-Z_](?!__)[\\w]*\\s*(?:=\\s*" +
                "(?:[+-]?\\d+|\\d*\\.\\d+|\".*?\"|'.'|true|false|[a-zA-Z_](?!__)[\\w]*))?\\s*" +
                ")*)\\s*;\\s*$");

        return valid || line.matches("^\\s*([a-zA-Z_](?!__)[\\w]*)\\s*=\\s*(?:[+-]?\\d+|" +
                "\\d*\\.\\d+|\".*?\"|'.'|true|false|[a-zA-Z_](?!__)[\\w]*)\\s*(?:,\\s*[a-zA-Z_]" +
                "(?!__)[\\w]*\\s*=\\s*(?:[+-]?\\d+|\\d*\\.\\d+|\".*?\"|'.'|true|false|[a-zA-Z_]" +
                "(?!__)[\\w]*))*\\s*;\\s*$\n");
    }
}
