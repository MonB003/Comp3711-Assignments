import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

class AttributeSubset { // Ex. Math, Science
    private final String attributeName;
    private final double informationGain;  // Information gain or entropy for child subset
    private final int attributeIndex;
    private final List<ChildSubset> childrenSubsets;

    // Constructor for leaf node (no children)
    public AttributeSubset(String attributeName, double informationGain, int attributeIndex) {
        this.attributeName = attributeName;
        this.informationGain = informationGain;
        this.attributeIndex = attributeIndex;
        this.childrenSubsets = null;  // No children for leaf nodes
    }

    // Constructor for non-leaf nodes (with children)
    public AttributeSubset(String attributeName, double informationGain, List<ChildSubset> childrenSubsets, int attributeIndex) {
        this.attributeName = attributeName;
        this.informationGain = informationGain;
        this.childrenSubsets = childrenSubsets;
        this.attributeIndex = attributeIndex;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public double getInformationGain() {
        return informationGain;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public List<ChildSubset> getChildrenSubsets() {
        return childrenSubsets;
    }
}

class ChildSubset { // Ex. A, A+
    private final String attributeName;
    private final double entropy;

    // Constructor for leaf node (no children)
    public ChildSubset(String attributeName, double entropy) {
        this.attributeName = attributeName;
        this.entropy = entropy;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public double getEntropy() {
        return entropy;
    }
}

class InfoGainResult {
    private final double informationGain;
    private final ArrayList<ChildSubset> subsets;

    public InfoGainResult(double informationGain, ArrayList<ChildSubset> subsets) {
        this.informationGain = informationGain;
        this.subsets = subsets;
    }

    public double getInformationGain() {
        return informationGain;
    }

    public ArrayList<ChildSubset> getSubsets() {
        return subsets;
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
    private static final List<String> allAttributes = new ArrayList<>();
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

    public static double calculateAverageChildrenEntropy(ArrayList<double[]> fractionEntropyPairs, double totalEntries) {
        double averageEntropy = 0.0;

        // Loop through each set of pairs in the ArrayList
        for (double[] currentPair : fractionEntropyPairs) {
            double currentChildSize = currentPair[0];
            double currentEntropy = currentPair[1];

            // Calculate weighted average entropy by multiplying each fraction by its entropy, and sum the results
            averageEntropy += (currentChildSize / totalEntries) * currentEntropy;
        }

        System.out.println("Average Children Entropy = " + averageEntropy);
        return averageEntropy;
    }

//    public static double calculateSubsetEntropy(ArrayList<String[]> subsetData) {
//        // Store the index of the attribute to split on
//        int booleanIndex = attributesIndices.size() - 1;
//        int yesValueCount = 0;
//        int noValueCount = 0;
//        double totalValues = subsetData.size();
//
//        // Loop through all data to count yes and no occurrences
//        for (String[] currentData : subsetData) {
//            String booleanValue = currentData[booleanIndex];
//            if (booleanValue.equalsIgnoreCase("yes")) {
//                yesValueCount++;
//            } else {
//                noValueCount++;
//            }
//        }
//
//        double yesFraction = yesValueCount / totalValues;
//        double noFraction = noValueCount / totalValues;
//
//        System.out.println("Yes fraction = " + yesValueCount + "/" + totalValues);
//        System.out.println("No fraction = " + noValueCount + "/" + totalValues);
//
//        // Calculate child entropy: H(X) = −p(x1)log2 p(x1) −p(x2)log2 p(x2) ... −p(xn)log2 p(xn)
//        return (calculateEntropy(yesFraction) + calculateEntropy(noFraction));
//    }

    public static double calculateSubsetEntropy(ArrayList<String[]> subsetData) {
        int booleanIndex = attributesIndices.size() - 1;
        // Count occurrences of "Yes" and "No"
        int yesValueCount = 0;
        int noValueCount = 0;
        double totalValues = subsetData.size();

        for (String[] currentData : subsetData) {
            String booleanValue = currentData[booleanIndex];  // Assuming booleanValue is the last element in the record
            if (booleanValue.equalsIgnoreCase("yes")) {
                yesValueCount++;
            } else {
                noValueCount++;
            }
        }

        // Now calculate probabilities based on counts
        double pYes = yesValueCount / totalValues;
        double pNo = noValueCount / totalValues;

        System.out.println("Yes fraction = " + yesValueCount + "/" + totalValues);
        System.out.println("No fraction = " + noValueCount + "/" + totalValues);

        // Calculate entropy using label counts
        double entropy = 0.0;
        if (pYes > 0) {
            entropy -= pYes * Math.log(pYes) / Math.log(2);
        }
        if (pNo > 0) {
            entropy -= pNo * Math.log(pNo) / Math.log(2);
        }

        return entropy;
    }


    public static void printTree(TreeNode node, String prefix) {
        if (node.isLeaf()) {
            System.out.println(prefix + "Leaf: " + node.nodeType);
        } else {
            System.out.println(prefix + "Subset: " + node.attribute);
            for (Map.Entry<String, TreeNode> entry : node.children.entrySet()) {
                printTree(entry.getValue(), prefix + "  " + entry.getKey() + " -> ");
            }
        }
    }

    public static HashMap<String, ArrayList<String[]>> splitSubsetData(ArrayList<String[]> data, int attributeIndex) {
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

    public static AttributeSubset getHighestInformationGain(ArrayList<AttributeSubset> subsets) {
        AttributeSubset highestSubset = subsets.getFirst();
        double highestInformationGain = highestSubset.getInformationGain();
        for (AttributeSubset currentSubset : subsets) {
            if (currentSubset.getInformationGain() > highestInformationGain) {
                highestInformationGain = currentSubset.getInformationGain();
            }
        }

        System.out.println("\nHighest Information Gain is " + highestSubset.getAttributeName() + ": " + highestInformationGain);
        // Return the entropy of the subset with the highest information gain, so it can become the next parent entropy
        return highestSubset;
    }

    private static ArrayList<String[]> getSubsetForAttributeValue(ArrayList<String[]> data, int attributeIndex, String value) {
        ArrayList<String[]> subset = new ArrayList<>();
        for (String[] row : data) {
            if (row[attributeIndex].equals(value)) {
                subset.add(row);
            }
        }
        return subset;
    }

    // Method to calculate information gain for an attribute
    public static InfoGainResult calculateInformationGain(ArrayList<String[]> data, int attributeIndex) {
        double parentEntropy = calculateSubsetEntropy(data);
        System.out.println("Parent Entropy: " + parentEntropy);
        System.out.println("ATTRIBUTE INDEX: " + attributeIndex);

        // Split data based on attribute
        HashMap<String, ArrayList<String[]>> subsets = splitSubsetData(data, attributeIndex); // SPLIT CAUSES SUBSET LETTERS TO GET LOST
        ArrayList<ChildSubset> subsetsInfo = new ArrayList<>();

        // Loop through each child subset in the attribute
        ArrayList<double[]> sizeEntropyPairs = new ArrayList<>();
        double totalEntries = 0;
        for (String attributeValue : subsets.keySet()) {
            System.out.println("\nSubset: " + attributeValue);
            ArrayList<String[]> subset = subsets.get(attributeValue);
            double subsetEntropy = calculateSubsetEntropy(subset);
            System.out.println("Entropy: " + subsetEntropy);
            totalEntries += subset.size();
            sizeEntropyPairs.add(new double[]{subset.size(), subsetEntropy});

            // Store child subset information
            ChildSubset currentSubset = new ChildSubset(attributeValue, subsetEntropy);
            subsetsInfo.add(currentSubset);
        }

//        for (String[] row : data) {
//            String attributeValue = row[attributeIndex];
//            // Find the subset for this attribute value
//            ArrayList<String[]> subset = getSubsetForAttributeValue(data, attributeIndex, attributeValue);
//
////            double subsetEntropy = calculateSubsetEntropy(subset);
////            sizeEntropyPairs.add(new double[]{subset.size(), subsetEntropy});
////            subsetsInfo.add(new ChildSubset(attributeValue, subsetEntropy));
//
//            double subsetEntropy = calculateSubsetEntropy(subset);
//            System.out.println("Entropy: " + subsetEntropy);
//            totalEntries += subset.size();
//            sizeEntropyPairs.add(new double[]{subset.size(), subsetEntropy});
//
//            // Store child subset information
//            ChildSubset currentSubset = new ChildSubset(attributeValue, subsetEntropy);
//            subsetsInfo.add(currentSubset);
//        }

        // Calculate average entropy and information gain
        double averageSubsetEntropy = calculateAverageChildrenEntropy(sizeEntropyPairs, totalEntries);
        double infoGain = parentEntropy - averageSubsetEntropy; // Information Gain
        return new InfoGainResult(infoGain, subsetsInfo);
    }

    // Main method to calculate parent entropy and find the best attribute to split on
//    public static AttributeSubset getNextSplitAttribute(ArrayList<String[]> currentFileData, List<String> attributesIndices) {
//        // Calculate entropy of the entire dataset (called only once)
////        double parentEntropy = calculateSubsetEntropy(currentFileData);
////        System.out.println("Parent Entropy: " + parentEntropy);
//
//        ArrayList<AttributeSubset> parentEntropyOptions = new ArrayList<>();
//
//        // Find the best attribute to split on by calculating information gain for each attribute
//        for (int attributeIndex = 0; attributeIndex < attributesIndices.size() - 1; attributeIndex++) {
//            String attribute = attributesIndices.get(attributeIndex);
//            System.out.println("\nAttribute: " + attribute);
//
//            // Calculate the information gain for this attribute
//            InfoGainResult infoGainResult = calculateInformationGain(currentFileData, attributeIndex);
//            double informationGain = infoGainResult.getInformationGain();
//            System.out.println("INFO GAIN for attribute " + attribute + ": " + informationGain);
//
//            // Store the result for this attribute
//            AttributeSubset currentSubset = new AttributeSubset(attribute, informationGain, infoGainResult.getSubsets(), attributeIndex);
//            parentEntropyOptions.add(currentSubset);
//        }
//
//        // Return the best subset with the highest information gain
//        return getHighestInformationGain(parentEntropyOptions);
//    }

    public static double calculateInformationGainForSubset(ArrayList<String[]> subsetData, int attributeIndex) {
        // This method only calculates information gain for a subset and one attribute
        // Calculate the entropy of the subset
        double subsetEntropy = calculateSubsetEntropy(subsetData);

        // Then, calculate the information gain for splitting based on this attribute
        InfoGainResult infoGainResult = calculateInformationGain(subsetData, attributeIndex);
        return infoGainResult.getInformationGain();
    }

    public static AttributeSubset getNextSplitAttribute(ArrayList<String[]> subsetData, List<String> remainingAttributes) {
        ArrayList<AttributeSubset> parentEntropyOptions = new ArrayList<>();

        // Loop through remaining attributes to calculate information gain
        for (String attribute : remainingAttributes) {
            int attributeIndex = getAttributeIndex(attribute);

            // Calculate the information gain for this attribute based on the subset data
            double informationGain = calculateInformationGainForSubset(subsetData, attributeIndex);
            System.out.println("INFO GAIN for attribute " + attribute + ": " + informationGain);

            // Store the result for this attribute
            AttributeSubset currentSubset = new AttributeSubset(attribute, informationGain, null, attributeIndex);
            parentEntropyOptions.add(currentSubset);
        }

        // Return the best subset with the highest information gain
        return getHighestInformationGain(parentEntropyOptions);
    }

    public static AttributeSubset getFirstSplitAttribute(ArrayList<String[]> currentFileData, List<String> attributesIndices) {
        // Calculate entropy of the entire dataset (called only once)
//        double parentEntropy = calculateSubsetEntropy(currentFileData);
//        System.out.println("Parent Entropy: " + parentEntropy);

        ArrayList<AttributeSubset> parentEntropyOptions = new ArrayList<>();

        // Find the best attribute to split on by calculating information gain for each attribute
        for (int attributeIndex = 0; attributeIndex < attributesIndices.size() - 1; attributeIndex++) {
            String attribute = attributesIndices.get(attributeIndex);
            System.out.println("\nAttribute: " + attribute);

            // Calculate the information gain for this attribute
            InfoGainResult infoGainResult = calculateInformationGain(currentFileData, attributeIndex);
            double informationGain = infoGainResult.getInformationGain();
            System.out.println("INFO GAIN for attribute " + attribute + ": " + informationGain);

            // Store the result for this attribute
            AttributeSubset currentSubset = new AttributeSubset(attribute, informationGain, infoGainResult.getSubsets(), attributeIndex);
            parentEntropyOptions.add(currentSubset);
        }

        // Return the best subset with the highest information gain
        return getHighestInformationGain(parentEntropyOptions);
    }

    private static String getClassLabel(ArrayList<String[]> data) {
        String classLabel = null;

        // Check if all examples have the same class label
        for (String[] record : data) {
            String currentLabel = record[record.length - 1]; // Assuming class label is the last column
            if (classLabel == null) {
                classLabel = currentLabel;
            } else if (!classLabel.equals(currentLabel)) {
                return null; // Data contains mixed class labels, not a leaf node
            }
        }

        return classLabel; // Return class label if all examples have the same label
    }

    public static int getAttributeIndex(String attributeName) {
        for (Map.Entry<Integer, String> entry : attributesIndices.entrySet()) {
            if (entry.getValue().equals(attributeName)) {
                return entry.getKey();
            }
        }
        return -1; // Return -1 if the attribute is not found
    }

    private static String getMajorityClass(ArrayList<String[]> data) {
        HashMap<String, Integer> classCounts = new HashMap<>();

        // Count occurrences of each class label
        for (String[] record : data) {
            String classLabel = record[record.length - 1];
            classCounts.put(classLabel, classCounts.getOrDefault(classLabel, 0) + 1);
        }

        // Find the class with the maximum count
        String majorityClass = null;
        int maxCount = -1;
        for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                majorityClass = entry.getKey();
            }
        }

        return majorityClass;
    }

    public static TreeNode buildTree(ArrayList<String[]> data, List<String> attributes) {
        // Base case: if the data is pure or no attributes left, return a leaf node
        String classLabel = getClassLabel(data);
        if (classLabel != null) {
            return new TreeNode(classLabel, true); // Leaf node
        }

        // Base case: if no attributes are left to split on, return a leaf node with the majority class
        if (attributes.isEmpty()) { //  || attributes.size() == 1
            String majorityClass = getMajorityClass(data);
            return new TreeNode(majorityClass, true); // Leaf node
        }

        // Step 1: Calculate the best attribute to split on
        AttributeSubset bestSubset = getNextSplitAttribute(data, attributes);

        // Step 2: Create a new TreeNode with the best attribute
        TreeNode node = new TreeNode(bestSubset.getAttributeName());

        // Step 3: Recursively split the data and add child nodes
        List<String> remainingAttributes = new ArrayList<>(attributes);
        remainingAttributes.remove(bestSubset.getAttributeName());

        System.out.println("--- Splitting on " + bestSubset.getAttributeName() + " ---");
        int nextIndexSplit = getAttributeIndex(bestSubset.getAttributeName());
        HashMap<String, ArrayList<String[]>> splitData = splitSubsetData(data, nextIndexSplit);

        // Recursively build the tree for each subset
        for (Map.Entry<String, ArrayList<String[]>> entry : splitData.entrySet()) {
            String value = entry.getKey();
            ArrayList<String[]> childData = entry.getValue();
            System.out.println("VALUE: " + value);
            System.out.println("Recursively building tree, remaining attributes: " + remainingAttributes);

            TreeNode childNode = buildTree(childData, remainingAttributes);
            node.children.put(value, childNode); // Add the child node
        }

        return node; // Return the root node
    }

    public static void performID3Algorithm() {
        List<String> remainingAttributes = new ArrayList<>(allAttributes);
//        AttributeSubset firstSplit = getFirstSplitAttribute(allFileData, remainingAttributes);
        TreeNode root = buildTree(allFileData, remainingAttributes);
        // Print the resulting tree
        printTree(root, "");
    }

    public static void main(String[] args) {
        String filename = args[0];
        storeFileData(filename);
        performID3Algorithm();
    }
}