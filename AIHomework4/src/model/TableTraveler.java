package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A class used for traveling a table of nodes and learning the optimal path
 * 
 * @author Jacob Slattery
 */
public class TableTraveler {	
	
	public static int SEED = 0;
	public static double EPSILON = 0.0;
	
	private NodeTable map;
	private Random random;
	
	private ArrayList<Integer> cliffsLocations;
	private int startLocation;
	private int goalLocation;
	
	/**
	 * Creates a traveler and a NodeTable of the given height and width that has a starting location and goal location
	 * 
	 * @param start The starting index
	 * @param goal The goal index
	 * @param cliffs The a map containing the index locations and values of Nodes with values great than default
	 * @param width The width of the board
	 * @param height The height of the board
	 */
	public TableTraveler(int start, int goal, HashMap<Integer,Integer> cliffs, int width, int height) {
		this.startLocation = start;
		this.goalLocation = goal;
		this.cliffsLocations = new ArrayList<Integer>();
		for (Integer current : cliffs.keySet())
		{
			if (cliffs.get(current) <= NodeTable.CLIFF_REWARD)
			{
				cliffsLocations.add(current);
			}
		}
		this.map = new NodeTable(width, height, cliffs, goal);
		this.random = new Random(SEED);
	}
	
	/**
	 * Tests if the current board leads to the goal Node
	 * 
	 * @param printPath Prints the node path taken if true
	 * 
	 * @return True if a valid path to the goal was made
	 */
	public boolean findGoal(boolean printPath, int optimalStepCount) {
		if (printPath) {
			System.out.print("Node path: ");
		}
		int currentIndex = this.startLocation;
		int count = 0;
		while (currentIndex != this.goalLocation && !this.cliffsLocations.contains(currentIndex) && count <= optimalStepCount) {
			count++;
			Action chosenAction = this.map.get(currentIndex).getBestAction();
			if (printPath) { 
				System.out.print(currentIndex + " ");
			}
			currentIndex = chosenAction.getNextLocation();
		}
		if (printPath) { 
			System.out.println(currentIndex + " ");
		}
		return (currentIndex == goalLocation) && count == optimalStepCount;
		
	}
	
	public void travel() {
		this.travel(this.startLocation);
	}
	
	/**
	 * Begins moving in search of the goal node and ends when a cliff is hit or when the goal is found
	 */
	public void travel(int startingLocation) {
		int currentIndex = startingLocation;
//		System.out.print(startingLocation + " ");
		while (currentIndex != this.goalLocation && !this.cliffsLocations.contains(currentIndex)) {
			Action chosenAction = this.chooseAction(currentIndex);
//			System.out.print(chosenAction.getNextLocation() + " ");
			int nextNodeReward = this.map.get(chosenAction.getNextLocation()).getReward();
			double nextNodeMaxScore = this.map.get(chosenAction.getNextLocation()).getBestActionScore();
			
			chosenAction.calculate(nextNodeReward, nextNodeMaxScore);
			currentIndex = chosenAction.getNextLocation();
		}
//		System.out.println();
	}

	/**
	 * Chooses the action to take at the current index based on random probability with weighted options
	 * 
	 * @param currentNodeIndex The current Node location
	 * 
	 * @return The chosen action
	 */
	public Action chooseAction(int currentNodeIndex) {
		Node currentNode = this.map.get(currentNodeIndex);
		ArrayList<Action> potentialActions = currentNode.getActions();
		Action chosenAction = null;
		
		double randomValue = this.random.nextDouble();
		if (randomValue <= EPSILON)
		{
			int size = potentialActions.size();
			for (int i = 0; i < size - 1; i++) {
				int selected = this.getRandomWeightedIndex(potentialActions);
				potentialActions.remove(selected);
			}
			chosenAction = potentialActions.get(0);
		}
		else
		{
			chosenAction = currentNode.getBestAction();
		}

		return chosenAction;
	}
	
	/**
	 * Prints the values for the table
	 */
	public void printTable() {
		this.map.printTable();
	}

	private int getRandomWeightedIndex(ArrayList<Action> potentialActions) {
		
		double totalWeight = 0.0;
		
		for (Action current : potentialActions)	{
			totalWeight += Math.abs(current.getScore());
		}
		
		int randomIndex = -1;
		double random = this.random.nextDouble() * totalWeight;
		if (random != 0) {
			for (int i = 0; i < potentialActions.size(); ++i) {
				Action currentAction = potentialActions.get(i);
				double score = currentAction.getScore();
			    random -= Math.abs(score);
			    if (random <= 0.0d) {
			        randomIndex = i;
			        break;
			    }
			}
		} else {
			randomIndex = new Random().nextInt(potentialActions.size());
		}
		
		if (randomIndex == -1) {
			throw new IllegalArgumentException("Random did not work");
		}
		return randomIndex;
	}

}
