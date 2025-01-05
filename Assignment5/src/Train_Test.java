public class Train_Test {
    private final Forward_Propagation fProp = new Forward_Propagation();
    public void trainPerception() {
        for (int iterationNum = 0; iterationNum < 1000; iterationNum++) {
            for (int index = 0; index < fProp.getInputs().length; index++) {
                double output = fProp.calculatePropagationOutput(fProp.getInputs()[index]);
                double error = fProp.getOutputs()[index][0] - output;
                updateWeightsAndBias(fProp.getInputs()[index], error);
            }
        }

        // Print result values
        System.out.println("Perception has been trained.");
        fProp.printWeights();
        System.out.println("Bias: " + fProp.getBias());
    }

    public void updateWeightsAndBias(double[] input, double error) {
        double learningRate = 0.05;
        double[] weights = fProp.getWeights();
        for (int index = 0; index < weights.length; index++) {
            weights[index] += learningRate * error * input[index];
        }
        double updatedBias = fProp.getBias() + (learningRate * error);
        fProp.setBias(updatedBias);
    }

    public void runTest() {
        System.out.println("--- Running a test ---");
        double[] testInput = {0, 0, 0};  // Test instance
        System.out.print("Test input: ");
        for (double value: testInput) {
            System.out.print(value + " ");
        }
        System.out.println();

        double testResult = fProp.calculatePropagationOutput(testInput);
        System.out.println("Test result: " + testResult);
        System.out.println("Based on the test result, the prediction is that the instance is " + (testResult >= 0.5 ? "real" : "fake"));
    }
}
