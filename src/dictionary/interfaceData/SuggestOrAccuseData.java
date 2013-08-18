package dictionary.interfaceData;

import java.io.Serializable;

import dictionary.gameEntities.*;

/**
 * Status: unfinished? depends on server & client's needs
 * @author mjp
 *
 */
public class SuggestOrAccuseData implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8296901713786477504L;

	//true = accusation, false = suggestion 
	boolean isAccusation;
	
	private Room room;
	private ClueCharacter suspect;
	private Weapon weapon;

	private Player player;

	//not currently being used
	private boolean playerPassesOnAccusation;

	public SuggestOrAccuseData(Player p, ClueCharacter c, Weapon w, Room r, Boolean isAccusation, Boolean playerPasses)	{
		player = p;
		suspect = c;
		weapon = w;
		room = r;
		this.isAccusation = isAccusation;
		playerPassesOnAccusation = playerPasses;
	}
	
	
	public boolean isAccusation() {
		return isAccusation;
	}
	public void setAccusation(boolean isAccusation) {
		this.isAccusation = isAccusation;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room2) {
		this.room = room2;
	}
	public ClueCharacter getSuspect() {
		return suspect;
	}
	public void setSuspect(ClueCharacter suspect2) {
		this.suspect = suspect2;
	}
	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon2) {
		this.weapon = weapon2;
	}
	public void setSuggestingPlayer(Player playerIn) {
		this.player = playerIn;
		
	}
	public Player getSuggestingPlayer() {
		return player;
	}
	public boolean isPass() {
		return playerPassesOnAccusation;
	}
	public void setPlayerPasses(boolean playerPassesOnAccusationIn)
	{
		playerPassesOnAccusation = playerPassesOnAccusationIn;
	}
}
