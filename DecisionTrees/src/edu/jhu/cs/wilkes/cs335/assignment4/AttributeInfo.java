package edu.jhu.cs.wilkes.cs335.assignment4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class used to store the various possibilities for an attribute based
 * values observed from a training set.
 * @author Ian Wilkes
 *
 */
public class AttributeInfo {

	private List<List<String>> attributeData;
	private List<String> classifications;
	
	/**
	 * Constructor for the map from identifier index to the values of that
	 * index.
	 * @param trainingList the list used to generate this data structure
	 */
	public AttributeInfo(List<CaseForClassification> trainingList) {
		attributeData = new ArrayList<List<String>>();
		classifications = new ArrayList<String>();
		CaseForClassification first = trainingList.get(0);
		// set up the map
		for (int i = 0; i < first.getNumIdentifiers(); i++) {
			attributeData.add(new ArrayList<String>());
		}
		for (CaseForClassification nextCase: trainingList) {
			for (int i = 0; i < first.getNumIdentifiers(); i++) {
				List<String> attributes = attributeData.get(i);
				String currentValue = nextCase.getIdentifierAtIndex(i);
				if (!attributes.contains(currentValue)) {
					attributes.add(currentValue);
				}
			}
			if (!classifications.contains(nextCase.getClassification())) {
				classifications.add(nextCase.getClassification());
			}
		}
		
	}
	
	/**
	 * getter method for the list of all classifications
	 * @return the list of all possible classifications
	 */
	public List<String> getClassifications() {
		return classifications;
	}
	
	
	/**
	 * Method to get a random classification.
	 * @return the random classification.
	 */
	public String getRandomClassification() {
		Random rand = new Random();
		return classifications.get(rand.nextInt(classifications.size()));
	}
	
	/**
	 * getter for all the possibilities for one attribute.
	 * @param index the attribute index to check.
	 * @return the list of all possibilities for that attribute.
	 */
	public List<String> getPossibilities(int index) {
		return attributeData.get(index);
	}
	
	/**
	 * gets a random attribute value for the specified index
	 * @param index the index to check
	 * @return the attribute value
	 */
	public String getRandomFromIndex(int index) {
		Random rand = new Random();
		List<String> possibilities = attributeData.get(index);
		return possibilities.get(rand.nextInt(possibilities.size()));
	}
	
	/**
	 * Gets an attribute for a given attribute index, and a given value number
	 * @param index
	 * @param valNumber
	 * @return the attribute value of that index and value number 
	 */
	public String getAttributeAtIndex(int index, int valNumber) {
		return attributeData.get(index).get(valNumber);
	}
	
	
	
	/**
	 * getter for the number of possibilities for one attribute.
	 * @param index the index of the attribute to be explored
	 * @return the number of values of that attribute
	 */
	public int getNumValuesAtIndex(int index) {
		return attributeData.get(index).size();
	}

	/**
	 * gets the number of attributes
	 * @return the number of attributes.
	 */
	public int getNumAttributes() {
		return attributeData.size();
	}

	/**
	 * Gets the index number of the value specified for the top level index
	 * @param attributeIndex the top level attribute number
	 * @param attributeValue the specific value at that top level index
	 * @return the index of that attribute value for that attribute number.
	 */
	public int getValueIndex(int attributeIndex, String attributeValue) {
		return attributeData.get(attributeIndex).lastIndexOf(attributeValue);
	}
	
}
