package dictionary.gameEntities;

import java.io.Serializable;

/**
 * status: finished
 * @author mjp
 *
 */
public class RoomCard extends Card implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 243734931555859147L;
	private Room room;
	public RoomCard() {super();}
	public RoomCard(Room roomIn) {
		super(roomIn.getName(), roomIn.getId());
		room = roomIn;
	}
	public RoomCard(RoomCard roomIn) {
		super(roomIn.getCardName(), roomIn.getCardId());
		room = roomIn.getRoom();
	}
	@Override
	public CardType getCardType() {
		return CardType.ROOM;
	}

	public Room getRoom() {
		return room;
	}

}
