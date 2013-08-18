package dictionary.interfaceData;

import java.io.Serializable;

/**
 * Status: unfinished? depends on handler method's needs
 * @author mjp
 * Provided by client to the server to join the game
 */
public class JoinGameData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2980987696856976172L;

	String ipAddress; // provided by client
	
	String userName; // optional: provided by client
}
