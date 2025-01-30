package ex5.checkers;

import ex5.handlers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ContentValidator {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final CharSequence OPENER_BRACE = "{";
    private static final CharSequence CLOSER_BRACE = "}";
    private static final String COMMENT_AFTER_WHITESPACE_REGEX = "^\\s*//.*$";
    private static final String NESTED_FUNCTION = "found nested function";
    private static final String LEGAL_LINE_REGEX = "^.*[\\{\\};]\\s*$";
    private static final String LEGAL_COMMENT_REGEX = "^\s*//.*";
    private static final String FUNCTION_START_REGEX = "^\\s*void\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*" +
            "\\(((\\s*(final\\s+)?" +
            "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)(,\\s*(final\\s+)?" +
            "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*)*)?\\)\\s*\\{\\s*$";
    private static final String RETURN_REGEX = "^\\s*return;\\s*$";
    private static final String CLOSER_BRACES_REGEX = "^\\s*}\\s*$";
    private static final String LOCAL_REGEX = "^\\s*(final\\s+)?(int|double|String|boolean|char)" +
            "\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=.*;$";
    private static final String METHOD_CALL_REGEX = "^\\s*([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(.*\\)\\s*;$";
    private static final String IF_WHILE_REGEX = "^\\s*(if|while)\\s*\\(.*\\)\\s*\\{\\s*$";
    private final String sourceFileName;
    public static final String VAR_REGEX = "(?:[a-zA-Z]|_[a-zA-Z0-9])\\w*";
    public static final String INIT_INT_REGEX = "^\\s*" + VAR_REGEX +
            "\\s*=\\s*[+-]?\\d+\\s*;\\s*$";
    public static final String INIT_STRING_REGEX = "^\\s*" + VAR_REGEX +
            "\\s*=\\s*\".*?\"\\s*;\\s*$";
    public static final String INIT_CHAR_REGEX = "^\\s*" + VAR_REGEX +
            "\\s*=\\s*'.'\\s*;\\s*$\n";
    public static final String INIT_DOUBLE_REGEX = "^\\s*" + VAR_REGEX +
            "\\s*=\\s*[+-]?\\d*\\.\\d+\\s*;\\s*$";
    public static final String INIT_BOOLEAN_REGEX = "^\\s*" + VAR_REGEX +
            "\\s*=\\s*(?:true|false|[+-]?\\d+|\\d*\\.\\d+)\\s*;\\s*$\n";
    private final VariablesHandler variablesHandler = new VariablesHandler();
    private final MethodHandler methodHandler;
    private List<String> cleanedLines;
    public ContentValidator(String sourceFileName) {

        this.sourceFileName = sourceFileName;
        this.methodHandler  = new MethodHandler(this.variablesHandler);
    }

    public boolean validate() throws IOException, CommentAfterWhitespaceException,
            MethodOrVariableNameException, CodeAtWrongPlaceException, InvalidLineException,
            FinalNotInitializedException, GlobalVariableAlreadyExistsException, MissingBracketException,
            WrongTypeInitializationException, FunctionEndedBadlyException,
            InitializationWithoutDeclarationException, InitializationOfFinalException, BadFunctionCallException {

        // Split file into lines, Remove empty & comment lines
        extractCodeLines();

        // Validate no comment lines with whitespace before //
        noCommentsAfterWhitespaces();

        System.out.println("looking for global vars");
        // get all names and basic validation check
        firstPass();

        System.out.println("the global vars and functions: ");
        System.out.println(VariablesHandler.getGlobalVariables());
        System.out.println(methodHandler.getMethods());
        // Second pass: check the rest of the code
        secondPass();
        return true;
    }

    private void extractCodeLines() throws IOException {
        // Split file into lines, Remove empty & comment lines
        this.cleanedLines = Files.readAllLines(Paths.get(sourceFileName)).stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.matches(LEGAL_COMMENT_REGEX))
                .collect(Collectors.toList());
    }

    private void noCommentsAfterWhitespaces() throws CommentAfterWhitespaceException {

        for (String line : cleanedLines) {
            
            // Check for comment lines that start with whitespace and then //
            if (line.matches(COMMENT_AFTER_WHITESPACE_REGEX)) {
                System.out.println(ONE);
                throw new CommentAfterWhitespaceException();
            }
        }
    }

    private void firstPass() throws MethodOrVariableNameException, CodeAtWrongPlaceException,
            InvalidLineException, MissingBracketException, FinalNotInitializedException,
            GlobalVariableAlreadyExistsException, WrongTypeInitializationException, InitializationOfFinalException {

        int currentScope = ZERO;

        // going over the code lines
        if (cleanedLines.size() > ZERO) {
            for (String line : cleanedLines) {
                // Adjust brace level
                if (line.contains(OPENER_BRACE)) {
                    currentScope++;
                    if (methodHandler.validateName(line)) {
                        if (currentScope == ONE) {
                            methodHandler.add(line);
                        } else {
                            System.out.println(ONE);
                            throw new CodeAtWrongPlaceException(NESTED_FUNCTION);
                        }
                    } else if(currentScope == ONE) {
                        System.out.println(ONE);
                        throw new InvalidLineException(line);
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
                        System.out.println(ONE);
                        throw new InvalidLineException(line);
                    }
                } else if (!line.matches(LEGAL_LINE_REGEX)) {
                    System.out.println(ONE);
                    throw new InvalidLineException(line);
                }
            }
        }

        if (currentScope != ZERO) {
            System.out.println(ONE);
            throw new MissingBracketException();
        }
    }

    private void secondPass() throws FunctionEndedBadlyException, InvalidLineException,
            FinalNotInitializedException, GlobalVariableAlreadyExistsException,
            WrongTypeInitializationException, InitializationWithoutDeclarationException, InitializationOfFinalException, BadFunctionCallException {

        int currentScope = ZERO;
        boolean functionClosedNow = false;
        Func current = null;
        if (cleanedLines.size() > ZERO) {
            for (String line : cleanedLines){

                if (line.matches(CLOSER_BRACES_REGEX) && !functionClosedNow) {
                    // function did not end after return;
                    System.out.println(ONE);
                    throw new FunctionEndedBadlyException();
                }
                // When inside a method
                if (line.matches(FUNCTION_START_REGEX)) {
                    // Update the current scope with the method name before the '(' meaning don't add the ( to the scope
                    currentScope++;
                    current = methodHandler.getMethodName(line);
                    current.up();

                } else if(currentScope > ZERO){
                    // Check for local variables
                    if (variablesHandler.validateName(line)) {
                        // Call a function to validate the local variable
                        current.addOrUpdateLocal(line, currentScope);
                    }
//                // Check for local variables without initialization
//                    else if (line.matches("^\\s*(final\\s+)?(int|double|String|boolean|char)" +
//                            "\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*;$")) {
//                        // Call a function to validate the local variable
//                        validateLocalVariableNoInitialisation(line, currentScope, globalVariablesType);
//                    }
                    // Check for variable assignment
//                else if (line.matches("^\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*=.*;$")) {
//                    // Call a function to validate the variable assignment
//                    validateVariableAssignment(line, currentScope, globalVariablesType);
//                }
//                // Check for method calls
                    else if (line.matches(METHOD_CALL_REGEX)) {
                        // Call a function to validate the method call
                        methodHandler.checkMethodCall(line, current);
                    }
//                // Check for if/while blocks
                    else if (line.matches(IF_WHILE_REGEX)) {
                        // Call a function to validate the if/while block
                        currentScope++;
                        // Update the current scope with if or while depending on the block
                    }
                    // Check for return statement
                    else if (line.matches(RETURN_REGEX)) {

                        functionClosedNow = true;
                    }
                    // Check for closing brace
                    else if (line.matches(CLOSER_BRACES_REGEX)) {
                        if (currentScope == ONE && !functionClosedNow) {
                            System.out.println(ONE);
                            throw new FunctionEndedBadlyException();
                        } else if (currentScope > ONE) {
                            current.down();
                        }
                        // Update the current scope: remove the last part
                        currentScope--;
                        functionClosedNow = false;
                    }
                    else {

                        System.out.println(ONE);
                        throw new InvalidLineException(line);
                    }
                }
            }
        }
    }
}
