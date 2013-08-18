package server.data;

import dictionary.gameEntities.*;

/**
 * Status: Code finished
 * 
 * @author mjp
 *
 */
public class CaseFile {

	private WeaponCard weapon;
	private CharacterCard murderer;
	private RoomCard room;
	
	public CaseFile(WeaponCard weaponCard, CharacterCard murdererCard, RoomCard roomCard)
	{
		System.out.println("CASE FILE CONTENTS");
		weapon = weaponCard;
		murderer = murdererCard;
		room = roomCard;
		System.out.println(toString());
	}

	/**
	 * @param room
	 * @param suspect
	 * @param weapon
	 */
	public boolean isMatch(Weapon weapon2, ClueCharacter clueCharacter, Room room2)
	{
		return 	weapon.getWeapon().equals(weapon2) && 
				murderer.getCharacter().equals(clueCharacter) && 
				room.getRoom().equals(room2);
	}
	
	@Override
	public String toString() {
		return getCaseFileContents();
	}
	/**
	 * 
	 * @return a string containing a sentence in the format: MurdererX in the RoomY with the WeaponZ.
	 */
	public String getCaseFileContents()
	{
		StringBuffer result = new StringBuffer();
		result.append(murderer.getCardName())
			  .append(" in the ")
			  .append(room.getCardName())
			  .append(" with the ")
			  .append(weapon.getCardName())
			  .append(".");
		return result.toString();
	}
	
}
