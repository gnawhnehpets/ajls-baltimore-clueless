package dictionary.gameEntities;

import java.io.Serializable;

import dictionary.interfaceData.Player;

/**
 * status: finished
 * @author mjp
 *
 */
public class Weapon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7293237789731160301L;

	private String name;

	private int id;
	Location location;
	
	public Weapon(String name, int id, Location location) {
		this.name = name;
		this.id = id;
		this.location = location;
	}
	
	void move(Location newLocation)
	{
		location = newLocation;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setLocation(Room room) {
		location = room;
	}
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Weapon)) return false;
		return id == ((Weapon)obj).id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
}
