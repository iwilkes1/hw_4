package edu.jhu.cs.wilkes.cs335.assignment4;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.jhu.cs.wilkes.cs335.assignment4.genetic.GeneticTree;
import edu.jhu.cs.wilkes.cs335.assignment4.genetic.GeneticTreeNode;
import edu.jhu.cs.wilkes.cs335.assignment4.traditional.TraditionalDecisionTreeNode;

/**
 * Main class for decision tree classification.
 * @author Ian Wilkes
 *
 */
public class DecisionTreeClassifier {

	/**
	 * Main method for the decision tree classifier.
	 * @param args the command line arguments to be used.  
	 * The first argument should be a path to the training data, and the second
	 * the path to the testing data.
	 */
	public static void main(String[] args) {
		// checking the number of arguments
		if (args.length != 3) {
			System.err.println("Error: incorrect number of arguments.");
			System.out.println("Usage: DecisionTreeClassifier <path to training data>"
					+ " <path to testing data> <0 for traditional, 1 for genetic>");
			System.exit(1);
		}
	
		List<CaseForClassification> trainingData = null;
		List<CaseForClassification> testingData = null;
		
		try {
			trainingData = parseDataFile(args[0]);
		} catch (FileNotFoundException e) {
			System.err.println("Error: data could not be found for file: \n" + args[0]);
			System.exit(1);
		}
		try {
			testingData = parseDataFile(args[1]);
		} catch (FileNotFoundException e) {
			System.err.println("Error: data could not be found for file: \n" + args[1]);
			System.exit(1);
		}
	
		List<Integer> identifierIndices = new ArrayList<Integer>();
		for (int i = 0;	i < trainingData.get(0).getNumIdentifiers(); i++) {
			identifierIndices.add(i);
		}
		
		DecisionTree classificationTree;
		String positiveClassification = testingData.get(0).getClassification();
		
		long start = System.currentTimeMillis();
		if (Integer.parseInt(args[2]) == 0) {
			classificationTree = new TraditionalDecisionTreeNode(identifierIndices, null, 
					trainingData);
		} else if (Integer.parseInt(args[2]) == 1) {
			classificationTree = new GeneticTree(trainingData, positiveClassification);
		} else {
			classificationTree = null;
			System.err.println("Invalid type of search tree.  Must be either 0 for traditional or 1 for genetic.");
			System.exit(1);
			
		}
		long endTraining = System.currentTimeMillis();
		
		System.out.println("training finished in " + (endTraining - start) + " ms");
		System.out.println("Classifying training set");
<<<<<<< HEAD
		
		//System.out.print((endTraining - start) + ",");
=======
		*/
		System.out.println();
		System.out.print((endTraining - start) + ",");
>>>>>>> af29c5c5e87ecf416a80aae58a1aa582dba914ea
		classifyDataSet(trainingData, classificationTree, positiveClassification);
		long endTrainingTest = System.currentTimeMillis();
		//System.out.print(endTrainingTest - endTraining + ",");
		
		System.out.println("classification of training set took: " + 
				(endTrainingTest - endTraining) + " ms");
		System.out.println("Classifying test set");
		
		classifyDataSet(testingData, classificationTree, positiveClassification);
	
		long endTime = System.currentTimeMillis();
<<<<<<< HEAD
		System.out.println("classification of test set took: " + (endTime - endTrainingTest) + " ms");
		//System.out.print(endTime - endTrainingTest);

=======
		//System.out.println("classification of test set took: " + (endTime - endTrainingTest) + " ms");
		System.out.println(endTime - endTrainingTest);
		
		/*
		//TODO remove unnecessary print
		for (CaseForClassification element : trainingData) {
			System.out.println(element);
		}
		*/
>>>>>>> af29c5c5e87ecf416a80aae58a1aa582dba914ea
	
	}

	/**
	 * Performs classification on a set of cases to classify, the classification tree, and a positive 
	 * classification.
	 * @param testingData the data to classify
	 * @param classificationTree the pre-built decision tree
	 * @param positiveClassification an example of a positive classification.
	 */
	private static void classifyDataSet(List<CaseForClassification> testingData, DecisionTree classificationTree, String positiveClassification) {
		String classification;
		int falsePositives = 0;
		int falseNegatives = 0;
		int truePositives = 0;
		int trueNegatives = 0;
		
		for (CaseForClassification testCase : testingData) {
			classification = classificationTree.classify(testCase);
			if (testCase.getClassification().equals(positiveClassification)) {
				if (classification.equals(testCase.getClassification())) {
					truePositives++;
				} else {
					falseNegatives++;
				}
			} else {
				if (classification.equals(testCase.getClassification())) {
					trueNegatives++;
				} else {
					falsePositives++;
				}
			}
		}
		
		double accuracy = (truePositives + trueNegatives)/ (1.0 * (truePositives + trueNegatives + falsePositives + falseNegatives));
		double precision = truePositives/(1.0 * truePositives + falsePositives);
		double recall = truePositives/(1.0 * truePositives + falseNegatives);
		//System.out.print(accuracy + "," + precision + "," + recall + ",");
				
		System.out.println("fp: " + falsePositives + " fn: " + falseNegatives
				+ " tp: " + truePositives + " tn:" + trueNegatives);
		System.out.println("test data size: " + testingData.size());
		System.out.println("accuracy: " + accuracy);
		System.out.println("precision: " + precision);
		System.out.println("recall: " +  recall);
		
	}

	/**
	 * Function to parse the input data into individual cases to be classified
	 * Works on either space separated data or comma separated data.
	 * @param fileToRead the file to be parsed
	 * @return the list of cases
	 * @throws FileNotFoundException thrown if the input file cannot be found
	 */
	private static List<CaseForClassification> parseDataFile(String fileToRead)
			throws FileNotFoundException {
				Scanner reader = new Scanner(new FileReader(fileToRead));
				List<CaseForClassification> toReturn = new ArrayList<CaseForClassification>();
				List<String> identifiers;
				String currentLine;
				
				while(reader.hasNextLine()) {
					currentLine = reader.nextLine();
					if (currentLine.contains(",")) {
						identifiers = Arrays.asList(currentLine.split(","));
					} else {
						identifiers = Arrays.asList(currentLine.split(" "));	
					}
					toReturn.add(new CaseForClassification(identifiers.get(0), identifiers.subList(1, identifiers.size())));
				}
				if (toReturn.isEmpty()) {
					System.out.println("Error: data file is null");
					return null;
				}
				return toReturn;
			}

<<<<<<< HEAD
}
=======
	public DecisionTreeClassifier() {
		super();
	}

}
>>>>>>> af29c5c5e87ecf416a80aae58a1aa582dba914ea
