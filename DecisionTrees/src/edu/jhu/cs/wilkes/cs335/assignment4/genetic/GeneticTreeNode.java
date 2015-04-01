package edu.jhu.cs.wilkes.cs335.assignment4.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.jhu.cs.wilkes.cs335.assignment4.AttributeInfo;
import edu.jhu.cs.wilkes.cs335.assignment4.CaseForClassification;

/**
 * Node for use in the genetic decision tree.
 * @author Ian Wilkes
 *
 */
public class GeneticTreeNode implements Comparable<GeneticTreeNode>{

	private static final double LEAF_PROBABILITY = .2;
	private static final int MIN_DEPTH = 2;
	private static final double DEEPENING_PROBABILITY = 0.3;
	private static final int MAX_ATTEMPTS = 10;

	private  AttributeInfo map = null;
	private int splitAttributeIndex;
	private int parentIndex;
	private String attributeValue;
	private List<GeneticTreeNode> children;
	private String classification = null;
	private GeneticTreeNode parent = null;
	private int depth;

	private double fitness;

	/**
	 * Constructor for a Genetic tree node.  This constructor will
	 * create a random tree based on the training information stored
	 * in attributes
	 * @param attributes this describes the available attributes and their values
	 * @param depth the current depth in the tree of this node.
	 * @param parentIndex the switch variable of the parent
	 * @param attributeValue the current value associated with this node
	 * @param parent the parent of this node
	 */
	public GeneticTreeNode(AttributeInfo attributes, int depth, 
			int parentIndex, String attributeValue, GeneticTreeNode parent) {
		this.map = attributes;
		this.depth = depth;
		this.parentIndex = parentIndex;
		this.attributeValue = attributeValue;
		this.parent = parent;
		Random rand = new Random();
		double observedValue = rand.nextDouble();
		// this node is now a leaf. 
		if ((observedValue < LEAF_PROBABILITY && depth > MIN_DEPTH) ||
				(observedValue - depth/(1.0 * map.getNumAttributes()) < LEAF_PROBABILITY)) {
			children = null;
			classification = map.getRandomClassification();
			return;
			// node is an interior node
		} else {
			children = new ArrayList<GeneticTreeNode>();
			String nextValue;
			this.splitAttributeIndex = rand.nextInt(map.getNumAttributes()); 
			// generate the children.
			for (int i = 0; i < map.getNumValuesAtIndex(splitAttributeIndex); i++) {
				nextValue = map.getPossibilities(splitAttributeIndex).get(i);
				children.add(new GeneticTreeNode(attributes, depth + 1, splitAttributeIndex, nextValue, this));
			}
		}
	}

	/**
	 * Copy constructor.  Produces a tree which matches the current tree.
	 * @param toCopy the tree to copy recursively
	 * @param parent the parent of this node.
	 */
	public GeneticTreeNode(GeneticTreeNode toCopy, GeneticTreeNode parent) {
		this.map = toCopy.map;
		this.splitAttributeIndex = toCopy.splitAttributeIndex;
		this.parentIndex = toCopy.parentIndex;
		this.parent = parent;
		this.attributeValue = toCopy.attributeValue;
		this.classification = toCopy.classification;
		this.depth = toCopy.depth;
		this.fitness = toCopy.fitness;
		if (toCopy.children == null) {
			this.children = null;
		} else {
			this.children = new ArrayList<GeneticTreeNode>();
			for (GeneticTreeNode child : toCopy.children) {
				this.children.add(new GeneticTreeNode(child, this));
			}
		}
	}


	/**
	 * Classifies a case.  While traversing the tree, 
	 * if the value of this case is not one of the options of the split,
	 * then a random child is picked to continue down the tree.
	 * @param nextCase the case to classify
	 * @return the classification of this case.
	 */
	public GeneticTreeNode getLeafForCase(CaseForClassification nextCase) {
		if (classification != null) {
			return this;
		}

		// mappings don't work after crossover and mutation
		Random rand = new Random();
		for (GeneticTreeNode child: children) {
			if (child.parentIndex > 0 && nextCase.getIdentifierAtIndex(
					child.parentIndex).equals(child.getAttributeValue())) {
				return child.getLeafForCase(nextCase);
			}
		}

		// if you don't find the value associated with this child, pick a random value.
		int childIndex = rand.nextInt(children.size());
		return children.get(childIndex).getLeafForCase(nextCase);
	}


	/**
	 * getter method for the classification of a node.
	 * @return the classification.
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @return the fitness
	 */
	public double getFitness() {
		return fitness;
	}


	/**
	 * @param fitness the fitness to set
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * Getter method for the parent of the current node.
	 * @return the parent.
	 */
	public GeneticTreeNode getParent() {
		return this.parent;
	}

	/**
	 * CompareTo function.  Uses the fitness of this node.
	 * @param other the other node to be compared to
	 * @return 1 if this node is worse than the other, 0 if equal, and 1 otherwise.
	 */
	public int compareTo(GeneticTreeNode other) {
		if (this.fitness > other.getFitness()) {
			return -1;
		} else if (this.fitness == other.getFitness()) {
			return 0;
		} else {
			return 1;
		}
	}


	/**
	 * Getter to determine the depth of the leaf node used to classify a value.
	 * @return the depth of this node.
	 */
	public int getDepth() {
		return depth;
	}


	/**
	 * Sets replaces a pointer in the children array with the new pointer.
	 * @param oldChild the child to be replaced
	 * @param newChild the new child
	 */
	public void replaceChild(GeneticTreeNode oldChild, GeneticTreeNode newChild) {
		if (children != null) {
			int index = children.indexOf(oldChild);
			children.remove(index);
			children.add(index, newChild);
		}
	}

	/**
	 * Finds a random node to be swapped. Recursively searches down the tree,
	 * until either a leaf is reached, or a probability is reached.
	 * @return the node to be swapped.
	 */
	public GeneticTreeNode getRandomNode() {
		Random rand = new Random();
		if (this.children == null || rand.nextDouble() < DEEPENING_PROBABILITY) {
			return this;
		} else {
			return children.get(rand.nextInt(map.getNumValuesAtIndex(splitAttributeIndex)));
		}
	}


	/**
	 * setter method for the parent node.
	 * @param newParent the new parent of this node.
	 */
	public void setParent(GeneticTreeNode newParent) {
		this.parent = newParent;

	}


	/**
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * returns the children of this node.
	 * @return the children
	 */
	public List<GeneticTreeNode> getChildren() {
		return this.children;
	}

	/**
	 * @return the parent index of this node
	 */
	public int getParentIndex() {
		return this.parentIndex;
	}


	/**
	 * Picks a random node in the tree, and then switches the 
	 * spliting attribute with a random other attribute with the same
	 * number of possible outcomes for internal nodes. If the random node is
	 * a leaf node, it gets a random classification. 
	 */
	public void mutate() {
		Random rand = new Random();
		boolean mutated = false;
		int attempts = 0;
		while (!mutated && attempts < MAX_ATTEMPTS) {
			GeneticTreeNode toMutate = getRandomNode();
			attempts++;
			if (toMutate.children == null) {
				toMutate.classification = map.getRandomClassification();
				mutated = true;
			} else {
				int oldSplit = toMutate.splitAttributeIndex;
				List<Integer> acceptableAttributes = new ArrayList<Integer>();
				// determine possible values to replace this one with
				for (int i = 0; i < map.getNumAttributes(); i++) {
					if (map.getNumValuesAtIndex(i) == children.size() && i != oldSplit) {
						acceptableAttributes.add(i);
					}
				}
				if (!acceptableAttributes.isEmpty()) {
					// change the split index
					toMutate.splitAttributeIndex = acceptableAttributes.get(rand.nextInt(acceptableAttributes.size()));
					mutated = true;
				}
			}
		}
	}

}
