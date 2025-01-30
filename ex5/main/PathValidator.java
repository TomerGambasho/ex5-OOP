package ex5.main;

import java.io.File;

public class PathValidator {

    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int ZERO = 0;
    private static final String SJAVA = ".sjava";
    private final String[] args;

    public PathValidator(String[] args) {

        this.args = args;
    }

    public void validatePath() throws WrongNumberOfInputs, InvalidFileNameException,
            WrongFileFormatException {

        // Check if there is exactly one argument (the source file name)
        wrongNumberOfInputs();

        File sourceFile = new File(args[ZERO]);
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            System.out.println(TWO);
            throw new InvalidFileNameException(args[ZERO]);
        }

        // Check if the file has the correct format (e.g., ends with ".java")
        if (!args[ZERO].endsWith(SJAVA)) {
            System.out.println(TWO);
            throw new WrongFileFormatException(args[ZERO]);
        }
    }

    private void wrongNumberOfInputs() throws WrongNumberOfInputs {
        if (args.length != ONE) {
            System.out.println(TWO);
            throw new WrongNumberOfInputs();
        }
    }
}
