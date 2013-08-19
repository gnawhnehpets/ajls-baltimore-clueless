package dictionary.interfaceData;

import java.io.Serializable;

import dictionary.gameEntities.ClueCharacter;
import dictionary.gameEntities.Hand;




/**
 * Status: unfinished? depends on server & client's needs
 * @author mjp
 * @version 1.0
 * @created 06-Nov-2011 10:35:19 AM
 * Initially created by the Server when a new Player 
 * wishes to Join.
 */
public class Player implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7989216279851394136L;
	public String userName;
	public final int playerId;
	public Hand playerCards;
	public ClueCharacter character;

	public Player(int playerIdIn, String userName)
	{
		this.userName = userName;
		playerId = playerIdIn;
		playerCards = new Hand();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (! (obj instanceof Player)) return false;
		Player other = (Player) obj;
		if (playerId != other.playerId) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = result*prime + playerId;
		
		return result;
	}
	
	public Integer getID(){
		return playerId;
	}
	
	public ClueCharacter getCharacter(){
		return character;
	}
	
	public String getVerboseDisplayText() {
		if(character != null) {
			return character.getName() + " (" + userName + ") " + character.getLocation().getName();
		} else {
			return "UNKNOWN (" + userName + ")";
		}
	}
	
	public String getDisplayText() {
		return userName + (character == null ? "" : "(" + character.getName() + ")");
	}
	
	@Override
	public String toString() {
		return 
				((character!=null)?this.character.getName():"")
				+" ("+this.playerId+"): "+
				((character!=null && character.getLocation()!= null)?character.getLocation().getName():"")
				+" "+
				this.playerCards.toString();
	}
	
} //end Player