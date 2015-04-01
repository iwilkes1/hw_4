package edu.jhu.cs.wilkes.cs335.assignment4;


public interface DecisionTree {

	/**
	 * Classify a case based on this tree.
	 * @param nextCase the case to be identified
	 * @return the classification based on the tree.
	 */
	public abstract String classify(CaseForClassification nextCase);

}