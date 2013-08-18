package server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dictionary.data.LocationIndex;
import dictionary.gameEntities.*;

/**
 * Status: Code finished
 * 
 * @author mjp
 *
 */
public class WeaponIndex {
	// all final data
	private static ArrayList<Weapon> allWeapons;
	
	private static boolean isInitialized = false;

	private static HashMap<String, Weapon> mapCharNamesToChar = new HashMap<String, Weapon>();
	
	public static ArrayList<Weapon> getWeapons()
	{
		init();
		return allWeapons;
	}
	
	private static void init() {
		if (!isInitialized)
		{
			createWeapons();
			isInitialized = true;
		}
	}

	private static void createWeapons()
	{
		int id = 0;
		
		Location defaultLocation = 
				LocationIndex.getLocationByName("Billiard Room");

		allWeapons = new ArrayList<Weapon>();
		// Add all weapons
		allWeapons.add(new Weapon("Rope", id++, defaultLocation));
		allWeapons.add(new Weapon("Lead Pipe", id++, defaultLocation));
		allWeapons.add(new Weapon("Knife", id++, defaultLocation));
		allWeapons.add(new Weapon("Wrench", id++, defaultLocation));
		allWeapons.add(new Weapon("Candlestick", id++, defaultLocation));
		allWeapons.add(new Weapon("Pistol", id++, defaultLocation));
		for (Weapon curr : allWeapons)
		{
			mapCharNamesToChar.put(curr.getName(), curr);
		}
	}

	public static Weapon getWeaponByName(String nameIn) {
		for(Weapon w: allWeapons){
			if(w.getName().equals(nameIn))
				return w;
		}
		return null;
		//init();
		//return mapCharNamesToChar.get(nameIn);
	}
}
