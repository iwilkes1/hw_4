package edu.jhu.cs.wilkes.cs335.assignment4;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to be used as individual entries from the data.
 * @author Ian Wilkes
 *
 */
public class CaseForClassification {

	private String classification;
	private List<String> identifiers;
	
	public CaseForClassification(String classification, List<String> identifiers) {
		this.classification = classification;
		this.identifiers = new ArrayList<String>();
		for (String s: identifiers) {
			this.identifiers.add(s);
		}
	}
	
	/**
	 * Getter method to get the classification of this object
	 * @return the classification of the object
	 */
	public String getClassification() {
		return classification;
	}
	
	/**
	 * gets the value of the identifier at a specific index.
	 * @param identifierIndex the index of the parameter to be checked
	 * @return the value of the identifier at the specified index
	 */
	public String getIdentifierAtIndex(int identifierIndex) {
		if (identifierIndex >= identifiers.size()) {
			throw new IndexOutOfBoundsException();
		}
		return identifiers.get(identifierIndex);
	}
	
	/**
	 * Getter to find the number of identifiers for this case.
	 * @return the number of identifiers, not including the classification.
	 */
	public int getNumIdentifiers() {
		return identifiers.size();
	}
	
	/**
	 * getter method for the identifiers
	 * @return the identifiers
	 */
	public List<String> getIdentifiers() {
		return this.identifiers;
	}
	
	/**
	 * Returns the string representation of a given case for classification.
	 */
	public String toString() {
		return "classification: " + classification + " identifiers: " + identifiers.toString();
	}
	
}
