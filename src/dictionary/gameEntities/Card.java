package dictionary.gameEntities;

import java.io.Serializable;

import dictionary.interfaceData.Player;

/**
 * status: finished
 * @author mjp
 * @version 1.0
 * @created 06-Nov-2011 10:34:58 AM
 */
public abstract class Card implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6966139547156338521L;

	public static enum CardType
	{
		CHARACTER, ROOM, WEAPON, ANY
	}
	
	private String cardName;
	private int cardId;

	public Card(String cardNameIn, int cardIdIn)
	{
		cardName = cardNameIn;
		cardId = cardIdIn;
	}
	public Card(Card other)
	{
		cardName = other.getCardName();
		cardId = other.getCardId();
	}	
	public Card() {
	}

	public String getCardName() {
		return cardName;
	}
	
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	
	public abstract CardType getCardType();
	@Override
	public String toString() {
		return getCardName();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (! (obj instanceof Player)) return false;
		Card other = (Card) obj;
		if (this.cardId != other.cardId) return false;
	    if (this.cardName == null) {
	        if (other.cardName != null)
	            return false;
	    } else if (!cardName.equals(other.cardName))
	        return false;

		return true;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = result*prime + this.cardId;
		result = result*prime + (this.cardName!=null?this.cardName.hashCode():0);
		
		return result;
	}
}//end Card