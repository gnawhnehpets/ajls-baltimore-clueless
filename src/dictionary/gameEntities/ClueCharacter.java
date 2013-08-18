package dictionary.gameEntities;

import java.io.Serializable;


/**
 * status: finished
 * @author mjp
 *
 *
 * Note that not all Clue characters have a user associated with them
 *  
 */
public class ClueCharacter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8339630381486478255L;
	public static enum CharacterColor
	{
		RED, WHITE, PLUM, YELLOW, GREEN, BLUE
	}
	
	private CharacterColor color;
	private String name;
	private int id;
	private Location location;
	
	boolean movedBySuggestion;

	public ClueCharacter(String name, int id, CharacterColor color,
			Location newLocation) {
		this(name,id,color,false,newLocation);
	}
	
	public ClueCharacter(String name, int id, CharacterColor color, boolean moved,
			Location newLocation) {
		this.color = color;
		this.name = name;
		this.id = id;
		this.movedBySuggestion = moved;
		
		if (newLocation.isRoom()) this.location = new Room((Room)newLocation);
		else this.location = new Hallway((Hallway)newLocation);
	}

	public ClueCharacter(ClueCharacter in, Location newLocation) {
		this(in.name, in.id, in.color, in.movedBySuggestion, newLocation);
	}
	
	public ClueCharacter(ClueCharacter character) {
		this(character, character.location);
	}

	public int getId() {
		return id;
	}
	public void setId(int characterId) {
		this.id = characterId;
	}
	public CharacterColor getColor() {
		return color;
	}
	public void setColor(CharacterColor characterColor) {
		this.color = characterColor;
	}
	public String getName() {
		return name;
	}
	public void setName(String characterName) {
		this.name = characterName;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location characterLocation) {
		// TODO revert?
		if (characterLocation.isRoom())
		{
			Room temp = (Room) characterLocation;
			this.location = new Room(characterLocation.name, characterLocation.id, temp.row, temp.col);
		}
		else 
		{
			this.location = new Hallway(characterLocation.name, characterLocation.id);
		}
	}

	public void setMovedBySuggestion(boolean movedBySuggestionIn) {
		movedBySuggestion = movedBySuggestionIn;
	}
	public boolean isMovedBySuggestion()
	{
		return movedBySuggestion;
	}
	@Override
	public String toString() {
		return getName()+" ("+getLocation().getName()+" moved:"+movedBySuggestion+" )";
	}
	@Override
	public boolean equals(Object obj) {
// TODO		if (this == obj) return true;
		if (obj == null) return false;
		if (! (obj instanceof ClueCharacter)) return false;
		ClueCharacter other = (ClueCharacter) obj;
		if (id != other.id) return false;
	    if (this.color == null) {
	        if (other.color != null)
	            return false;
	    } else if (!color.equals(other.color))
	        return false;
	    if (this.location == null) {
	    	if (other.location != null)
	    		return false;
	    } else if (!location.equals(other.location))
	    	return false;
	    if (this.name == null) {
	    	if (other.name != null)
	    		return false;
	    } else if (!name.equals(other.name))
	    	return false;
	    
	    if (this.movedBySuggestion!=other.movedBySuggestion) return false;
	    
		return true;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = result*prime + id;
		result = result*prime + (this.name!=null?name.hashCode():0);
		result = result*prime + (this.location!=null?location.hashCode():0);
		result = result*prime + (this.color!=null?color.hashCode():0);
		result = result*prime + (this.movedBySuggestion?1:2);
		
		return result;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ClueCharacter(this);
	}
	
}
