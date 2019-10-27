package control;

import java.util.ArrayList;
import java.util.HashMap;

import model.Action;
import model.NodeTable;
import model.TableTraveler;

/**
 * The launching point for the program
 * 
 * @author Jacob Slattery
 */
public class Driver {
	
	public static int START_LOCATION = 36;
	public static int GOAL_LOCATION = 47;
	public static int OPTIMAL_DISTANCE = 13;
	public static int WIDTH = 12;
	public static int HEIGHT = 4;

	/**
	 * The launching point for the program
	 * 
	 * @param args Not used
	 */
	public static void main(String[] args) {
		
		
		
		HashMap<Integer, Integer> cliffMap = createCliffMapAllDefault();
//		HashMap<Integer, Integer> cliffMap = createCliffMapCustom();
		
		FindOptimal(cliffMap);
		
//		TableTraveler traveler = new TableTraveler(START_LOCATION, GOAL_LOCATION, cliffMap, WIDTH, HEIGHT);
//		traveler.printTable();
		
//		TableTraveler traveler = timeLaps(cliffMap);
//		TableTraveler traveler = travelUntilOptimalPathFound(cliffMap);
//		TableTraveler traveler = run(cliffLocations, 10000);
//		findSafeNumberOfEpisodeToTrustPath(cliffMap);
		
//		boolean works = true;
//		
//		traveler.printTable();
//		System.out.println();
//
//		boolean current = traveler.findGoal(true, 13);
//		if (!current) {
//			works = false;
//		}
//		
//		if (works) {
//			System.out.println("Success");
//		} else {
//			System.out.println("Boo");
//		}

	}
	
	public static void FindOptimal(HashMap<Integer, Integer> cliffMap)
	{
		int min = Integer.MAX_VALUE;
		double bestA = 0;
		double bestG = 0;
		double bestE = 0;
		for (double a = 1; a <= 10; a++)
		{
			Action.ALPHA = a/10;
			for (double g = 1; g <= 10; g++)
			{
				Action.GAMMA = g/10;
				for (double e = 0; e <= 10; e++)
				{
					TableTraveler.EPSILON = e/10;
					int currentMax = findSafeNumberOfEpisodeToTrustPath(cliffMap);
					if (currentMax < min)
					{
						bestA = Action.ALPHA;
						bestG = Action.GAMMA;
						bestE = TableTraveler.EPSILON;
						min = currentMax;
					}
				}
			}
		}
		
		System.out.println("Number of safe episodes: " + min);
		System.out.println("Alpha: " + bestA);
		System.out.println("Gamma: " + bestG);
		System.out.println("Epsilon: " + bestE);
	}
	
	private static HashMap<Integer,Integer> createCliffMapCustom()
	{
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		
		map.put(5, -50);
		map.put(7, -50);
		map.put(28, -50);
		map.put(40, -100);
		map.put(42, -50);
		map.put(43, -50);
		map.put(44, -50);
		map.put(45, -100);
		map.put(46, -200);
		return map;
	}
	
	
	private static HashMap<Integer,Integer> createCliffMapAllDefault()
	{
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		
		for (int i = (START_LOCATION + 1); i <= START_LOCATION + 10; i++) {
			map.put(i, NodeTable.CLIFF_REWARD);
		}
		
		return map;
	}
	
	private static ArrayList<Integer> createCliffLocations() {
		ArrayList<Integer> cliffLocations = new ArrayList<Integer>();
		
		for (int i = (START_LOCATION + 1); i <= START_LOCATION + 10; i++) {
			cliffLocations.add(i);
		}
		return cliffLocations;
	}
	
	private static int findSafeNumberOfEpisodeToTrustPath(HashMap<Integer,Integer> cliffLocations)
	{
//		System.out.println("Traveling until optimal path found...");
		
		int max = 0;
		for (int i = 0; i < 1000; i++)	{
			TableTraveler traveler = new TableTraveler(START_LOCATION, GOAL_LOCATION, cliffLocations, WIDTH, HEIGHT);
			int count = 0;
			while (!traveler.findGoal(false, OPTIMAL_DISTANCE))
			{
				traveler.travel();
				count++;
			}
			if (count > max)
			{
				max = count;
			}
		}
		System.out.println(Action.ALPHA + "," + Action.GAMMA + "," + TableTraveler.EPSILON + "," + max);

//		System.out.println("Maximum episodes was " + max);
		return max;
	}
	
	private static TableTraveler travelUntilOptimalPathFound(HashMap<Integer,Integer> cliffLocations)
	{
		System.out.println("Traveling until optimal path found...");
		TableTraveler traveler = new TableTraveler(START_LOCATION, GOAL_LOCATION, cliffLocations, WIDTH, HEIGHT);
		traveler.printTable();
		int count = 0;
		while (!traveler.findGoal(false, OPTIMAL_DISTANCE))
		{
			traveler.travel();
			count++;
		}
		System.out.println("Took " + count + " episodes to find optimal path.");
		return traveler;
	}
	
	private static TableTraveler timeLaps(HashMap<Integer,Integer> cliffLocations) {
		System.out.println("Beggin running timed laps...");
		TableTraveler traveler = new TableTraveler(START_LOCATION, GOAL_LOCATION, cliffLocations, WIDTH, HEIGHT);
		traveler.printTable();
		
		for (int i = 0; i < 5; i++)	{
			long startTime = System.nanoTime();
			for (int j = 0; j < 100; j++) {
				traveler.travel();
			}
			long endTime = System.nanoTime();			
			long duration = (endTime - startTime);
			System.out.println("Lap " + (i + 1) + ": " + duration / 1000000 + " miliseconds");
		}
		
		System.out.println();
		return traveler;
		
	}
	
	private static TableTraveler run(HashMap<Integer,Integer> cliffLocations, int numberOfEpisodes)
	{
		TableTraveler traveler = new TableTraveler(START_LOCATION, GOAL_LOCATION, cliffLocations, WIDTH, HEIGHT);
		
		for (int i = 0; i < numberOfEpisodes; i++) {
			traveler.travel();
		}
		
		return traveler;
	}

}
