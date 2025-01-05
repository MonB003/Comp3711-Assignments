import java.util.Arrays;

public class Train_Test {
    private final Forward_Propagation forwardProp = new Forward_Propagation();
    private static final int ITERATION_COUNT = 1000;
    public void trainPerception() {
        for (int iterationNum = 0; iterationNum < ITERATION_COUNT; iterationNum++) {
            for (int index = 0; index < forwardProp.getInputs().length; index++) {
                double output = forwardProp.calculatePropagationOutput(forwardProp.getInputs()[index]);
                // Error = prediction - actual
                double error = forwardProp.getOutputs()[index][0] - output;
                forwardProp.updateWeightsAndBias(forwardProp.getInputs()[index], error);

                // Print some calculation results
                if (iterationNum % 50 == 0) {
                    System.out.println("Iteration " + iterationNum + ", Input: " + Arrays.toString(forwardProp.getInputs()[index]));
                    System.out.println("Output: " + output + ", Error: " + error);
                    forwardProp.printWeightsAndBias();
                    System.out.println();
                }
            }
        }

        // Print result values
        System.out.println("Perception has been trained.");
        forwardProp.printWeightsAndBias();
    }

//    public void updateWeightsAndBias(double[] input, double error) {
//        double learningRate = 0.05;
//        double[] weights = forwardProp.getWeights();
//
//        // Adjust the error using the derivative of the sigmoid
//        double sigmoidResult = forwardProp.calculatePropagationOutput(input);
//        double updatedErrorResult = error * forwardProp.calculateSigmoidDerivative(sigmoidResult);
//
//        // Update weights and bias
//        for (int index = 0; index < weights.length; index++) {
//            weights[index] += learningRate * updatedErrorResult * input[index];
//        }
//        double updatedBias = forwardProp.getBias() + (learningRate * updatedErrorResult);
//        forwardProp.setBias(updatedBias);
//    }

    public void runTest() {
        System.out.println("--- Running a test ---");
        double[] testInput = {0, 0, 0};  // Test instance
        System.out.print("Test input: ");
        System.out.println(Arrays.toString(testInput));

        double testResult = forwardProp.calculatePropagationOutput(testInput);
        System.out.println("Output: " + testResult);
        System.out.println("Based on the test result, the prediction is that the instance is " + (testResult >= 0.5 ? "real" : "fake"));
    }
}
