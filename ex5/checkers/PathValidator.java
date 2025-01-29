package ex5.checkers;

import java.io.File;

public class PathValidator {

    private final String sourceFileName;
    private static final int TWO = 2;

    public PathValidator(String sourceFileName) {

        this.sourceFileName = sourceFileName;
    }

    public boolean validatePath() {

        File sourceFile = new File(sourceFileName);
        if (!sourceFile.exists() || !sourceFile.isFile()) {

            // TODO replace with Error class.
            System.err.println("Error: File not found or invalid filename.");
            System.out.println(TWO);
            return false;
        }

        // Check if the file has the correct format (e.g., ends with ".java")
        if (!sourceFileName.endsWith(".sjava")) {
            // TODO replace with Error class.
            System.err.println("Error: Wrong file format. Expected a .java file.");
            System.out.println(TWO);
            return false;
        }
        return true;
    }
}
