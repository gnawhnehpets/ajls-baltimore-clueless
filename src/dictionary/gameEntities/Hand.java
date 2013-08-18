package dictionary.gameEntities;

import java.io.Serializable;
import java.util.*;

import dictionary.gameEntities.Card.CardType;
import dictionary.interfaceData.Player;

/**
 * status: finished
 * @author mjp
 *
 */
public class Hand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3606068761494573287L;
	private ArrayList<Card> cards; 
	
	public Hand() 
	{
		this.cards = new ArrayList<Card>();
	}
	
	/**
	 * Adds a card to the hand
	 */
	public boolean addCard (Card cardIn)
	{
		return cards.add(cardIn);
	}
	
	/**
	 * @param cardType type of cards in question
	 * @return a list of cards in this hand of a given type
	 */
	public ArrayList<Card> getCards(Card.CardType cardType)
	{
		ArrayList<Card> result = new ArrayList<Card>();
		for (Card curr : this.cards)
		{
			if (CardType.ANY == cardType || curr.getCardType().equals(cardType))
			{
				result.add(curr);
			}
		}
		return result;
		
	}
	
	/**
	 * Determines if the specified card is in the hand
	 */
	public boolean findCard(String cardName)
	{
		for (Card card: cards)
		{
			if (card.getCardName().equalsIgnoreCase(cardName))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("hand contents: { ");
		for (Card c : cards)
		{
			sb.append(c.getCardName()).append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (! (obj instanceof Player)) return false;
		Hand other = (Hand) obj;
	    if (this.cards == null) {
	        if (other.cards != null)
	            return false;
	    } else if (!cards.equals(other.cards))
	        return false;

		return true;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = result*prime + (this.cards!=null?this.cards.hashCode():0);
		
		return result;
	}
}
