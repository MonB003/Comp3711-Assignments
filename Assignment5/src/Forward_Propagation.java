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

    public void printWeightsAndBias() {
        System.out.print("Weights: ");
//        for (double currentWeight : weights) {
//            System.out.print(currentWeight + " ");
//        }
        for (int index = 0; index < weights.length; index++) {
            double currentWeight = weights[index];
            System.out.print(currentWeight);
            if (index != weights.length-1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("Bias: " + bias);
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
            // Propagation is calculated by multiplying inputs by the weights
            outputSum += inputs[index] * weights[index];
        }
        return calculateSigmoidFunction(outputSum);
    }

    public double calculateSigmoidFunction(double number) {
        // S(x) = 1 / (1+e^−x)
        return 1 / (1 + Math.exp(-number));
    }

    public double calculateSigmoidDerivative(double sigmoidNumber) {
        return sigmoidNumber * (1 - sigmoidNumber);
    }

    public void updateWeightsAndBias(double[] input, double error) {
        double learningRate = 0.05;

        // Adjust the error using the derivative of the sigmoid
        double sigmoidResult = calculatePropagationOutput(input);
        double updatedErrorResult = error * calculateSigmoidDerivative(sigmoidResult);

        // Update weights and bias
        for (int index = 0; index < weights.length; index++) {
            weights[index] += learningRate * updatedErrorResult * input[index];
        }
        bias = bias + (learningRate * updatedErrorResult);
    }
}
