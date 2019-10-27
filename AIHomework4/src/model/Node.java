package model;

import java.util.ArrayList;

/**
 * An object representing a single block on the board, holding an array of potential moves along with the current space's reward value
 * 
 * @author Jacob Slattery
 */
public class Node {
	
	/**
	 * Holds the potential indexes of action directions
	 */
	public enum Directions { LEFT_EDGE, TOP_EDGE, RIGHT_EDGE, BOTTOM_EDGE }
	
	private Action[] actions = new Action[4];
	private int reward;
	
	/**
	 * Creates a node and assigns it a currentIndex along with a reward
	 * 
	 * @param reward The reward value
	 */
	public Node(int reward) {
		this.reward = reward;
	}
	

	/**
	 * Sets the action at the given direction
	 * 
	 * @param direction The direction this action is stored
	 * @param currentLocation The starting action location
	 * @param actionNextLocation The ending location for the action
	 */
	public void setAction(Directions direction, int currentLocation, int actionNextLocation) {
		this.actions[direction.ordinal()] = new Action(currentLocation, actionNextLocation);
	}
	
	/**
	 * Gets the action stored at the given direction for this Node
	 * 
	 * @param direction The direction the action is moving
	 * @return An action
	 */
	public Action getActionAt(Directions direction) {
		Action selectedNode = this.actions[direction.ordinal()];
		return selectedNode;
	}
	
	/**
	 * Gets a list of all potential actions at this Node
	 * 
	 * @return potential actions
	 */
	public ArrayList<Action> getActions() {
		ArrayList<Action> potentials = new ArrayList<Action>();
		for (Action current : this.actions) {
			if (current != null) {
				potentials.add(current);
			}
		}
		return potentials;
	}

	/**
	 * Gets the score of this Node's best action
	 * 
	 * @return The best action score
	 */
	public double getBestActionScore() {
		double max = Double.NEGATIVE_INFINITY;
		for (Action current : this.actions) {
			if (current != null) {
				double currentScore = current.getScore();
				if (currentScore > max) {
					max = currentScore;
				}
			}
		}
		return max;
	}
	
	
	/**
	 * Gets the action with the best score for this Node
	 * 
	 * @return The best action
	 */
	public Action getBestAction() {
		Action best = null;
		for (Action current : this.actions) {
			if (current != null) {
				double currentScore = current.getScore();
				if (best == null || currentScore > best.getScore()) {
					best = current;
				}
			}
		}
		return best;
	}
	
	/**
	 * Sets the reward value for the Node
	 * 
	 * @param reward The new reward value
	 */
	public void setReward(int reward) {
		this.reward = reward;
	}
	
	/**
	 * Gets the Node's reward value
	 * 
	 * @return the reward value
	 */
	public int getReward() {
		return this.reward;
	}
	
}
