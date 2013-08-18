package dictionary.data;

import java.util.*;

import dictionary.gameEntities.*;

/**
 * status: finished
 * @author mjp
 *
 * Holds all of the location data: Rooms and Locations.  
 * This data is consistent between clients and the server and
 * consistent for each run of the game.
 * @author mjp
 */
public class LocationIndex {

	private static List <Location> allLocations;
	
	// indexed by hallway Number 
	private static HashMap <Integer, Hallway> allHallways;

	private static HashMap <String, Location> maplocationNamesToLocation;
	
	private static List <Room> allRooms;
	
	// organizes the rooms into a grid: row, column
	private static ArrayList <ArrayList<Room>> columnsOfRowsOfRooms;
	
	private static boolean isInitialized = false;
	
	public static List <Location> getLocations()
	{
		init();
		return allLocations;
	}

	/**
	 * Creates all the rooms and hallways, giving each a unique location id.
	 * Refer to ClueLessMap_HallwayLabels.jpg
	 */
	private static void createLocations()
	{
		int id = 0;
		
		columnsOfRowsOfRooms = new ArrayList<ArrayList<Room>>();
		allRooms = new ArrayList<Room>();
		allHallways = new HashMap<Integer, Hallway>();
		allLocations = new ArrayList<Location>();
		maplocationNamesToLocation = new HashMap<String, Location>();

		// Add first row of Rooms
		ArrayList <Room> firstRow = new ArrayList<Room>();
		Room study = new Room("Study", id++, 0, 0);
		firstRow.add(study);
		firstRow.add(new Room("Hall", id++, 0, 1));
		Room lounge = new Room("Lounge", id++, 0, 2);
		firstRow.add(lounge);
		columnsOfRowsOfRooms.add(firstRow);
		
		// Add second row of Rooms
		ArrayList <Room> secondRow = new ArrayList<Room>();
		secondRow.add(new Room("Library", id++, 1, 0));
		secondRow.add(new Room("Billiard Room", id++, 1, 1));
		secondRow.add(new Room("Dining Room", id++, 1, 2));
		columnsOfRowsOfRooms.add(secondRow);
		
		// Add second row of Rooms
		ArrayList <Room> thirdRow = new ArrayList<Room>();
		Room conservatory = new Room("Conservatory", id++, 2, 0);
		thirdRow.add(conservatory);
		thirdRow.add(new Room("Ball Room", id++, 2, 1));
		Room kitchen = new Room("Kitchen", id++, 2, 2);
		thirdRow.add(kitchen);
		columnsOfRowsOfRooms.add(thirdRow);
		
		// create secret passageways
		study.setSecret(kitchen);
		kitchen.setSecret(study);
		conservatory.setSecret(lounge);
		lounge.setSecret(conservatory);

		// iterate through each row, adding contents to allRooms
		for (ArrayList <Room> currRow : columnsOfRowsOfRooms)
		{
			allRooms.addAll(currRow);
			for (Room currRoom : currRow)
			{
				maplocationNamesToLocation.put(currRoom.getName(), currRoom);
			}
		}

		allLocations.addAll(allRooms);
		
		// create the Hallways and their connections
		createHorizontalHallwayAndConnections(1, 0, 0, id++);
		createHorizontalHallwayAndConnections(2, 0, 1, id++);

		createVerticalHallwayAndConnections(3, 0, 0, id++);
		createVerticalHallwayAndConnections(4, 0, 1, id++);
		createVerticalHallwayAndConnections(5, 0, 2, id++);
		
		createHorizontalHallwayAndConnections(6, 1, 0, id++);
		createHorizontalHallwayAndConnections(7, 1, 1, id++);
		
		createVerticalHallwayAndConnections(8, 1, 0, id++);
		createVerticalHallwayAndConnections(9, 1, 1, id++);
		createVerticalHallwayAndConnections(10, 1, 2, id++);
		
		createHorizontalHallwayAndConnections(11, 2, 0, id++);
		createHorizontalHallwayAndConnections(12, 2, 1, id++);
		
		// iterate through each row, adding contents to allRooms
		for (Hallway currHall : allHallways.values())
		{
			maplocationNamesToLocation.put(currHall.getName(), currHall);
			allLocations.add(currHall);
		}
		
	}
	
	/**
	 * Creates a left-to-right hallway and connects it to the appropriate rooms
	 * @param hallNumber the number to be used to designate the hallway
	 * @param row the row of the hallway
	 * @param col the column of the room to the hallway's left
	 * @param locationId the id for the hallway - unique for all locations
	 */
	private static void createHorizontalHallwayAndConnections(int hallNumber, int row, int col, int locationId) {
		Hallway currHall = new Hallway ("Hall "+hallNumber, locationId);
		setHorConnection(columnsOfRowsOfRooms.get(row).get(col), currHall);
		setHorConnection(currHall, columnsOfRowsOfRooms.get(row).get(col+1));
		allHallways.put(Integer.valueOf(hallNumber), currHall);
	}
	/**
	 * Creates a up-to-down hallway and connects it to the appropriate rooms
	 * @param hallNumber the number to be used to designate the hallway
	 * @param row the row of the room above the hallway
	 * @param col the column of the hallway
	 * @param locationId the id for the hallway - unique for all locations
	 */
	private static void createVerticalHallwayAndConnections(int hallNumber, int row, int col, int locationId) {
		Hallway currHall = new Hallway ("Hall "+hallNumber, locationId);
		setVerConnection(columnsOfRowsOfRooms.get(row).get(col), currHall);
		setVerConnection(currHall, columnsOfRowsOfRooms.get(row+1).get(col));
		allHallways.put(Integer.valueOf(hallNumber), currHall);
	}

	
	/**
	 * Creates the left/right associations between two locations
	 * @param left the left-most location
	 * @param right the right-most location
	 */
	private static void setHorConnection(Location left, Location right) {
		left.setRight(right);
		right.setLeft(left);
	}

	/**
	 * Creates the up/down associations between the two locations
	 * @param up the higher location
	 * @param down the lower location
	 */
	private static void setVerConnection(Location up, Location down) {
		up.setDown(down);
		down.setUp(up);
	}

	public static final List<Location> getAllLocations() {
		init();
		return allLocations;
	}
	
	public static final HashMap<Integer, Hallway> getAllHallways() {
		init();
		return allHallways;
	}
	
	public static final List<Room> getAllRooms() {
		init();
		return allRooms;
	}

	private static void init() {
		if (!isInitialized)
		{
			createLocations();
			isInitialized = true;
		}
	}
	public static Location getLocationByName(String nameIn)
	{
		init();
		return maplocationNamesToLocation.get(nameIn);
	}
	public static Room getRoomByName(String nameIn)
	{
		for(Location loc: allLocations){
			if(loc.getName().equals(nameIn))
				return (Room)loc;
		}
		return null;
	}
}
