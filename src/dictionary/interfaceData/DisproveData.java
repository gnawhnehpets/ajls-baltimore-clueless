package dictionary.interfaceData;

import java.io.Serializable;

import dictionary.gameEntities.*;

/**
 * Status: unfinished? depends on handler method's needs
 */
public class DisproveData implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6778684106441394028L;
	private Player disprovingPlayer;
	private Card disprovalCard;
	public Player getDisprovingPlayer() {
		return disprovingPlayer;
	}
	public void setDisprovingPlayer(Player disprovingPlayer) {
		this.disprovingPlayer = disprovingPlayer;
	}
	public Card getDisprovalCard() {
		return disprovalCard;
	}
	public void setDisprovalCard(Card disprovalCard) {
		this.disprovalCard = disprovalCard;
	}
}
