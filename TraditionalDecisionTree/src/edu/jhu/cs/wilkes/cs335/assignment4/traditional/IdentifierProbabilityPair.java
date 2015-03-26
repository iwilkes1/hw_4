package edu.jhu.cs.wilkes.cs335.assignment4.traditional;

public class IdentifierProbabilityPair implements Comparable<IdentifierProbabilityPair>{

	private String identifierValue;
	private int occurrences; 
	private int numTotalOccurrences;
	private double probability;
	
	/**
	 * Constructor for an IdentifierProbabilityPair, gives the string value, and 
	 * keeps track of the probability of that string.
	 * @param identifierValue
	 */
	public IdentifierProbabilityPair(String identifierValue) {
		this.identifierValue = identifierValue;
		this.probability = 0.0;
		this.numTotalOccurrences = 0;
		this.occurrences = 0;
		inputElement(identifierValue);
	}
	
	
	/**
	 * reads an element and if it matches this value, the total number of occurrences
	 * of this value is updated.  the total number is always updated.
	 * the probability that a case will have this identifier value is specified.
	 * @param idValue the value for the added identifier.
	 */
	public void inputElement(String idValue) {
		numTotalOccurrences++;
		if (identifierValue.equals(idValue)) {
			occurrences++;
		}
		probability = occurrences / (1.0 * numTotalOccurrences);
	}
	
	/**
	 * getter for the identifier value
	 * @return the identifierValue
	 */
	public String getIdentifierValue() {
		return identifierValue;
	}
	
	
	/**
	 * getter for the probability of this value.
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}
	
	@Override
	public int compareTo(IdentifierProbabilityPair other) {
		// we want the larger probabilities first in the priority queue.
		if (this.probability > other.probability) {
			return -1;
		} else if (this.probability < other.probability) {
			return 1;
		} else {
			return 0;
		}
	}

	
}
