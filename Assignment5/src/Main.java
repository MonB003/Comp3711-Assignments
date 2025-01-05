/**
 * Main class handles the assignment 5 functionality for running the question 5 code.
 */
public class Main {
    /**
     * Main method that runs the program, which creates a Train_Test object, trains a perception, and runs a test.
     * @param args: Command line arguments passed to the program.
     */
    public static void main(String[] args) {
        Train_Test trainTest = new Train_Test();
        trainTest.trainPerception();
        trainTest.runTest();
    }
}
