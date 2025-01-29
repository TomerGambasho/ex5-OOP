package ex5.handlers;

import java.util.ArrayList;
import java.util.List;

public class MethodHandler implements Handler{

    private final static List<Func> functionsList = new ArrayList<>() ;
    private static final String SPACE_AND_BRACE = "[\\s(]+";
    private static final int ONE = 1;

    @Override
    public void add(String line) {

        String name = extractName(line);

        // TODO add parameters to functions variables
        for (Func f : functionsList) {
            if (f.getName().equals(name))
                // TODO ex
        }
        functionsList.add(new Func(name));
    }

    private String extractName(String line) {

        String[] wordsLine = line.split(SPACE_AND_BRACE);
        return wordsLine[ONE];
    }

    @Override
    public boolean validateName(String line) {
        return line.matches("^\\s*void\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(((\\s*(final\\s+)?" +
                "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)(,\\s*(final\\s+)?" +
                "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*)*)?\\)\\s*\\{\\s*$");
    }
}
