package dictionary.interfaceData;

import java.io.Serializable;
import java.util.*;

import dictionary.gameEntities.*;

/**
 * Status: unfinished? depends on server and GUI's needs
 * 
 * @author mjp
 * Information that is needed by the user to update their GUI screen
 */
public class GameState implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8089525784695376284L;

	// whose turn is it?
	public Player currentPlayer;
	
	// What is the current player supposed to do?
	public UserActionTypes pendingAction;
	
	// where is everyone?
	public ArrayList<ClueCharacter> charactersPositions;

	// where are the weapons?
	public ArrayList<Weapon> weaponPositions;
	
	/* From HCI: message indicating whether to disprove, accuse, etc. 
	 * could also include error */
	public String nextActionText; 
	
	/* from HCI: awaiting move from Colonel Mustard */
	public String playerTurnStatus; 
	// note: the user's hand will not change after they are first dealt cards
	
	public GameState(Player currentPlayer, UserActionTypes pendingAction,
			ArrayList<ClueCharacter> charactersPositions,
			ArrayList<Weapon> weaponPositions,
			String nextActionText,
			String playerTurnStatus) 
	{
		this.currentPlayer = currentPlayer;
		this.pendingAction = pendingAction;
		this.charactersPositions = charactersPositions;
		this.weaponPositions = weaponPositions;
		this.nextActionText = nextActionText;
		this.playerTurnStatus = playerTurnStatus;
	}
	
	
	public GameState(GameState copy)
	{
		this.currentPlayer = copy.currentPlayer;
		this.pendingAction = copy.pendingAction;
		this.nextActionText = copy.nextActionText;
		this.playerTurnStatus = copy.playerTurnStatus;
		
		// must do this semi-deep copy for serialized object transfer
		this.charactersPositions = new ArrayList<ClueCharacter>();
		for (ClueCharacter currCharacter: copy.charactersPositions)
		{
			this.charactersPositions.add(new ClueCharacter(currCharacter));
		}
//		charactersPositions.addAll(copy.charactersPositions);
		this.weaponPositions = new ArrayList<Weapon>();
		weaponPositions.addAll(copy.weaponPositions);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\n\t");
		sb.append(this.charactersPositions.toString());
		sb.append("\n\t");
		sb.append(this.weaponPositions.toString());
		sb.append("\n\t");
		sb.append(this.currentPlayer.toString());
		sb.append("\n\t");
		sb.append(this.nextActionText.toString());
		sb.append("\n\t");
		sb.append(this.pendingAction.toString());
		sb.append("\n\t");
		sb.append(this.playerTurnStatus.toString());
		sb.append("\n");
		sb.append("}");
		return sb.toString();
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return this;
	}
}
