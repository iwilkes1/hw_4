package edu.jhu.cs.wilkes.cs335.assignment4.traditional;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class IdentifierProbabilities {

	private List<PriorityQueue<IdentifierProbabilityPair>> identifierData;

	public IdentifierProbabilities(List<CaseForClassification> inputData) {
		identifierData = new ArrayList<PriorityQueue<IdentifierProbabilityPair>>();
		
		for (int i = 0; i < inputData.get(0).getNumIdentifiers(); i++) {
			identifierData.add(new PriorityQueue<IdentifierProbabilityPair>());
		}
		
		boolean isThere;
		String currentIdentifierValue;
		IdentifierProbabilityPair currentPair;

		// iterate over all input data cases
		for (CaseForClassification toClassify: inputData) {
			// for each case, check all identifiers
			for (int i = 0; i < toClassify.getNumIdentifiers(); i++) {
				currentIdentifierValue = toClassify.getIdentifierAtIndex(i);
				isThere = false;
				// update all elements in the priority queue, which contains possible outputs.
				for (IdentifierProbabilityPair element : identifierData.get(i)) {
					element.inputElement(currentIdentifierValue);
					if (element.getIdentifierValue().equals(currentIdentifierValue)) {
						isThere = true;
					}
				}
				// adds the new value for that identifier.
				if (!isThere) {
					currentPair = new IdentifierProbabilityPair(currentIdentifierValue);
					identifierData.get(i).add(currentPair);
				}
			}
		}
	}

	/**
	 * Returns the identifier with the highest probability for the given index
	 * @param idIndex the index to check.
	 * @return the identifier which occurs most often.
	 */
	public String getBestIdentifier(int idIndex) {
		return identifierData.get(idIndex).peek().getIdentifierValue();
	}
	
	/** 
	 * returns the probability of the specified value of the specified index.
	 * @param idIndex the index of this identifier
	 * @param idValue the specific value of this identifier
	 * @return the probability of that value in that index.
	 */
	public double getIdentifierProbability(int idIndex, String idValue) {
		for (IdentifierProbabilityPair pair: identifierData.get(idIndex)) {
			if (pair.getIdentifierValue().equals(idValue)) {
				return pair.getProbability();
			}
		}
		return 0.0;
	}
	
	
}
