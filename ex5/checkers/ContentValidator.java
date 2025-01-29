package ex5.checkers;

import ex5.handlers.MethodHandler;
import ex5.handlers.VariablesHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentValidator {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final CharSequence OPENER_BRACE = "{";
    private static final CharSequence CLOSER_BRACE = "}";
    private final String sourceFileName;
    private final VariablesHandler variablesHandler = new VariablesHandler();
    private final MethodHandler methodHandler = new MethodHandler();
    private List<String> cleanedLines;
    public ContentValidator(String sourceFileName) {

        this.sourceFileName = sourceFileName;
    }

    public boolean validate() throws IOException {

        // Split file into lines, Remove empty & comment lines
        extractCodeLines();

        // Validate no comment lines with whitespace before //
        noCommentsAfterWhitespaces();

        // get all names and basic validation check
        firstPass();

        // Second pass: check the rest of the code
        secondPass();
        return true;
    }

    private void secondPass() {

        int currentScope = 0;

        for (String line : cleanedLines){

            // When inside a method
            if (line.matches("^\\s*void\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(((\\s*(final\\s+)?" +
                    "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)(,\\s*(final\\s+)?" +
                    "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*)*)?\\)\\s*\\{\\s*$")) {
                // Update the current scope with the method name before the '(' meaning don't add the ( to the scope
                currentScope++;
                addMethodParamsToVariables(line, currentScope, globalVariablesType);
            }
            else if(currentScope > 0){
                // Check for local variables
                if (line.matches("^\\s*(final\\s+)?(int|double|String|boolean|char)" +
                        "\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=.*;$")) {
                    // Call a function to validate the local variable
                    validateLocalVariable(line, currentScope, globalVariablesType);
                }
                // Check for local variables without initialization
                else if (line.matches("^\\s*(final\\s+)?(int|double|String|boolean|char)" +
                        "\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*;$")) {
                    // Call a function to validate the local variable
                    validateLocalVariableNoInitialisation(line, currentScope, globalVariablesType);
                }
                // Check for variable assignment
                else if (line.matches("^\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*=.*;$")) {
                    // Call a function to validate the variable assignment
                    validateVariableAssignment(line, currentScope, globalVariablesType);
                }
                // Check for method calls
                else if (line.matches("^\\s*([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(.*\\)\\s*;$")) {
                    // Call a function to validate the method call
                    validateMethodCall(line, currentScope, methodNames, globalVariablesType);
                }
                // Check for if/while blocks
                else if (line.matches("^\\s*(if|while)\\s*\\(.*\\)\\s*\\{\\s*$")) {
                    // Call a function to validate the if/while block
                    validateIfWhileBlock(line, currentScope, globalVariablesType);
                    currentScope++;
                    // Update the current scope with if or while depending on the block
                }
                // Check for return statement
                else if (line.matches("^\\s*return;\\s*$")) {
                    // Call a function to validate the return statement
                    validateReturnStatement(line, currentScope, globalVariablesType);
                }
                // Check for closing brace
                else if (line.matches("^\\s*}\\s*$")) {
                    // Update the current scope: remove the last part
                    removeScopeParamsFromVariables(currentScope, globalVariablesType);
                    currentScope--;
                }
                else {
                    // Line is illegal
                    // TODO: print 1 and handle error
                    System.out.println(ONE);
                }
            }
        }
    }

    private void extractCodeLines() throws IOException {
        // Split file into lines, Remove empty & comment lines
        this.cleanedLines = Files.readAllLines(Paths.get(sourceFileName)).stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.matches("^\s*//.*"))
                .collect(Collectors.toList());
    }

    private void firstPass() {

        int currentScope = 0;

        // going over the global part
        for (String line : cleanedLines) {

            // Adjust brace level
            if (line.contains(OPENER_BRACE)) {

                currentScope++;
                if (methodHandler.validateName(line)) {

                    // Call a function to validate the method name
                    methodHandler.add(line);
                }
            } else if (line.contains(CLOSER_BRACE)) {
                currentScope--;
            }

            // Only process lines when outside any method (currentScope == 0)
            else if (currentScope == ZERO) {

                // Check for global variables declarations or initializations
                if (variablesHandler.validateName(line)) {

                    variablesHandler.add(line);
                }
                else {
                    // TODO ex invalid global line
                }
            }
        }

        if (currentScope != ZERO) {
            // TODO: print 1 and handle error, wrong number of braces
        }
    }

    private void noCommentsAfterWhitespaces() {

        for (String line : cleanedLines) {
            // Check for comment lines that start with whitespace and then //
            if (line.matches("^\\s*//.*$")) {
                // TODO: print 1 and handle error
                System.out.println(ONE);
            }
        }
    }

    private static void validateMethodName(String line) {
        /**
         * Validate a method name declaration line.
         * Add the method name to the method names map.
         * Validate that the method name is unique.
         * Validate that the parameters list is valid.
         * @param line The line to validate.
         * @param methodNames A map of method names.
         */

    }

    private static void validateGlobalVariable(String line, int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Validate a global variable declaration line.
         * Add the variable to the global variables map and assign it the right type.
         * Add the variable to the right type map.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }
}
