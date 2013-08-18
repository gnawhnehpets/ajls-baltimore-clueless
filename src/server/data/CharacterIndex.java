package server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import dictionary.data.LocationIndex;
import dictionary.gameEntities.*;

/**
 * Status: Code finished
 * @author mjp
 *
 */
public class CharacterIndex {

	// all final data
	private static ArrayList<ClueCharacter> allCharacters;
	
	private static boolean isInitialized = false;

	private static HashMap<String, ClueCharacter> mapCharNamesToChar = 
			new HashMap<String, ClueCharacter>();
	
	public static ArrayList<ClueCharacter> getCharacters()
	{
		init();
		return allCharacters;
	}

	private static void init() {
		if (!isInitialized)
		{
			createCharacters();
			isInitialized = true;
		}
	}

	private static void createCharacters()
	{
		int id = 0;
		
		allCharacters = new ArrayList<ClueCharacter>();
		
		// note location is different for each Character.  Use the location objects
		allCharacters.add(new ClueCharacter("Miss Scarlet", id++,
				ClueCharacter.CharacterColor.RED, LocationIndex
						.getLocationByName("Hall 2")));
		
		allCharacters.add(new ClueCharacter("Col. Mustard", id++,
				ClueCharacter.CharacterColor.YELLOW, LocationIndex
						.getLocationByName("Hall 5")));
		
		allCharacters.add(new ClueCharacter("Mrs. White", id++,
				ClueCharacter.CharacterColor.WHITE, LocationIndex
				.getLocationByName("Hall 12")));

		allCharacters.add(new ClueCharacter("Mr. Green", id++,
				ClueCharacter.CharacterColor.GREEN, LocationIndex
				.getLocationByName("Hall 11")));

		allCharacters.add(new ClueCharacter("Mrs. Peacock", id++,
				ClueCharacter.CharacterColor.BLUE, LocationIndex
						.getLocationByName("Hall 8")));

		allCharacters.add(new ClueCharacter("Prof. Plum", id++,
				ClueCharacter.CharacterColor.PLUM, LocationIndex
				.getLocationByName("Hall 3")));
		
		for (ClueCharacter curr: allCharacters)
		{
			mapCharNamesToChar.put(curr.getName(), curr);
		}
	}

	public static ClueCharacter getCharacterByName(String nameIn) {
		for(ClueCharacter c: allCharacters){
			if(c.getName().equals(nameIn))
				return c;
		}
		return null;
		/*init();
		return mapCharNamesToChar.get(nameIn);*/
	}
}
