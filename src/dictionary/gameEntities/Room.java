package dictionary.gameEntities;

import java.io.Serializable;

/**
 * status: finished
 * @author mjp
 *
 * @version 1.0
 * @created 06-Nov-2011 10:35:20 AM
 */
public class Room extends Location implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 97265542945247891L;
	public int row;
	public int col;
	
	public Room(String nameIn, int idIn, int rowIn, int colIn)
	{
		super(nameIn, idIn);
		row = rowIn;
		col = colIn;
	}

	public Room(Room newLocation) {
		super(newLocation);
	}

	@Override
	public boolean isRoom() {
		return true;
	}
	
	boolean isTop() {return 0 == row;}
	boolean isVerCenter() {return 1 == row;}
	boolean isBottom() {return 2 == row;}
	boolean isLeft() {return 0 == col;}
	boolean isRight() {return 1 == col;}
	boolean isHorCenter(){return 2 == col;}
}//end Room