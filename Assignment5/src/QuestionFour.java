import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class QuestionFour {
    private static final ArrayList<String[]> allFileData = new ArrayList<>();
    public static void storeFileData(String filename) {
        try {
            // Create objects to read the file
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            // Store current file line and its data
            String currentLine;
            String[] currentFileData;

            // Read the file line by line
            while ((currentLine = bufferReader.readLine()) != null) {
                // Get line of data
                currentFileData = currentLine.split(",");
                // Store data in the ArrayList
                allFileData.add(currentFileData);

//                // Print data
//                for (String data : currentFileData) {
//                    System.out.print(data + " ");
//                }
//                System.out.println();
            }
            bufferReader.close();
            
        } catch(Exception exception) {
            // If an error occurs, print the error message
            System.out.println("Error: " + exception.getMessage());
        }
    }

    public static double calculateEntropy(double recordFraction) {
        // Calculate entropy: -p(x) log2 p(x)
        double entropyValue;
        double logBase2Result = (Math.log(recordFraction) / Math.log(2));
        entropyValue = -recordFraction * logBase2Result;
        System.out.println("Entropy = " + entropyValue);
        return entropyValue;
    }

    public static double calculateTotalChildEntropy(ArrayList<Double> entropyValues) {
        // Calculate child entropy: H(X) = −p(x1)log2 p(x1) −p(x2)log2 p(x2) ... −p(xn)log2 p(xn)
        double childEntropy = 0;
        // Sum all entropy values
        for (double currentEntropy : entropyValues) {
            childEntropy += currentEntropy;
        }

        System.out.println("Child Entropy = " + childEntropy);
        return childEntropy;
    }

    public static double calculateAverageChildrenEntropy(ArrayList<double[]> fractionEntropyPairs) {
        double averageEntropy = 0;

//        // Loop through each set of key value pairs in the HashMap
//        for (HashMap.Entry<Double, Double> entry : fractionEntropyPairs.entrySet()) {
//            double currentFraction = entry.getKey();
//            double currentEntropy = entry.getValue();
//
//            // Calculate weighted average entropy by multiplying each fraction by its entropy, and sum the results
//            averageEntropy += currentFraction * currentEntropy;
//        }

        // Loop through each set of pairs in the ArrayList
        for (double[] currentPair : fractionEntropyPairs) {
            double currentFraction = currentPair[0];
            double currentEntropy = currentPair[1];

            // Calculate weighted average entropy by multiplying each fraction by its entropy, and sum the results
            averageEntropy += currentFraction * currentEntropy;
        }

        System.out.println("Average Children Entropy = " + averageEntropy);
        return averageEntropy;
    }

    public static double calculateInformationGain(double parentEntropy, double averageChildrenEntropy) {
        // Information Gain = entropy(parent) – [average entropy(children)]
        return parentEntropy - averageChildrenEntropy;
    }

    public static double compareInformationGains(ArrayList<Double> informationGains) {
        double highestInformationGain = informationGains.getFirst();
        for (double currentInformationGain : informationGains) {
            if (currentInformationGain > highestInformationGain) {
                highestInformationGain = currentInformationGain;
            }
        }

        System.out.println("Highest Information Gain = " + highestInformationGain);
        return highestInformationGain;
    }

    public static void performID3Algorithm() {
        // EXAMPLE NUMBERS
        double num1 = (double) 3/5;
        double entropy1 = calculateEntropy(num1);
        double num2 = (double) 2/5;
        double entropy2 = calculateEntropy(num2);
        ArrayList<Double> entropyValues = new ArrayList<>();
        entropyValues.add(entropy1);
        entropyValues.add(entropy2);
        calculateTotalChildEntropy(entropyValues);

        num1 = (double) 5/14;
        entropy1 = 0.97098;
        num2 = (double) 5/14;
        entropy2 = 0.97098;
        ArrayList<double[]> fractionEntropyPairs = new ArrayList<>();
        fractionEntropyPairs.add(new double[]{num1, entropy1});
        fractionEntropyPairs.add(new double[]{num2, entropy2});
        calculateAverageChildrenEntropy(fractionEntropyPairs);


//        for (String[] currentData: allFileData) {
//
//        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        storeFileData(filename);
        performID3Algorithm();
    }
}