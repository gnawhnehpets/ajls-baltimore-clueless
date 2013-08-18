package server.data;

import java.util.*;


import dictionary.data.LocationIndex;
import dictionary.gameEntities.*;

/**
 * Status: Code finished
 * 
 * Creates ArrayLists of card objects, all shuffled.
 * 
 * @author mjp
 *
 */
public class CardIndex {

	private ArrayList <Card> deckOfCardsMinusCaseFile;
	private ArrayList <WeaponCard> weaponCards;
	private ArrayList <CharacterCard> characterCards;
	private ArrayList <RoomCard> roomCards;
	public CaseFile caseFile;
	private HashMap<String, Card> cardMap = new HashMap<String, Card>();
	
	public CardIndex()
	{
		weaponCards = new ArrayList<WeaponCard>();
		characterCards = new ArrayList<CharacterCard>();
		roomCards = new ArrayList<RoomCard>();
		
		createCardsForEachType();
		shuffleCardsByType();
		createCaseFile();
		
		// populates the allCards field
		deckOfCardsMinusCaseFile = new ArrayList<Card>();
		shuffleAllCards();
	}
	
	public Card getCardByName(String nameIn)
	{
		return cardMap.get(nameIn);
	}
	/**
	 * Note that the ArrayList of each type of card is already shuffled at this point,
	 * so just getting the first entry in each ArrayList is equivalent to getting a random card.
	 */
	private void createCaseFile() {
		caseFile = new CaseFile(weaponCards.get(0), characterCards.get(0), roomCards.get(0));
	}

	public CaseFile getCaseFile()
	{
		return caseFile;
	}
	
	private void shuffleCardsByType() {
		Collections.shuffle(characterCards, new Random(System.currentTimeMillis()));
		Collections.shuffle(weaponCards, new Random(System.currentTimeMillis()));
		Collections.shuffle(roomCards, new Random(System.currentTimeMillis()));
	}

	public ArrayList<Card> getCards()
	{
		return deckOfCardsMinusCaseFile;
	}

	public ArrayList<WeaponCard> getWeaponCards() {
		return weaponCards;
	}

	public ArrayList<CharacterCard> getCharacterCards() {
		return characterCards;
	}

	public ArrayList<RoomCard> getRoomCards() {
		return roomCards;
	}
	
	private void createCardsForEachType() 
	{
		for (ClueCharacter curr : CharacterIndex.getCharacters())
		{
			CharacterCard card = new CharacterCard(curr);
			characterCards.add(card);
			cardMap.put(card.getCardName(), card);
		}
		
		for (Weapon curr : WeaponIndex.getWeapons())
		{
			WeaponCard card = new WeaponCard(curr);
			weaponCards.add(new WeaponCard(curr));
			cardMap.put(card.getCardName(), card);
		}

		for (Room curr : LocationIndex.getAllRooms())
		{
			RoomCard card = new RoomCard(curr);
			roomCards.add(new RoomCard(curr));
			cardMap.put(card.getCardName(), card);
		}
		
	}
	
	/**
	 * Shuffles the non-case-file cards, putting them into allCards
	 */
	private void shuffleAllCards() {
		//skip index 0, because index 0 is the case file card
		//character
		for (int i = 1; i < characterCards.size(); i++)
			deckOfCardsMinusCaseFile.add(new CharacterCard(characterCards.get(i)));
		//weapon
		for (int i = 1; i < weaponCards.size(); i++)
			deckOfCardsMinusCaseFile.add(new WeaponCard(weaponCards.get(i)));
		//room
		for (int i = 1; i < roomCards.size(); i++)
			deckOfCardsMinusCaseFile.add(new RoomCard(roomCards.get(i)));
		
		//shuffle
		Collections.shuffle(deckOfCardsMinusCaseFile, new Random(System.currentTimeMillis()));
	}
}
