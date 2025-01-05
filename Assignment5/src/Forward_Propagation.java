/**
 * Forward_Propagation class handles the assignment 5 functionality for storing and updating the weights and bias,
 * calculating the sigmoid function and its derivative, and calculating the propagation output.
 */
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

    /**
     * Constructor for a Forward_Propagation object that initializes the weights and bias.
     */
    public Forward_Propagation() {
        bias = Math.random();
        weights = generateInitialWeights();
    }

    /* Getter methods for inputs and outputs */
    public double[][] getInputs() {
        return inputs;
    }

    public double[][] getOutputs() {
        return outputs;
    }

    /**
     * Prints the current weights and bias values to the console.
     */
    public void printWeightsAndBias() {
        System.out.print("Weights: ");
        for (int index = 0; index < weights.length; index++) {
            double currentWeight = weights[index];
            System.out.print(currentWeight);
            if (index != weights.length-1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("Bias: " + bias + "\n");
    }

    /**
     * Generates an array of initial random weights for the perceptron inputs.
     * @return Double array of the initial weights.
     */
    public double[] generateInitialWeights() {
        int inputSize = inputs[0].length;
        double[] initialWeights = new double[inputSize];
        for (int index = 0; index < initialWeights.length; index++) {
            initialWeights[index] = Math.random();
        }
        return initialWeights;
    }

    /**
     * Calculates the perceptron output by applying the weights, bias, and sigmoid activation to the inputs.
     * @param inputs: Double array of input values.
     * @return Double value of the output.
     */
    public double calculatePropagationOutput(double[] inputs) {
        double outputSum = bias;
        for (int index = 0; index < inputs.length; index++) {
            // Propagation is calculated by multiplying inputs by the weights
            outputSum += inputs[index] * weights[index];
        }
        return calculateSigmoidFunction(outputSum);
    }

    /**
     * Applies the sigmoid activation function to a number.
     * @param number: Double value to apply sigmoid function to.
     * @return Double value of the sigmoid result.
     */
    public double calculateSigmoidFunction(double number) {
        // S(x) = 1 / (1+e^−x)
        return 1 / (1 + Math.exp(-number));
    }

    /**
     * Calculates the derivative of the sigmoid function for a sigmoid output.
     * @param sigmoidNumber: Double value of the sigmoid output.
     * @return Double value of the sigmoid derivative result.
     */
    public double calculateSigmoidDerivative(double sigmoidNumber) {
        return sigmoidNumber * (1 - sigmoidNumber);
    }

    /**
     * Updates the perceptron weights and bias using the input, error, and learning rate.
     * @param input: Double array of input values.
     * @param error: Double value of the calculated error.
     */
    public void updateWeightsAndBias(double[] input, double error) {
        // A learning rate of 0.05 was chosen
        double learningRate = 0.05;

        // Adjust the error using the sigmoid derivative
        double sigmoidResult = calculatePropagationOutput(input);
        double updatedErrorResult = error * calculateSigmoidDerivative(sigmoidResult);

        // Update weights and bias
        for (int index = 0; index < weights.length; index++) {
            weights[index] += learningRate * updatedErrorResult * input[index];
        }
        bias += (learningRate * updatedErrorResult);
    }
}
