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
        System.out.println("Perception has been trained.");
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
        double[] testInput = {0, 0, 0};  // Test instance
        double testResult = fProp.calculatePropagationOutput(testInput);
        System.out.println("Test result: The instance is " + (testResult >= 0.5 ? "real" : "fake"));
    }
}
