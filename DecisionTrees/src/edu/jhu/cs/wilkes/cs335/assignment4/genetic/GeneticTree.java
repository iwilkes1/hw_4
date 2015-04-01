package edu.jhu.cs.wilkes.cs335.assignment4.genetic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import edu.jhu.cs.wilkes.cs335.assignment4.AttributeInfo;
import edu.jhu.cs.wilkes.cs335.assignment4.CaseForClassification;
import edu.jhu.cs.wilkes.cs335.assignment4.DecisionTree;

/** TODO
 * Genetic Tree wrapper for the individual nodes.  Will handle the 
 * selection and training of the tree. 
 * @author Ian Wilkes
 *
 */
public class GeneticTree implements DecisionTree {

	private static final double TRAINING_PROPORTION = 1.0; // 1.0
	private static final int POPULATION = 100;//100
	private static final int ITERATIONS = 100;//100
	private static final double CROSSOVER_PROBABILITY = 0.70;//0.7
	private static final double MUTATION_PROBABILITY = 0.01; //.1
	private static final int CHANGE_CUTOFF = 10;//10

	private List<GeneticTreeNode> roots;
	private List<CaseForClassification> trainingData;

	private GeneticTreeNode bestTree;
	private Double bestFitness = null;
	private Random rand;
	private String positiveClassification;
	private static int sinceLastChange = 0;
	
	private AttributeInfo attributes;
	/**
	 * Constructor for the Genetic tree.  Can set up a tree from the root and work down.
	 * @param trainingData the training data.
	 */
	public GeneticTree(List<CaseForClassification> trainingData, String positiveClassification) {
		this.trainingData = trainingData;
		this.positiveClassification = positiveClassification;

		attributes = new AttributeInfo(trainingData);
		rand = new Random();
		roots = new ArrayList<GeneticTreeNode>();
		for (int i = 0; i < POPULATION; i++) {
			roots.add(new GeneticTreeNode(attributes, 0, -1, null, null));
		}
		for (int i = 0; i < ITERATIONS; i++) {
			updateTrees();
			sinceLastChange++;
			if (sinceLastChange > CHANGE_CUTOFF) {
				break;
			}
		}

	}

	/**
	 * Method which creates the next set of trees.
	 * This is currently done by taking the population and evaluating
	 * their fitness.  Once all trees are evaluated, they are
	 * iterated over and a set of nodes equal to the population
	 * size is selected, starting with the best nodes so far.
	 * These then have the chance to have crossover, or to mutate, or both or neither.
	 * Finally, these are put back into the set of nodes to be used in the
	 * next iteration. Additional nodes are added to the set of new nodes
	 * for each node which goes through crossover.
	 */
	private void updateTrees() {
		List<Double> fitnesses = new ArrayList<Double>();
		PriorityQueue<GeneticTreeNode> orderedRoots = new PriorityQueue<GeneticTreeNode>();
		double currFitness;
		double totalFitness = 0;
		for (GeneticTreeNode root : roots) {
			currFitness = getFitness(root);
			totalFitness += currFitness;
			root.setFitness(currFitness);
			fitnesses.add(currFitness);
			if (!Double.isNaN(currFitness) && (bestFitness == null || bestFitness < currFitness)) {
				bestFitness = currFitness;
				bestTree = new GeneticTreeNode(root, null);
				sinceLastChange = 0;
			}
			orderedRoots.add(root);
		}
		
		List<GeneticTreeNode> nextRoots = new ArrayList<GeneticTreeNode>();
		GeneticTreeNode currTree;
		while(nextRoots.size() < POPULATION) {
			//TODO figure out a succession strategy. Currently using rank
			currTree = orderedRoots.poll();
			if (currTree == null) {
				currTree = new GeneticTreeNode(attributes, 0, -1, null, null);
			}
			if (rand.nextDouble() < CROSSOVER_PROBABILITY) {
				performCrossover(orderedRoots, currTree);
			}

			if(rand.nextDouble() < MUTATION_PROBABILITY) {
				currTree.mutate();
			}

			nextRoots.add(currTree);
		}
		roots = nextRoots;
		
	}

	/**
	 * Performs crossover between two trees.  The first input is the priority queue
	 * of trees currently in the population and is used to produce another individual
	 * to cross over with.  When an individual is found, each tree finds a random node
	 * to swap to the other tree.  Once a node is found, the parent of each swap nodes
	 * is found, and the nodes which are to be swapped are swapped. The second node
	 * is discarded at the end of the process.  
	 * @param orderedRoots the priority queue containing 
	 * @param currTree
	 */
	private void performCrossover(PriorityQueue<GeneticTreeNode> orderedRoots,
			GeneticTreeNode currTree) {
		// pull a random node from the priority queue
		GeneticTreeNode otherTree = orderedRoots.peek();
		

		if (otherTree != null) {
			Iterator<GeneticTreeNode> iter = orderedRoots.iterator();
			for (int i = 0; i < rand.nextInt(orderedRoots.size()); i++) {
				otherTree = iter.next();
			}
			orderedRoots.remove(otherTree);
			GeneticTreeNode thisToSwap = currTree.getRandomNode();
			GeneticTreeNode otherToSwap = otherTree.getRandomNode();
			GeneticTreeNode thisParent = thisToSwap.getParent();

			otherToSwap.setParent(thisParent);
			thisToSwap.setParent(null);
			if (thisParent != null) {
				thisParent.replaceChild(thisToSwap, otherToSwap);
			}
		}
	}

	@Override
	public String classify(CaseForClassification nextCase) {
		return bestTree.getLeafForCase(nextCase).getClassification();
	}

	/**
	 * Function to calculate the fitness of a given genetic tree starting at root. 
	 * @param root the root of the tree to evaluate.
	 * @return the fitness of this tree.
	 */
	private double getFitness(GeneticTreeNode root) {
		String classification;
		int falsePositives = 0;
		int falseNegatives = 0;
		int truePositives = 0;
		int trueNegatives = 0;
		int totalDepth = 0;
		GeneticTreeNode leaf;

		for (CaseForClassification testCase : trainingData) {
			if (rand.nextDouble() > TRAINING_PROPORTION) {
				continue;
			}
			leaf = root.getLeafForCase(testCase);
			totalDepth += leaf.getDepth();
			classification = leaf.getClassification();
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
		//TODO insert other metrics here.
	
	return getAccuracy(truePositives, trueNegatives, falsePositives, falseNegatives)
				+getPrecision(truePositives, falsePositives) + getRecall(truePositives, falseNegatives)
				- totalDepth/(10. * trainingData.size() * TRAINING_PROPORTION);

	}


	/**
	 * Calculates the accuracy from this classification
	 * @param truePositives the number of correctly classified positives
	 * @param trueNegatives the number of correctly classified negatives
	 * @param falsePositives the number of negatives classified as positive
	 * @param falseNegatives the number of positives classified as negative
	 * @return the accuracy
	 */
	private double getAccuracy(int truePositives, int trueNegatives, int falsePositives, int falseNegatives) {
		return (truePositives + trueNegatives)/ (1.0 * (truePositives + trueNegatives + falsePositives + falseNegatives));
	}

	/**
	 * Calculates the recall of the data
	 * @param truePositives the number of true positives from this run
	 * @param falseNegatives the number of false negatives from this run
	 * @return the recall
	 */
	private double getRecall(int truePositives, int falseNegatives) {
		return truePositives/(1.0 * truePositives + falseNegatives);
	}

	/**
	 * Calculates the precision of this classification run
	 * @param truePositives the number of true positives
	 * @param falsePositives the number of false positives from this data.
	 * @return the precision of this classification.
	 */
	private double getPrecision(int truePositives, int falsePositives) {
		return truePositives/(1.0 * truePositives + falsePositives);
	}

}
