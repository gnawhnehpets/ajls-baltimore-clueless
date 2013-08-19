package dictionary.gameEntities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * status: finished
 * @author mjp
 *
 * @version 1.0
 * @created 06-Nov-2011 10:35:18 AM
 */
public abstract class Location implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7343768421090038659L;

	public abstract boolean isRoom();
	
	String name;
	int id;

	private Location up;
	private Location down;
	private Location left;
	private Location right;
	private Location secret;
	
	private ArrayList<Location> allConnections = new ArrayList<Location>();

	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(name).append(": ").append(id).append("(");
		
		if (getUp()!=null) buffer.append(" up: ").append(getUp().getName()).append(";");
		if (getDown()!=null) buffer.append(" down: "+getDown().getName()).append(";");
		if (getLeft()!=null) buffer.append(" left: "+getLeft().getName()).append(";");
		if (getRight()!=null) buffer.append(" right: "+getRight().getName()).append(";");
		if (getSecret()!=null) buffer.append(" secret: "+getSecret().getName()).append(";");
		
		buffer.append(")");
		
		return buffer.toString();
	}
	public Location(String nameIn, int idIn)
	{
		name = nameIn;
		id = idIn;
	}
	public Location(Location in)
	{
		if (in.getAllConnections()!=null) this.allConnections.addAll(in.getAllConnections());
		this.id = in.id;
		this.name = in.name;
		this.down = in.down;
		this.left = in.left;
		this.right = in.right;
		this.secret = in.secret;
		this.up = in.up;
	}

	public String getName() 
	{
		return name;
	}
	
	void setName(String nameIn)
	{
		name = nameIn;
	}
	 
	public int getId()
	{
		return id;
	}
	
	void setId(int idIn) 
	{
		id = idIn;
	}
	
	public Location getUp() {
		return up;
	}
	
	public void setUp(Location up) {
		this.up = up;
		this.allConnections.add(up);
	}
	
	public Location getDown() {
		return down;
	}
	
	public void setDown(Location down) {
		this.down = down;
		this.allConnections.add(down);
	}
	
	public Location getLeft() {
		return left;
	}
	
	public void setLeft(Location left) {
		this.left = left;
		this.allConnections.add(left);
	}
	
	public Location getRight() {
		return right;
	}
	
	public void setRight(Location right) {
		this.right = right;
		this.allConnections.add(right);
	}
	
	public Location getSecret() {
		return secret;
	}
	
	public void setSecret(Location secret) {
		this.secret = secret;
		this.allConnections.add(secret);
	}
	
	public ArrayList<Location> getAllConnections() {
		return allConnections;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (! (obj instanceof Location)) return false;
		Location other = (Location) obj;
		if (id != other.id) return false;
		if (this.getAllConnections()!=null)
		{
			if (other.getAllConnections()==null) return false;
			if (this.getAllConnections().size()!=other.getAllConnections().size()) return false;
			for (int i = 0; i < this.getAllConnections().size(); i++)
			{
				if (this.getAllConnections().get(i).hashCode() !=
					other.getAllConnections().get(i).hashCode()) return false;	
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = result*prime + id;
		
		if (this.getAllConnections()!=null)
		{
			for (int i = 0; i < this.getAllConnections().size(); i++)
			{
				result = result*prime + getAllConnections().get(i).id;	
			}
		}
		else result = result*prime;
		
		return result;
	}
}//end Location