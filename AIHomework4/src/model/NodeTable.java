package model;

import java.util.ArrayList;
import java.util.HashMap;

import model.Node.Directions;

/**
 * A node table used for storing all information regarding the learning process.
 * @author Jacob Slattery
 */
public class NodeTable {
	
	public static int CLIFF_REWARD = -100;
	public static int OTHER_REWARD = -1;
	public static int GOAL_REWARD = 0;
	
	private int goalLocation;
	
	private ArrayList<Node> nodeTable;
	
	/**
	 * Makes a NodeTable with the given height and width. Sets cliff locations to the given indexes and specifies the goal location to be at the given index.
	 * 
	 * @param width Table width
	 * @param height Table height
	 * @param cliffLocations The indexes of cliff Nodes
	 * @param goalLocation The goal location
	 */
	public NodeTable(int width, int height, HashMap<Integer,Integer> cliffs, int goalLocation) {
		this.nodeTable = new ArrayList<Node>();
		this.goalLocation = goalLocation;
		
		this.createTable(width, height, cliffs);
	}
	
	/**
	 * Creates a table of the given height and width and sets all reward values and actions within it.
	 * 
	 * @param width The number of Nodes wide the table is
	 * @param height The number of Node high the table is
	 * @param cliffLocations The index locations of Nodes that are treated as cliffs
	 */
	private void createTable(int width, int height, HashMap<Integer,Integer> cliffs) {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int currentLocation = this.getLocation(col, row, width);
				Node currentNode = new Node(OTHER_REWARD);
				
				Integer top = this.getTopLocation(currentLocation, row, width);
				if (top != null) {
					currentNode.setAction(Directions.TOP_EDGE, currentLocation, top);
				}
				
				Integer bottom = this.getBottomLocation(currentLocation, row, width, height);
				if (bottom != null)	{
					currentNode.setAction(Directions.BOTTOM_EDGE, currentLocation, bottom);
				}
				
				Integer left = this.getLeftLocation(currentLocation, col);
				if (left != null) {
					currentNode.setAction(Directions.LEFT_EDGE, currentLocation, left);
				}
				
				Integer right = this.getRightLocation(currentLocation, col, width);
				if (right != null) {
					currentNode.setAction(Directions.RIGHT_EDGE, currentLocation, right);
				}
				this.nodeTable.add(currentNode);
			}
		}
		
		for (int current : cliffs.keySet()) {
			this.nodeTable.get(current).setReward(cliffs.get(current));
		}
		this.nodeTable.get(this.goalLocation).setReward(GOAL_REWARD);
	}
	
	/**
	 * Prints out all information about the table
	 */
	public void printTable() {
		System.out.println("TABLE:");
		int index = 0;
		for (Node current : this.nodeTable) {
			System.out.println("Index[" + index + "] Reward[" + this.nodeTable.get(index).getReward() + "]\t{L:" + current.getActionAt(Directions.LEFT_EDGE) + ", T:" + current.getActionAt(Directions.TOP_EDGE) + ", R:" + current.getActionAt(Directions.RIGHT_EDGE) + ", B:" + current.getActionAt(Directions.BOTTOM_EDGE) + "}");
			index++;
		}
	}
	
	/**
	 * Gets the node at the given index location
	 * 
	 * @param index Index of the node
	 * @return A Node
	 */
	public Node get(int index) {
		return this.nodeTable.get(index);
	}
	
	
	private int getLocation(int col, int row, int tableWidth) {
		return (row * tableWidth) + col;
	}
	
	
	private Integer getTopLocation(int location, int row, int tableWidth) {
		if (this.canMoveUp(row)) {
			return location - tableWidth;
		}
		return null;
	}
	
	private Integer getBottomLocation(int location, int row, int tableWidth, int tableHeight) {
		if (this.canMoveDown(row, tableHeight)) {
			return location + tableWidth;
		}
		return null;
	}
	
	private Integer getLeftLocation(int location, int col) {
		if (this.canMoveLeft(col)) {
			return location - 1;
		}
		return null;
	}
	
	private Integer getRightLocation(int location, int col, int tableWidth) {
		if (this.canMoveRight(col, tableWidth)) {
			return location + 1;
		}
		return null;
	}
	
	private boolean canMoveUp(int row) {	
		return row > 0;
	}
	
	private boolean canMoveDown(int row, int tableHeight) {
		return row < tableHeight - 1;
	}
	
	private boolean canMoveLeft(int col) {
		return col > 0;
	}
	
	private boolean canMoveRight(int col, int tableWidth) {
		return col < tableWidth - 1;
	}

}
