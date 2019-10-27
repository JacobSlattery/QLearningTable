package model;

/**
 * Class that holds an action, which is a step from one node to the next and contains the score for the move
 * 
 * @author Jacob Slattery
 */
public class Action {
	
	public static double ALPHA = 0.6;
	public static double GAMMA = 0.9;

	private int currentLocation;
	private int nextLocation;

	private double score;

	/**
	 * Creates an action
	 * 
	 * @param current The current node position
	 * @param next The next node position
	 */
	public Action(int current, int next) {
		this.currentLocation = current;
		this.nextLocation = next;
		this.score = 0;
	}

	/**
	 * Calculates the new score for the current action.
	 * 
	 * @param nextNodeReward The reward value at the next node
	 * @param maxEdgeScore The max edge score at the next node location
	 */
	public void calculate(int nextNodeReward, double maxEdgeScore) {
		this.score = ((1 - ALPHA) * this.score)
				+ ALPHA * (nextNodeReward + GAMMA * maxEdgeScore);
	}
	
	/**
	 * Gets the current score
	 * 
	 * @return current score
	 */
	public double getScore() {
		return this.score;
	}

	/**
	 * Sets the score of the action
	 * 
	 * @param newScore The new value for score
	 */
	public void setScore(double newScore) {
		this.score = newScore;
	}

	/**
	 * Gets the current location
	 * 
	 * @return current location
	 */
	public int getCurrentLocation() {
		return this.currentLocation;
	}

	/**
	 * Gets the next location
	 * 
	 * @return next location
	 */
	public int getNextLocation() {
		return this.nextLocation;
	}

	@Override
	public String toString() {
		return "Q(" + currentLocation + "," + nextLocation + "){S:" + String.format("%.3f", this.score) + "}";
	}
}
