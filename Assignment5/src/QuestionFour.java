import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Subset {
    private final String attributeName;
    private final String attributeValue;
    private final double entropy;
    private final double informationGain;

    public Subset(String attributeName, String attributeValue, double entropy, double informationGain) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.entropy = entropy;
        this.informationGain = informationGain;
    }

    public void printSubset() {
        System.out.println("Subset for " + attributeName + ", Value: " + attributeValue
                + ", Entropy: " + entropy + ", Information Gain: " + informationGain);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public double getEntropy() {
        return entropy;
    }

    public double getInformationGain() {
        return informationGain;
    }
}

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
            
        } catch (Exception exception) {
            // If an error occurs, print the error message
            System.out.println("Error: " + exception.getMessage());
        }
    }

    public static double calculateEntropy(double recordFraction) {
        // Base case: if fraction is 0, entropy is 0
        if (recordFraction == 0) {
            return 0;
        }
        // Calculate entropy: -p(x) log2 p(x)
        double logBase2Result = (Math.log(recordFraction) / Math.log(2));
        double entropyValue = -recordFraction * logBase2Result;
        System.out.println("Entropy = " + entropyValue);
        return entropyValue;
    }

    public static double calculateTotalChildEntropy(ArrayList<Double> recordFractions) {
        // Calculate child entropy: H(X) = −p(x1)log2 p(x1) −p(x2)log2 p(x2) ... −p(xn)log2 p(xn)
        double childEntropy = 0;
        // Sum all entropy values
        for (double currentFraction : recordFractions) {
            childEntropy += calculateEntropy(currentFraction);
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

    public static double compareInformationGains(ArrayList<Subset> subsets) {
        Subset highestSubset = subsets.getFirst();
        double highestInformationGain = highestSubset.getInformationGain();
        for (Subset currentSubset : subsets) {
            if (currentSubset.getInformationGain() > highestInformationGain) {
                highestInformationGain = currentSubset.getInformationGain();
            }
        }

        System.out.println("Highest Information Gain = " + highestInformationGain);
        // Return the entropy of the subset with the highest information gain, so it can become the next parent entropy
        return highestSubset.getEntropy();
    }

    public static HashSet<String> getUniqueAttributeValues(int attributeIndex) {
        HashSet<String> attributeValues = new HashSet<>();

        // Loop through all rows in the dataset
        for (String[] currentData : allFileData) {
            attributeValues.add(currentData[attributeIndex]);
        }

        return attributeValues;
    }

    public static double calculateRecordFractions(int attributeIndex, String currentAttributeValue) {
        // Store the index of the attribute to split on
        int splitOnIndex = attributesIndices.size()-1;
        int yesValueCount = 0;
        int noValueCount = 0;
        double totalValues = 0;

        // Loop through all data to count yes and no occurrences
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
        recordFractions.add(yesRecordFraction);
        recordFractions.add(noRecordFraction);
        return calculateTotalChildEntropy(recordFractions);
    }

    public static ArrayList<Subset> calculateAllSubsetsEntropy(int attributeIndex, double parentEntropy) {
        // Get each type of attribute value
        HashSet<String> uniqueAttributeValues = getUniqueAttributeValues(attributeIndex);
        ArrayList<String> uniqueAttributeList = new ArrayList<>(uniqueAttributeValues);

        ArrayList<Subset> allSubsets = new ArrayList<>();
        // Loop through each subset value for the current attribute
        for (String currentAttributeValue : uniqueAttributeList) {
            // Get average child entropy
            double currentChildEntropy = calculateRecordFractions(attributeIndex, currentAttributeValue);
            // Information Gain = entropy(parent) – [average entropy(children)]
            double currentInformationGain = parentEntropy - currentChildEntropy;
            Subset currentSubset = new Subset(attributesIndices.get(attributeIndex), currentAttributeValue, currentChildEntropy, currentInformationGain);
            currentSubset.printSubset();
            allSubsets.add(currentSubset);
        }

        return allSubsets;
    }

    public static void performID3Algorithm() {
//        // Get entropy of entire subset
//        int booleanIndex = attributesIndices.size()-1;
//        calculateRecordFractions(booleanIndex, "");

        ArrayList<Subset> subsets = calculateAllSubsetsEntropy(0, 1);
        double nextParentEntropy = compareInformationGains(subsets);
    }

    public static void main(String[] args) {
        String filename = args[0];
        storeFileData(filename);
        performID3Algorithm();
    }
}