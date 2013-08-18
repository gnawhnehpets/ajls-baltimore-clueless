package dictionary.gameEntities;

import java.io.Serializable;

/**
 * status: finished
 * @author mjp
 *
 * @version 1.0
 * @created 06-Nov-2011 10:35:17 AM
 */
public class Hallway extends Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -332375368244793129L;

	public Hallway(String nameIn, int idIn)
	{
		super(nameIn, idIn);
	}

	public Hallway(Hallway newLocation) {
		super(newLocation);
	}

	@Override
	public boolean isRoom() {
		return false;
	}
}//end Hallway