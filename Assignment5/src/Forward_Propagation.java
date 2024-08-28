public class Forward_Propagation {
    private final double[][] inputs = new double[][]{
            {0, 0, 1},
            {1, 1, 1},
            {1, 0, 1},
            {0, 1, 1}
    };
    private final double[][] outputs = new double[][]{
            {0},
            {1},
            {1},
            {0}
    };
    private double bias;
    private final double[] weights;

    public Forward_Propagation() {
        bias = Math.random();
        weights = generateInitialWeights();
    }

    public double[][] getInputs() {
        return inputs;
    }

    public double[][] getOutputs() {
        return outputs;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double newBias) {
        bias = newBias;
    }

    public double[] getWeights() {
        return weights;
    }

    public double[] generateInitialWeights() {
        int inputSize = inputs[0].length;
        double[] initialWeights = new double[inputSize];
        for (int index = 0; index < initialWeights.length; index++) {
            initialWeights[index] = Math.random();
        }
        return initialWeights;
    }

    public double calculatePropagationOutput(double[] inputs) {
        double outputSum = bias;
        for (int index = 0; index < inputs.length; index++) {
            outputSum += inputs[index] * weights[index];
        }
        return calculateSigmoidFunction(outputSum);
    }

    public double calculateSigmoidFunction(double number) {
        return 1 / (1 + Math.exp(-number));
    }
}
