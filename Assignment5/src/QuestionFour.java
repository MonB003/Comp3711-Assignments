import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

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

//    public void printSubset() {
//        System.out.println("Subset for " + attributeName + ", Value: " + attributeValue
//                + ", Entropy: " + entropy + ", Information Gain: " + informationGain);
//    }

//    public String getAttributeName() {
//        return attributeName;
//    }
//
//    public String getAttributeValue() {
//        return attributeValue;
//    }

    public double getEntropy() {
        return entropy;
    }

    public double getInformationGain() {
        return informationGain;
    }
}

class TreeNode {
    String attribute; // The attribute used to split data
    Map<String, TreeNode> children; // Children nodes, keyed by attribute value
    String nodeType; // Classification if it's a leaf node

    public TreeNode(String attribute) {
        this.attribute = attribute;
        this.children = new HashMap<>();
    }

    public TreeNode(String nodeType, boolean isLeaf) {
        this.nodeType = nodeType;
        this.children = null;
    }

    public boolean isLeaf() {
        return nodeType != null;
    }
}

public class QuestionFour {
    private static final HashMap<Integer, String> attributesIndices = new HashMap<>();
    private static final ArrayList<String> allAttributes = new ArrayList<>();
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
                allAttributes.add(attributeName);
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
//        System.out.println("Entropy = " + entropyValue);
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

//        System.out.println("Average Children Entropy = " + averageEntropy);
        return averageEntropy;
    }

    public static double calculateSubsetEntropy(ArrayList<String[]> subsetData) {
        // Store the index of the attribute to split on
        int booleanIndex = attributesIndices.size() - 1;
        int yesValueCount = 0;
        int noValueCount = 0;
        double totalValues = subsetData.size();

        // Loop through all data to count yes and no occurrences
        for (String[] currentData : subsetData) {
            String booleanValue = currentData[booleanIndex];
            if (booleanValue.equalsIgnoreCase("yes")) {
                yesValueCount++;
            } else {
                noValueCount++;
            }
        }

        double yesFraction = yesValueCount / totalValues;
        double noFraction = noValueCount / totalValues;

        System.out.println("Yes fraction = " + yesValueCount + "/" + totalValues);
        System.out.println("No fraction = " + noValueCount + "/" + totalValues);

        ArrayList<Double> recordFractions = new ArrayList<>();
        recordFractions.add(yesFraction);
        recordFractions.add(noFraction);
        return calculateTotalChildEntropy(recordFractions);
    }

    public static void printTree(TreeNode node, String prefix) {
        if (node.isLeaf()) {
            System.out.println(prefix + "Leaf: " + node.nodeType);
        } else {
            System.out.println(prefix + "Attribute: " + node.attribute);
            for (Map.Entry<String, TreeNode> entry : node.children.entrySet()) {
                printTree(entry.getValue(), prefix + "  " + entry.getKey() + " -> ");
            }
        }
    }

    private static String checkIfAllSameClassification(List<String[]> dataSubset) {
        int targetIndex = attributesIndices.size() - 1; // Index of the target column
        String firstValue = dataSubset.get(0)[targetIndex];

        for (String[] record : dataSubset) {
            if (!record[targetIndex].equalsIgnoreCase(firstValue)) {
                return null; // Not all classifications are the same
            }
        }

        return firstValue; // All classifications are the same
    }

    private static String findMajorityClass(List<String[]> dataSubset) {
        int targetIndex = attributesIndices.size() - 1; // Index of the target column
        Map<String, Integer> classCounts = new HashMap<>();

        for (String[] record : dataSubset) {
            String classification = record[targetIndex];
            classCounts.put(classification, classCounts.getOrDefault(classification, 0) + 1);
        }

        return Collections.max(classCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public static HashMap<String, ArrayList<String[]>> splitAttributeData(ArrayList<String[]> data, int attributeIndex) {
        // Stores pairs of: attribute name, all data values for that attribute
        HashMap<String, ArrayList<String[]>> splitData = new HashMap<>();

        // Loop through all rows in the dataset
        for (String[] currentData : data) {
            // Get the value of the attribute to split on
            String attributeValue = currentData[attributeIndex];

            // If the value doesn't exist in the map, create a new list for it
            if (!splitData.containsKey(attributeValue)) {
                splitData.put(attributeValue, new ArrayList<>());
            }

            // Add the current row to the appropriate list
            splitData.get(attributeValue).add(currentData);
        }

        return splitData;
    }

    private static String getBestAttribute(List<String[]> dataSubset, List<String> attributes, HashSet<Integer> visitedAttributes) {
        double parentEntropy = calculateSubsetEntropy(new ArrayList<>(dataSubset));
        double maxGain = -1;
        String bestAttribute = null;

        for (int attributeIndex = 0; attributeIndex < attributes.size()-1; attributeIndex++) {
//        for (String attribute : attributes) {
//            int attributeIndex = attributes.indexOf(attribute);
            System.out.println("Current Attribute Index: " + attributeIndex);
            System.out.println("Attributes: " + attributes);
            System.out.println("Visited Attributes: " + visitedAttributes);

            if (visitedAttributes.contains(attributeIndex)) {
                continue;
            }

            String attribute = attributes.get(attributeIndex);
            System.out.println("Attribute: " + attribute);
//            if (!visitedAttributes.contains(attributeIndex)) {
                HashMap<String, ArrayList<String[]>> subsets = splitAttributeData(new ArrayList<>(dataSubset), attributeIndex);

                // Calculate average entropy for subsets
                ArrayList<double[]> fractionEntropyPairs = new ArrayList<>();
                for (String value : subsets.keySet()) {
                    ArrayList<String[]> subset = subsets.get(value);
                    double subsetFraction = (double) subset.size() / dataSubset.size();
                    double subsetEntropy = calculateSubsetEntropy(subset);
                    fractionEntropyPairs.add(new double[]{subsetFraction, subsetEntropy});
                }

                double averageEntropy = calculateAverageChildrenEntropy(fractionEntropyPairs);
                double informationGain = parentEntropy - averageEntropy;

                // Check for maximum information gain
                if (informationGain > maxGain) {
                    maxGain = informationGain;
                    bestAttribute = attribute;
                }

                // Debugging output
                System.out.println("Parent Entropy: " + parentEntropy);
                System.out.println("Average Children Entropy: " + averageEntropy);
                System.out.println("Information Gain: " + informationGain);
                System.out.println("--------------------------");

                // Mark attribute as visited
//                visitedAttributes.add(attributeIndex);
//            }
        }

        return bestAttribute;
    }

    public static TreeNode performID3Algorithm(List<String[]> dataSubset, List<String> remainingAttributes, HashSet<Integer> visitedAttributes) {
        // Base case: Check if all examples have the same classification
        String classification = checkIfAllSameClassification(dataSubset);
        if (classification != null) {
//            System.out.println("SAME CLASSIFICATION");
            return new TreeNode(classification, true); // Leaf node
        }

        // Base case: No attributes left to split
        if (remainingAttributes.isEmpty()) {
            String majorityClass = findMajorityClass(dataSubset);
//            System.out.println("MAJORITY CLASS: " + majorityClass);
            return new TreeNode(majorityClass, true); // Leaf node
        }

        // Find the best attribute to split on
        String bestAttribute = getBestAttribute(dataSubset, remainingAttributes, visitedAttributes);
        if (bestAttribute == null) { // Edge case: No best attribute found
            String majorityClass = findMajorityClass(dataSubset);
            return new TreeNode(majorityClass, true); // Leaf node
        }
        TreeNode root = new TreeNode(bestAttribute);
        System.out.println("Next attribute: " + bestAttribute);

        // Get index of the best attribute
        int bestAttributeIndex = allAttributes.indexOf(bestAttribute);
        visitedAttributes.add(bestAttributeIndex);

        // Split data by the best attribute
        HashMap<String, ArrayList<String[]>> subsets = splitAttributeData(new ArrayList<>(dataSubset), bestAttributeIndex);

        // Recurse for each subset
//        for (String attributeValue : subsets.keySet()) {
//            List<String[]> subset = subsets.get(attributeValue);
//
//            // Exclude the best attribute from remaining attributes
//            List<String> newRemainingAttributes = new ArrayList<>(remainingAttributes);
//            newRemainingAttributes.remove(bestAttribute);
//
//            // Create child nodes recursively
//            root.children.put(attributeValue, performID3Algorithm(subset, newRemainingAttributes, visitedAttributes));
//        }
        for (Map.Entry<String, ArrayList<String[]>> entry : subsets.entrySet()) {
            String attributeValue = entry.getKey();
            ArrayList<String[]> subset = entry.getValue();

            // Remove the best attribute from the remaining attributes
            List<String> newRemainingAttributes = new ArrayList<>(remainingAttributes);
            newRemainingAttributes.remove(bestAttribute);

            // Recursively call performID3Algorithm on the subset
            TreeNode childNode = performID3Algorithm(subset, newRemainingAttributes, visitedAttributes);

            // Add the child node to the current node
            root.children.put(attributeValue, childNode);
        }

        return root;
    }

    public static Subset getHighestInformationGain(ArrayList<Subset> subsets) {
        Subset highestSubset = subsets.getFirst();
        double highestInformationGain = highestSubset.getInformationGain();
        for (Subset currentSubset : subsets) {
            if (currentSubset.getInformationGain() > highestInformationGain) {
                highestInformationGain = currentSubset.getInformationGain();
            }
        }

        System.out.println("Highest Information Gain = " + highestInformationGain);
        // Return the entropy of the subset with the highest information gain, so it can become the next parent entropy
        return highestSubset;
    }

    public static double calculateParentEntropy() {
        // Calculate entropy of the entire dataset
        System.out.println("Calculations for entire dataset:");

        double parentEntropy = calculateSubsetEntropy(allFileData);
        System.out.println("Dataset Entropy: " + parentEntropy);

        ArrayList<Subset> parentEntropyOptions = new ArrayList<>();

        // Find the first attribute to split on by calculating the information gain for each attribute
        for (int attributeIndex = 0; attributeIndex < attributesIndices.size() - 1; attributeIndex++) {
            System.out.println("-----------------------------");
            System.out.println("Attribute: " + attributesIndices.get(attributeIndex));

            // Calculate the information gain for this attribute
            HashMap<String, ArrayList<String[]>> subsets = splitAttributeData(allFileData, attributeIndex);
            ArrayList<Subset> subsetsInfo = new ArrayList<>();

            for (String attributeValue : subsets.keySet()) {
                ArrayList<String[]> subset = subsets.get(attributeValue);
                double subsetEntropy = calculateSubsetEntropy(subset);
                double informationGain = parentEntropy - subsetEntropy;
                Subset currentSubset = new Subset(attributesIndices.get(attributeIndex), attributeValue, subsetEntropy, informationGain);
                subsetsInfo.add(currentSubset);
            }

            Subset nextHighestSubset = getHighestInformationGain(subsetsInfo);
            parentEntropyOptions.add(nextHighestSubset);
        }

        Subset nextParentSubset = getHighestInformationGain(parentEntropyOptions);
        return nextParentSubset.getEntropy();
    }

    public static void calculateAllSubsetsEntropy(HashMap<String, ArrayList<String[]>> subsets, double parentEntropy) {
        for (String attributeValue : subsets.keySet()) {
            ArrayList<String[]> subset = subsets.get(attributeValue);

            // Calculate entropy for this subset
            double subsetEntropy = calculateSubsetEntropy(subset);

            // Calculate information gain: Information Gain = entropy(parent) – [average entropy(children)]
            double informationGain = parentEntropy - subsetEntropy;

            // Print the subset values along with its entropy and information gain
//            System.out.println("SUBSET for attribute value: " + attributeValue);
//            for (String[] arrayValue: subset) {
//                for (String value: arrayValue) {
//                    System.out.print(value + " ");
//                }
//                System.out.println();
//            }
            System.out.println("Entropy: " + subsetEntropy);
            System.out.println("Information Gain: " + informationGain);
            System.out.println();
        }
    }


    public static void performID3AlgorithmV2() {
        double parentEntropy = calculateParentEntropy();
        int numAttributes = allFileData.getFirst().length;
        for (int index = 0; index < numAttributes; index++) {
            HashMap<String, ArrayList<String[]>> currentAttributeData = splitAttributeData(allFileData, index);
            calculateAllSubsetsEntropy(currentAttributeData, parentEntropy);
        }
    }

    public static void main(String[] args) {
//        String filename = args[0];
//        storeFileData(filename);
//        HashSet<Integer> visitedAttributes = new HashSet<>();
//        TreeNode treeResult = performID3Algorithm(allFileData, allAttributes, visitedAttributes);
//        System.out.println("\nFinal decision tree:");
//        printTree(treeResult, "");

        String filename = args[0];
        storeFileData(filename);
        performID3AlgorithmV2();
    }
}