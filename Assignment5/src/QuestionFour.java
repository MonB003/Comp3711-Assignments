import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Class that represents an attribute in the data file, such as Math or Science.
 */
class AttributeSubset {
    private final String attributeName;
    private final double informationGain;  // Information gain or entropy for child subset

    public AttributeSubset(String attributeName, double informationGain) {
        this.attributeName = attributeName;
        this.informationGain = informationGain;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public double getInformationGain() {
        return informationGain;
    }
}

/**
 * Class that represents a node in the decision tree.
 */
class TreeNode {
    String attribute; // The attribute used to split data
    Map<String, TreeNode> children; // Children nodes, key is attribute value
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

    public static double calculateSubsetEntropy(ArrayList<String[]> subsetData) {
        // Store the index of the boolean attribute
        int booleanIndex = attributesIndices.size() - 1;
        // Count occurrences of "Yes" and "No"
        int yesValueCount = 0;
        int noValueCount = 0;
        double totalValues = subsetData.size();

        // Loop through all data to count yes and no occurrences
        for (String[] currentData : subsetData) {
            String booleanValue = currentData[booleanIndex];  // Assuming booleanValue is the last element in the record
            if (booleanValue.equalsIgnoreCase("yes")) {
                yesValueCount++;
            } else {
                noValueCount++;
            }
        }

        // Calculate probabilities based on counts
        double yesFraction = yesValueCount / totalValues;
        double noFraction = noValueCount / totalValues;
        System.out.println("Yes fraction = " + yesValueCount + "/" + totalValues);
        System.out.println("No fraction = " + noValueCount + "/" + totalValues);

        // Calculate child entropy: H(X) = −p(x1)log2 p(x1) −p(x2)log2 p(x2) ... −p(xn)log2 p(xn)
        double entropy = 0.0; // Base case: if fraction is 0, entropy is 0
        if (yesFraction > 0) {
            entropy -= yesFraction * Math.log(yesFraction) / Math.log(2);
        }
        if (noFraction > 0) {
            entropy -= noFraction * Math.log(noFraction) / Math.log(2);
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
        System.out.println("\n--- Finding highest information gain ---");
        AttributeSubset highestSubset = subsets.getFirst();
        double highestInformationGain = highestSubset.getInformationGain();

        // Loop through all attributes
        for (AttributeSubset currentSubset : subsets) {
            System.out.println("Information gain of " + currentSubset.getAttributeName() + ": " + currentSubset.getInformationGain());
            if (currentSubset.getInformationGain() > highestInformationGain) {
                // Store new highest subset
                highestInformationGain = currentSubset.getInformationGain();
                highestSubset = currentSubset;
            }
        }

        System.out.println("Highest information gain is " + highestSubset.getAttributeName() + ": " + highestInformationGain + "\n");
        // Return the subset with the highest information gain, so it can become the next parent entropy
        return highestSubset;
    }

    // Method to calculate information gain for an attribute
    public static double calculateInformationGain(ArrayList<String[]> data, int attributeIndex) {
        System.out.println("Attribute: " + allAttributes.get(attributeIndex));
        double parentEntropy = calculateSubsetEntropy(data);
        System.out.println("Parent Entropy: " + parentEntropy);

        // Split data based on attribute
        HashMap<String, ArrayList<String[]>> subsets = splitSubsetData(data, attributeIndex);

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
        }

        // Calculate average entropy and information gain
        double averageSubsetEntropy = calculateAverageChildrenEntropy(sizeEntropyPairs, totalEntries);
        return parentEntropy - averageSubsetEntropy; // Return information gain
    }

    // Main method to calculate parent entropy and find the best attribute to split on
    public static AttributeSubset getNextSplitAttribute(ArrayList<String[]> subsetData, List<String> remainingAttributes) {
        ArrayList<AttributeSubset> parentEntropyOptions = new ArrayList<>();

        // Loop through remaining attributes to calculate information gain
        for (String attribute : remainingAttributes) {
            int attributeIndex = getAttributeIndex(attribute);
            if (attribute.equals(allAttributes.getLast())) {
                System.out.println("SKIPPING " + attribute);
                continue; // Skip the boolean attribute
            }

            // Calculate the information gain for this attribute based on the subset data
            double informationGain = calculateInformationGain(subsetData, attributeIndex);
            System.out.println("Information Gain for " + attribute + ": " + informationGain + "\n");

            // Store the result for this attribute
            AttributeSubset currentSubset = new AttributeSubset(attribute, informationGain);
            parentEntropyOptions.add(currentSubset);
        }

        // Return the best subset with the highest information gain
        return getHighestInformationGain(parentEntropyOptions);
    }

    private static String getClassLabel(ArrayList<String[]> data) {
        String classLabel = null;

        // Check if all examples have the same class label
        for (String[] record : data) {
            String currentLabel = record[record.length - 1];
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
        if (attributes.isEmpty()) {
            String majorityClass = getMajorityClass(data);
            return new TreeNode(majorityClass, true); // Leaf node
        }

        // Calculate the best attribute to split on
        AttributeSubset bestSubset = getNextSplitAttribute(data, attributes);

        // Create a new TreeNode with the best attribute
        TreeNode node = new TreeNode(bestSubset.getAttributeName());

        // Recursively split the data and add children nodes
        List<String> remainingAttributes = new ArrayList<>(attributes);
        remainingAttributes.remove(bestSubset.getAttributeName());

        System.out.println("--- Splitting on " + bestSubset.getAttributeName() + " ---");
        int nextIndexSplit = getAttributeIndex(bestSubset.getAttributeName());
        HashMap<String, ArrayList<String[]>> splitData = splitSubsetData(data, nextIndexSplit);

        // Recursively build the tree for each subset
        for (Map.Entry<String, ArrayList<String[]>> entry : splitData.entrySet()) {
            String value = entry.getKey();
            ArrayList<String[]> childData = entry.getValue();
            TreeNode childNode = buildTree(childData, remainingAttributes);
            node.children.put(value, childNode); // Add the child node
        }

        return node; // Return the root node
    }

    public static void performID3Algorithm() {
        List<String> remainingAttributes = new ArrayList<>(allAttributes);
        TreeNode root = buildTree(allFileData, remainingAttributes);
        // Print the resulting tree
        System.out.println("\nDecision tree result:");
        printTree(root, "");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: One argument (data filename) must be passed to the program. Number of arguments passed: " + args.length);
            return;
        }
        String filename = args[0];
        storeFileData(filename);
        performID3Algorithm();
    }
}