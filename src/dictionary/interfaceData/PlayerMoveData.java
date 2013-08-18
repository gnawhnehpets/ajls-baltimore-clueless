package dictionary.interfaceData;

import java.io.Serializable;

import dictionary.gameEntities.Location;

/**
 * Status: unfinished? depends on server & client's needs
 * @author mjp
 *
 */
public class PlayerMoveData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2301765636061112521L;
	private Player movingPlayer;
	private Location newLocation;
	
	public PlayerMoveData(Player p, Location l)
	{
		movingPlayer = p;
		newLocation = l;
	}
	
	public Player getMovingPlayer() {
		return movingPlayer;
	}
	public void setMovingPlayer(Player movingPlayer) {
		this.movingPlayer = movingPlayer;
	}
	public Location getNewLocation() {
		return newLocation;
	}
	public void setNewLocation(Location newLocation) {
		this.newLocation = newLocation;
	}
}
