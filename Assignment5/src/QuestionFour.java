import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class QuestionFour {
    private static final HashMap<Integer, String> attributesIndices = new HashMap<>();
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

            // Read the first line with the attributes (column names) in the file
            currentLine = bufferReader.readLine();
            currentFileData = currentLine.split(",");
            // Store the attributes and their column index in the file
            for (int columnIndex = 0; columnIndex < currentFileData.length; columnIndex++) {
                String attributeName = currentFileData[columnIndex];
                attributesIndices.put(columnIndex, attributeName);
            }

            // Read the file line by line
            while ((currentLine = bufferReader.readLine()) != null) {
                // Get line of data
                currentFileData = currentLine.split(",");
                // Store data in the ArrayList
                allFileData.add(currentFileData);
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

    public static HashSet<String> getUniqueAttributeValues(int attributeIndex) {
        HashSet<String> attributeValues = new HashSet<>();

        // Loop through all rows in the dataset
        for (String[] currentData : allFileData) {
            attributeValues.add(currentData[attributeIndex]);
        }

        return attributeValues;
    }

    public static double calculateAllSubsetsEntropy(int attributeIndex) {
        // Store the index of the attribute to split on
        int splitOnIndex = attributesIndices.size()-1;

        int yesValueCount = 0;
        int noValueCount = 0;
        double totalValues = 0;
        HashSet<String> uniqueAttributeValues = getUniqueAttributeValues(attributeIndex);
        ArrayList<String> uniqueAttributeList = new ArrayList<>(uniqueAttributeValues);

        // Get the attribute value for the current subset
        String currentAttributeValue = uniqueAttributeList.get(0);
        System.out.println("Attribute: " + currentAttributeValue);

        // Loop through all data
        for (String[] currentData : allFileData) {
            // Only count the values that match the current attribute value
            if (currentData[attributeIndex].equalsIgnoreCase(currentAttributeValue)) {
                String booleanValue = currentData[splitOnIndex];
                if (booleanValue.equalsIgnoreCase("yes")) {
                    yesValueCount++;
                } else {
                    noValueCount++;
                }
                totalValues++;
            }
        }

        double yesRecordFraction = yesValueCount / totalValues;
        double noRecordFraction = noValueCount / totalValues;

        System.out.println("Yes fraction = " + yesValueCount + "/" + totalValues);
        System.out.println("No fraction = " + noValueCount + "/" + totalValues);

        ArrayList<Double> recordFractions = new ArrayList<>();
        recordFractions.add(calculateEntropy(yesRecordFraction));
        recordFractions.add(calculateEntropy(noRecordFraction));
        calculateTotalChildEntropy(recordFractions);

//        System.out.println("Subset Entropy = " + subsetEntropy);
        double subsetEntropy = 0;
        return subsetEntropy;
    }

    public static void performID3Algorithm() {
//        // EXAMPLE NUMBERS
//        double num1 = (double) 3/5;
//        double entropy1 = calculateEntropy(num1);
//        double num2 = (double) 2/5;
//        double entropy2 = calculateEntropy(num2);
//        ArrayList<Double> entropyValues = new ArrayList<>();
//        entropyValues.add(entropy1);
//        entropyValues.add(entropy2);
//        calculateTotalChildEntropy(entropyValues);
//
//        num1 = (double) 5/14;
//        entropy1 = 0.97098;
//        num2 = (double) 5/14;
//        entropy2 = 0.97098;
//        ArrayList<double[]> fractionEntropyPairs = new ArrayList<>();
//        fractionEntropyPairs.add(new double[]{num1, entropy1});
//        fractionEntropyPairs.add(new double[]{num2, entropy2});
//        calculateAverageChildrenEntropy(fractionEntropyPairs);
//
//        getUniqueAttributeValues(0);

        calculateAllSubsetsEntropy(0);
    }

    public static void main(String[] args) {
        String filename = args[0];
        storeFileData(filename);
        performID3Algorithm();
    }
}