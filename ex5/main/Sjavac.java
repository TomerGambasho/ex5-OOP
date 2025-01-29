package ex5.main;
import java.io.File;
//import java.util.regexp.Matcher;
//import java.util.regexp.Pattern;
import java.util.stream.Collectors;
//import java.util.function;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sjavac {
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
//    3 Options for Lines:
//    - Some kind of comment, has to start with '//' with no whitespace before and is to be ignored.
//    - An empty line. Containing only white spaces.
//    - A code line, ending with '{', ';' or '}', they can all have whitespace before and afterwards, and the
//           '}' has to be in its own line.

//    * Not supporting arrays, other comments, operators.

//    Rules for variables:
//    - There are 'global' and 'local' variables only. Being created like this: "type name = value;"
//    - Legal name examples: ’g2’, ’b_3’, ’_a’,’_0’, ’a_’.
//    - Illegal name examples: ’2g’, ’_’, ’2__’, ’54_a’, ’3_3__3_b’, ’__’,’___b’..
//    - Naming variables with reserved words (e.g int, if etc) is for out choosing on how to handle.
//    - The following four characters will not appear in strings: ' " \ ,
//    - We can do X = Y only if they have the same type. But boolean can be assigned an int or double
//                      and double can be assigned an int.
//    - A value can be declared with or without initialisation: int a = 1; / int b;
//    - The valid types are:
//      * int: int num1 = 5; / int num2 = -5; / int num3 = +5;
//      * double: double b = 5.21; / double c = 2.; / double e = .1; / double k = -.3; / double p = +.4
//      * String: String s = "a4t*#@$v";
//      * boolean: boolean a = true; / boolean b = false; / boolean c = 5.2;
//      * char: char c = '@';
//    - More then one variable with the same type can be declared and initialized in the same line:
//    - int i1, i2 = 6; / char c='Z', f; / boolean a, b ,c , d = true, e, f = 5;  / String a = "hello" , b = "goodbye";
//    - Also we can just assign two variables in the same line: a = 1, b = "hi";
//    - There can not be two variables with the same name, Unless one is global and the other is local. Even if the global
//          variable is being declared inside the function of the local variable before its declaration.
//    - Methods parameters are considered local variables of the method they belong to.
//    - Two local variable with the same name can be defined inside different blocks, even if one is nested in the other.
//    - A variable (local or global) may have the same name as a method.
//    - Final variables: final int a = 5; Can not be assigned values afterwards.
//    - Using the final modifier on a line with more than one initialisation makes all the variables final.
//    - When checking for initialisation, it may appear in an insider scope when talking about locals.
//    - A local variable can not be used before assignment of value.
//    - This is legal: int a = 5, b;
//                     b = 7, a=b;

//    Rules for methods:
//    - Creation line for method: void method name ( type1 parameter1, type2 parameter2, . . . , typen parametern) {
//    - Method's name is like a variable name that has to start with a letter.
//    - only void methods supported.
//    - Inside a method there are 5 types of lines:
//      * Local variable declaration.
//      * Variable assignment.
//      * A call to another method: method name (param1, param2, . . . , paramn);
//      * An if/ while block.
//      * A return statement (Can appear in a couple of places but has to be in the last line too.): return;
//    - Method can be called only from another method and not from global scope.
//    - Calling a method with an incompatible number of arguments, wrong types or uninitialized variables is illegal.
//    - We can define params of the function as final, and then we can call the function with a final or non-final
//              variable, but it can not be changed during the function: void foo(final int a) {
//    - We can change the value of a non-final parameter inside a function.
//    - Methods must end with '}' that comes in a separate line after the return.
//    - Methods can call themselves, and we don't have to do anything with that.
//    - Methods can't have the same name.
//    - There is no order to the global scope.
//    - A method can not be declared inside another method.

//    If and While blocks explanation:
//    - if (condition) {
//      ...
//      }
//    - while (condition) {
//      ...
//      }

//    A condition definition:
//    - It can be one of the following:
//      * The reserved words 'true' and 'false'.
//      * An initialized boolean / int / double.
//      * A double / int constant.
//      * A sequence of conditions separated with Or ('||') or And ('&&'). We don't need to consider brackets.

//    More comments:
//    - Each line should appear in one line only.
//    - No two statements in the same line.
//    - White spaces can be anywhere but inside variable names, values and reserve words, and also before comments.
//    - White spaces have to appear between return type and name, variable type and name, and final modifier to type.
//    - All reserved keywords are:
//      * Types: int, double, boolean, char, String.
//      * Other: void, final, if, while, true, false, return.
//    -  The output of your program is a single digit, outputted using System.out.println():
//      * 0 – if the code is legal.
//      * 1 – if the code is illegal.
//      * 2 – in case of IO errors (illegal number of arguments for the program, wrong file format or invalid filename).
//    - We should create class for Exceptions and call them "XXXException". They should be put inside the package of
//            the class that throws it. Not in a nested class.
//    - We need to use a lot in regular expressions. We can also use String class methods such
//          as substring(), charAt(), etc. But if we can, try to use the regular expressions.


//    Side note: We should go through the code twice, first without going in the methods and only looking at global
//        variables and method's names, and in the other one we will go through everything.

    public static void main(String[] args) {
        // Check if there is exactly one argument (the source file name)
        if (args.length != 1) {
            // Maybe replace with Error class.
            System.err.println("Error: Illegal number of arguments. Expected exactly one source file.");
            System.out.println(TWO);
            return;
        }

        String sourceFileName = args[0];

        try {
            // Validate file name and format
            File sourceFile = new File(sourceFileName);
            if (!sourceFile.exists() || !sourceFile.isFile()) {
                // Maybe replace with Error class.
                System.err.println("Error: File not found or invalid filename.");
                System.out.println(TWO);
                return;
            }

            // Check if the file has the correct format (e.g., ends with ".java")
            if (!sourceFileName.endsWith(".sjava")) {
                // Maybe replace with Error class.
                System.err.println("Error: Wrong file format. Expected a .java file.");
                System.out.println(TWO);
                return;
            }

            // Validate the source file's content
            boolean isLegal = validateFile(sourceFileName);

            if (isLegal) {
                System.out.println(ZERO);
            } else {
                // Maybe replace with Error class.
                System.err.println("Error: Code is illegal.");
                System.out.println(ONE);
            }
        } catch (Exception e) {
            // Maybe replace with Error class.
            throw new RuntimeException(e);
        }

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

    private static void validateGlobalVariableNoInitialisation(String line, int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Validate a global variable declaration line without initialisation.
         * Add the variable to the global variables map and assign it the right type.
         * Add the variable to the right type map.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static void validateMethodName(String line, Map<String, String> methodNames) {
        /**
         * Validate a method name declaration line.
         * Add the method name to the method names map.
         * Validate that the method name is unique.
         * Validate that the parameters list is valid.
         * @param line The line to validate.
         * @param methodNames A map of method names.
         */
    }

    private static void addMethodParamsToVariables(String line, int currentScope,
                                                   Map<String, String> variablesType) {
        /**
         * Add the method parameters to the variables map.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param variablesType A map of global variables and their types.
         */
    }

    private static void validateReturnStatement(String line, int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Validate a return statement line.
         * Check that the return statement is the last line in the method.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static void validateIfWhileBlock(String line, int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Validate an if/while block line.
         * Check that the condition is valid.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static void validateVariableAssignment(String line, int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Validate a variable assignment line.
         * Check that the variable is declared and has the right type.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static void validateMethodCall(String line, int currentScope, Map<String, String> methodNames, Map<String, String> globalVariablesType) {
        /**
         * Validate a method call line.
         * Check that the method is declared and has the right parameters.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param methodNames A map of method names.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static void validateLocalVariableNoInitialisation(String line, int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Validate a local variable declaration line without initialisation.
         * Add the variable to the local variables map and assign it the right type.
         * Add the variable to the right type map.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static void validateLocalVariable(String line, int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Validate a local variable declaration line.
         * Add the variable to the local variables map and assign it the right type.
         * Add the variable to the right type map.
         * @param line The line to validate.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static void removeScopeParamsFromVariables(int currentScope, Map<String, String> globalVariablesType) {
        /**
         * Remove the variables from the variables map that are in the current scope.
         * @param currentScope The current scope.
         * @param globalVariablesType A map of global variables and their types.
         */
    }

    private static boolean validateFile(String path) throws IOException {
        // Split file into lines
        List<String> cleanedLines = Files.readAllLines(Paths.get(path)).stream()
                .map(String::trim) // Trim whitespace
                .filter(line -> !line.isEmpty() && !line.matches("^//.*")) // Remove empty & comment lines
                .collect(Collectors.toList());

        // Validate no comment lines with whitespace before //
        for (String line : cleanedLines) {
            // Check for comment lines that start with whitespace and then //
            if (line.matches("^\\s*//.*$")) {
                // TODO: print 1 and handle error
                System.out.println(ONE);
            }
        }

        // First pass: check global variables and method names
        Map<String, String> globalVariablesType = new HashMap<>();
        Map<String, String> methodNames = new HashMap<>();
        int currentScope = 0;

        // Tracks the current level of braces
        for (String line : cleanedLines) {
            // Adjust brace level
            if (line.contains("{")) {
                currentScope++;
                if (line.matches("^\\s*void\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(((\\s*(final\\s+)?" +
                        "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)(,\\s*(final\\s+)?" +
                        "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*)*)?\\)\\s*\\{\\s*$")) {
                    // Call a function to validate the method name
                    validateMethodName(line, methodNames);
                }
            }
            else if (line.contains("}")) {
                currentScope--;
            }
            // Only process lines when outside any method (currentScope == 0)
            else if (currentScope == 0) {
                // Check for global variables
                if (line.matches("^\\s*(final\\s+)?(int|double|String|boolean|char)" +
                        "\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=.*;$")) {
                    // Call a function to validate the global variable
                    validateGlobalVariable(line, currentScope, globalVariablesType);
                }
                // Check for global variables without initialization
                else if (line.matches("^\\s*(final\\s+)?(int|double|String|boolean|char)" +
                        "\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*;$")) {
                    // Call a function to validate the global variable
                    validateGlobalVariableNoInitialisation(line, currentScope, globalVariablesType);
                }
                else {
                    // Line is illegal
                    // TODO: print 1 and handle error
                }
            }
        }

        if (currentScope != 0) {
            // TODO: print 1 and handle error, missing closing brace
        }

        // Second pass: check the rest of the code
        for (String line : cleanedLines){
            // When inside a method
            if (line.matches("^\\s*void\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(((\\s*(final\\s+)?" +
                    "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)(,\\s*(final\\s+)?" +
                    "(int|double|String|boolean|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*)*)?\\)\\s*\\{\\s*$")) {
                // Update the current scope with the method name before the '(' meaning dont add the ( to the scope
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
        return true;
    }
}

