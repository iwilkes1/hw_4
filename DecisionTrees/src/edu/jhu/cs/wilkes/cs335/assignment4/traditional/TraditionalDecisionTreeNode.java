package edu.jhu.cs.wilkes.cs335.assignment4.traditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import edu.jhu.cs.wilkes.cs335.assignment4.CaseForClassification;
import edu.jhu.cs.wilkes.cs335.assignment4.DecisionTree;
import edu.jhu.cs.wilkes.cs335.assignment4.IdentifierProbabilities;

/**
 * Class which makes up the decision tree, and handles the creation
 * of the tree, as well as classification of cases.
 * @author Ian Wilkes
 *
 */
public class TraditionalDecisionTreeNode implements DecisionTree {
	
	private int decisionNumber;
	private List<Integer> remainingAttributes;
	private Map<String,TraditionalDecisionTreeNode> children;
	private TraditionalDecisionTreeNode parent;
	private List<CaseForClassification> toBeClassified;
	private String classification;
	private IdentifierProbabilities probabilities;
	
	/**
	 * Constructor for a decision tree node.  Recursively creates the
	 * remainder of the tree based on the data passed to it. 
	 * @param decisionNumber the attribute index to be used
	 * @param remainingAttributes the list containing all the other attribute indices which are unused
	 * @param parent the node which created this node
	 * @param toBeClassified the cases which reach this decision node.
	 */
	public TraditionalDecisionTreeNode(List<Integer> remainingAttributes, 
			TraditionalDecisionTreeNode parent,
			List<CaseForClassification> toBeClassified) {
		
		this.remainingAttributes = remainingAttributes;
		this.parent = parent;
		this.toBeClassified = toBeClassified;
		this.classification = "";
		
		if (classifyNode()) {
			this.children = null;
			return;
		}
		
		// figure out the children.
		List<List<CaseForClassification>> childrenSplit = pickSplitAttribute();
		this.children = new HashMap<String, TraditionalDecisionTreeNode>();
		
		List<Integer> childrenAvailableAttributes = new ArrayList<Integer>(this.remainingAttributes);
		childrenAvailableAttributes.remove(this.remainingAttributes.indexOf(this.decisionNumber));
		String attributeValue;
		// put in children nodes.
		for (int i = 0; i < childrenSplit.size(); i++) {
			attributeValue = childrenSplit.get(i).get(0).getIdentifierAtIndex(decisionNumber);
			children.put(attributeValue, 
					new TraditionalDecisionTreeNode(childrenAvailableAttributes, 
							this, childrenSplit.get(i)));
		}
	}



	/**
	 * Method used to classify a node.  Classification occurs if either
	 * all values are the same class, or if there are no remaining 
	 * attributes to try.
	 * @return true if this node has been classified, false otherwise.
	 */
	private boolean classifyNode() {
		// check if all the remaining cases to be classified are the same.
		CaseForClassification toCheck = this.toBeClassified.get(0);
		for (int i = 0; i < this.toBeClassified.size(); i++) {
			if (!toCheck.getClassification().equals(this.toBeClassified.get(i).getClassification())) {
				this.classification = null;
			}
		}
		// if they are all the same, then the classification of this node is that classification.
		if (this.classification != null) {
			this.classification = toCheck.getClassification();
			return true;
		}
		
		// classify this node if there are no more remaining attributes.
		if (remainingAttributes.isEmpty()) {
			Map<String, Integer> classCounts = new HashMap<String, Integer>();
			String curr;
			int highestCount = 0;
			for (CaseForClassification thisCase : toBeClassified) {
				curr = thisCase.getClassification();
				if (classCounts.containsKey(curr)) {
					classCounts.put(curr, classCounts.get(curr) + 1);
				} else {
					classCounts.put(curr, 1);
				}
			}
			
			for (Entry<String, Integer> entry : classCounts.entrySet()) {
				if (entry.getValue() > highestCount) {
					highestCount = entry.getValue();
					this.classification = entry.getKey();
				}
			}
			return true;
		}
		return false;
	}
	

	
	/**
	 * Gets the attribute number which is being used as the decision for this node
	 * @return the identifier index number for the attribute used at this node.
	 */
	public int getDecisionNumber() {
		return decisionNumber;
	}


	/**
	 * getter method to find the parent node
	 * @return the parent
	 */
	public TraditionalDecisionTreeNode getParent() {
		return parent;
	}


	/**
	 * Returns the list of attributes which can still be investigated
	 * @return the remainingAttributes
	 */
	public List<Integer> getRemainingAttributes() {
		return remainingAttributes;
	}


	/**
	 * Gets the list of pointers to the children of this node.
	 * @return the children
	 */
	public Map<String, TraditionalDecisionTreeNode> getChildren() {
		return children;
	}


	/**
	 * gets the list of cases which are found at this node.
	 * @return the toBeClassified
	 */
	public List<CaseForClassification> getToBeClassified() {
		return toBeClassified;
	}


	/**
	 * Method used to split the remaining cases based on a specified attribute index.  
	 * @param attributeIndex the index on which we are splitting
	 * @return a list of lists, with each element of the top level list representing a different
	 * value for the attribute index being explored, and is a list of all elements who have that value. 
	 */
	private List<List<CaseForClassification>> separateChildren(int attributeIndex) {
		List<List<CaseForClassification>> toReturn = new ArrayList<List<CaseForClassification>>();
		for (CaseForClassification child : toBeClassified) {
			int i;
			// check all the lists of classifications we have found so far.
			for (i = 0; i < toReturn.size(); i++) {
				if (child.getIdentifierAtIndex(attributeIndex).equals(
						toReturn.get(i).get(0).getIdentifierAtIndex(attributeIndex))) {
					toReturn.get(i).add(child);
					break;	
				}
			}
			// if this value of the specified index has not yet been found, put in a new list for this type.
			if (i >= toReturn.size()) {
				toReturn.add(new ArrayList<CaseForClassification>());
				toReturn.get(i).add(child);
			}
		}
		return toReturn;
	}
	
	/**
	 * Explores this attribute split, and checks the gain value, calculated from the difference
	 * between the current entropy value of the system and the entropy of the child splits.  
	 * @param separateChildren the split of all remaining cases based on one attribute.
	 * @return the gain value.
	 */
	private double calculateGain(List<List<CaseForClassification>> separateChildren) {
		return calculateCurrentEntropy() - calculateEntropyOfChildren(separateChildren);
	}
	
	/**
	 * Calculates the gain ratio for the children at a certain node.
	 * @param separateChildren the split of the children based on this attribute.
	 * @return the gain ratio
	 */
	private double gainRatio(List<List<CaseForClassification>> separateChildren) {
		return calculateGain(separateChildren)/informationValue(separateChildren);
	}

	/**
	 * Calculates the information value of an attribute which has
	 * created a given split.
	 * @param separateChildren the split
	 * @return the information value.
	 */
	private double informationValue(List<List<CaseForClassification>> separateChildren) {
		double toReturn = 0;
		double ratio;
		for (List<CaseForClassification> split: separateChildren) {
			ratio = split.size()/(1.0 * toBeClassified.size());
			toReturn -= ratio * Math.log(ratio)/Math.log(2);
		}
		return toReturn;
	}
	
	/**
	 * Calculates the current entropy of the node.
	 * @return the calculated entropy.
	 */
	private double calculateCurrentEntropy() {
		double probability = getProbabilityOfClassification(
					toBeClassified.get(0).getClassification(), 
					toBeClassified);
		if (probability == 1 || probability == 0) {
			return 0;
		} else {
			double result = probability * -1 * Math.log(probability)/Math.log(2);
			result += (1 - probability) * -1 * Math.log((1 - probability))/Math.log(2);
			return result;
		}
	}
	
	/**
	 * calculates the entropy of the system after a given split.
	 * @param childrenSplitByAttribute the list of lists of children which each have the 
	 * same value for the split attribute.  
	 * @return the entropy of the resulting split.
	 */	
	private double calculateEntropyOfChildren(List<List<CaseForClassification>> childrenSplitByAttribute) {
		double singleAttributeEntropy = 0.0;
		double totalEntropy = 0.0;
		double proportionOfCases;
		double probabilityOfAClassification;
		String aClassification = toBeClassified.get(0).getClassification();
		
		for (List<CaseForClassification> singleAttributeList : childrenSplitByAttribute) {
			proportionOfCases = singleAttributeList.size()/ (1.0 * toBeClassified.size());
			probabilityOfAClassification = getProbabilityOfClassification(aClassification, singleAttributeList);
			if (probabilityOfAClassification == 1.0 || probabilityOfAClassification == 0.0) {
				continue;
			} else {
				singleAttributeEntropy -= probabilityOfAClassification * Math.log(probabilityOfAClassification);
				singleAttributeEntropy -= (1.0 - probabilityOfAClassification) * Math.log(1.0 - probabilityOfAClassification);
				singleAttributeEntropy *= proportionOfCases;
			}
			totalEntropy += singleAttributeEntropy;
		}
		return totalEntropy;
		
	}

	/**
	 * determines the best split available, and sets the decision number to be that split.
	 * @return the split of children
	 */
	private List<List<CaseForClassification>> pickSplitAttribute() {
		List<List<CaseForClassification>> bestSplit = null;
		double bestGain = -1;
		int bestIndex = -1;
		List<List<CaseForClassification>> currentSplit;
		double currentGain;
		// checking all the splits.
		for (int idx : remainingAttributes) {
			currentSplit = separateChildren(idx);
			currentGain = gainRatio(currentSplit);
			if (currentGain > bestGain) {
				bestGain = currentGain;
				bestSplit = currentSplit;
				bestIndex = idx;
			}
		}
		this.decisionNumber = bestIndex;
		return bestSplit;
	}

	
	/**
	 * Method to get the probability of a given classification from a list of 
	 * cases to classify.
	 * @param aClassification the classification we are searching for.
	 * @param listToClassify the list of cases to check it against
	 * @return the probability of having that classification given this list.
	 */
	private double getProbabilityOfClassification(String aClassification, 
			List<CaseForClassification> listToClassify) {
		int matchCount = 0;
		for (CaseForClassification toClassify : listToClassify) {
			if (toClassify.getClassification().equals(aClassification)) {
				matchCount++;
			}
		}
		return matchCount/(1.0 * listToClassify.size());
	}

	/**
	 * method which recursively calls its children to classify a test case
	 * @param testCase the case to be classified.
	 * @return the classification of that case.
	 */
	public String classify(CaseForClassification testCase) {
		// this is a root node.
		String identifier = testCase.getIdentifierAtIndex(decisionNumber);
		if (classification != null) {
			return classification;
		}
		// this attribute for this identifier has been seen.
		if (children.containsKey(identifier)) {
			return children.get(identifier).classify(testCase);
		} else {
			// this value for this attribute split has not been seen yet.
			probabilities = new IdentifierProbabilities(this.toBeClassified);
			return children.get(probabilities.getBestIdentifier(decisionNumber)).classify(testCase);
		}
	}
	
	
}
