import java.util.Arrays;

/**
 * Train_Test class handles the assignment 5 functionality for training and testing a perception.
 */
public class Train_Test {
    // Object for managing the weights and bias
    private final Forward_Propagation forwardProp = new Forward_Propagation();
    // Number of iterations to run for training
    private static final int ITERATION_COUNT = 1000;

    /**
     * Trains the perceptron by iterating through the inputs, calculating errors, updating weights and bias,
     * and printing progress every 100 iterations.
     */
    public void trainPerception() {
        for (int iterationNum = 0; iterationNum < ITERATION_COUNT; iterationNum++) {
            double errorSum = 0;
            for (int index = 0; index < forwardProp.getInputs().length; index++) {
                double output = forwardProp.calculatePropagationOutput(forwardProp.getInputs()[index]);
                // Error = prediction - actual
                double error = forwardProp.getOutputs()[index][0] - output;
                forwardProp.updateWeightsAndBias(forwardProp.getInputs()[index], error);
                errorSum += Math.abs(error);
            }

            // Print some calculation results
            if (iterationNum % 100 == 0) {
                System.out.println("Iteration " + iterationNum + ", Total error: " + errorSum);
                forwardProp.printWeightsAndBias();
            }
        }

        // Print result values
        System.out.println("\nPerception has been trained.");
        forwardProp.printWeightsAndBias();
    }

    /**
     * Runs a test on the perceptron using a test input, calculates the output, and prints whether the instance is
     * predicted as "real" or "fake."
     */
    public void runTest() {
        System.out.println("\nRunning a test.");
        double[] testInput = {0, 0, 0};  // Test instance
        System.out.print("Test input: ");
        System.out.println(Arrays.toString(testInput));

        double testResult = forwardProp.calculatePropagationOutput(testInput);
        System.out.println("Output: " + testResult);
        System.out.println("Based on the test result, the prediction is that the instance is " + (testResult >= 0.5 ? "real" : "fake"));
    }
}
