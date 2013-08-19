package dictionary.interfaceData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import dictionary.gameEntities.StatusLogEntry;



/**
 * Status: unfinished? depends on server & client's needs
 * 
 * @author mjp
 * @version 1.0
 * @created 06-Nov-2011 10:34:56 AM
 * 
 * Used as a wrapper for all the types of data that could be 
 * transfered between the server and the client.
 * Includes data that the client and server exchange:
 * a player action from a client, a game state change from
 * the server, etc.
 */
public class NetworkDataWrapper implements Serializable {

	private static final long serialVersionUID = 3021694293931850993L;

	private static final String RET = "\n\r";

	public enum ActionType 
	{
		// client/user actions:
		USER_ACTION,
		// server initializing a player (client) and initial game state
		INIT_PLAYER, 
		// server providing data to client - notification, player location change, end of game, etc.
		GAME_STATE_CHANGE 
	}
	
	// indicates the type of data in this object
	public ActionType currentAction;
	
	// if action type is JOIN_GAME
	public JoinGameData userInfo;

	// only set if currentAction is INIT_PLAYER
	// gives user their cards and other one-time initialization information
	public Player playerData;
	
	// only set if currentAction is GAME_STATE_CHANGE 
	// used by server to tell clients what to show.  
	public GameState gameState;

	public HashMap<Player, ArrayList<StatusLogEntry>> statusLogEntries;
	// -----
	
	// only set if currentAction is a USER_ACTION type
	public UserActionTypes currentUserAction;
	
	// only set if currentUserAction is DISPROVE
	public DisproveData disproval;
	
	// only set if currentUserAction is PLAYER_MOVE
	public PlayerMoveData moveData;
	
	// only set if currentUserAction is SUGGEST or ACCUSE
	public SuggestOrAccuseData suggestionOrAccusation;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		
		sb.append("**CurrentAction"+RET);
		if (this.currentAction!=null)
		{
			sb.append(this.currentAction.toString());
			sb.append(RET);
		}

		sb.append("**CurrentUserAction"+RET);
		if (this.currentUserAction!=null)
		{
			sb.append(this.currentUserAction.toString());
			sb.append(RET);
		}
		
		sb.append("**Disproval"+RET);
		if (this.disproval!=null)
		{
			sb.append(this.disproval.toString());
			sb.append(RET);
		}
		
		sb.append("**GameState"+RET);
		if (this.gameState!=null)
		{
			sb.append(this.gameState.toString());
			sb.append(RET);
		}
		
		sb.append("**MoveData"+RET);
		if (this.moveData!=null)
		{
			sb.append(this.moveData.toString());
			sb.append(RET);
		}
		
		sb.append("**PlayerData"+RET);
		if (this.playerData!=null)
		{
			sb.append(this.playerData.toString());
			sb.append(RET);
		}
		
		sb.append("**StatusLog"+RET);
		if (this.statusLogEntries!=null)
		{
			sb.append(this.statusLogEntries.toString());
			sb.append(RET);
		}
		
		sb.append("**Suggest or Accuse"+RET);
		if (this.suggestionOrAccusation!=null)
		{
			sb.append(this.suggestionOrAccusation.toString());
			sb.append(RET);
		}
		
		sb.append("**User Info"+RET);
		if (this.userInfo!=null)
		{
			sb.append(this.userInfo.toString());
			sb.append(RET);
		}
		
		sb.append("]");
		return sb.toString();
	}
}//end ActionData