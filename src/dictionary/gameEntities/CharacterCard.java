package dictionary.gameEntities;

import java.io.Serializable;

/**
 * status: finished
 * @author mjp
 *
 */
public class CharacterCard extends Card implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6980957750336972966L;
	private ClueCharacter character;
	public CharacterCard() {super();}
	public CharacterCard(ClueCharacter characterIn) {
		super(characterIn.getName(), characterIn.getId());
		character = characterIn;
	}

	public CharacterCard(CharacterCard other) {
		super(other.getCardName(), other.getCardId());
		character = other.getCharacter();
	}
	
	public ClueCharacter getCharacter()
	{
		return character;
	}

	@Override
	public CardType getCardType() {
		return CardType.CHARACTER;
	}

}
