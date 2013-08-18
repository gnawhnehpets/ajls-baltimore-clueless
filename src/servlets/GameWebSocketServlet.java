package servlets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import server.data.*;
import dictionary.interfaceData.*;
import dictionary.data.LocationIndex;
import dictionary.gameEntities.*;

//import util.HTMLFilter;

/**
 * Base WebSocketServlet.
 */
public class GameWebSocketServlet extends WebSocketServlet {


	static final boolean DEBUG = false;

	public void out(String text)
	{
		if (DEBUG)
		{
			System.out.println(this.toString()+": "+text);
		}
	}
	
	
	/*Clueless objects*/
    private CardIndex cardIndex;
    private Integer nextClientID = 0;
    private ArrayList<Player> allPlayers;
    private ArrayList<Player> orderedPlayerList = new ArrayList<Player>();
	private ArrayList<Player> losers = new ArrayList<Player>();
    private GameState latestGameState;
	private int runningMessageId = 0;
	private SuggestOrAccuseData suggestion;
	private int currentPlayerIndex;
	HashMap <Player, ArrayList<StatusLogEntry>> statusLogEntriesByPlayer = new HashMap<Player, ArrayList<StatusLogEntry>>();
	HashMap <String, Integer> UserNameToClientID = new HashMap<String, Integer>();
    
	private static final String MOVE_PROMPT = "Please select room to move to.";
	private static final String ACCUSE_PROMPT = "Please make an accusation or pass.";
	
	
    private static final long serialVersionUID = 1L;

    private static final String GUEST_PREFIX = "Guest";

    private final AtomicInteger connectionIds = new AtomicInteger(0);
    private final Set<ChatMessageInbound> globalConnections =
            new CopyOnWriteArraySet<ChatMessageInbound>();

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol,
            HttpServletRequest request) {
    	System.out.println("HEEEEY!");
        return new ChatMessageInbound(connectionIds.incrementAndGet(), (String)request.getAttribute("username"));
    }

    private final class ChatMessageInbound extends MessageInbound {

        private final String nickname;
        

        private ChatMessageInbound(int id, String username) {
            this.nickname = username;
        }

        @Override
        protected void onOpen(WsOutbound outbound) {
        	globalConnections.add(this);
            String message = String.format("* %s %s",
                    nickname, "has joined.");
            globalBroadcast(message);
        }

        @Override
        protected void onClose(int status) {
        	globalConnections.remove(this);
            String message = String.format("* %s %s",
                    nickname, "has disconnected.");
            globalBroadcast(message);
        }

        @Override
        protected void onBinaryMessage(ByteBuffer message) throws IOException {
            throw new UnsupportedOperationException(
                    "Binary message not supported.");
        }

        @Override
        protected void onTextMessage(CharBuffer message) throws IOException {
        	
        	String messStr = message.toString();
        	String splitMsg[] = messStr.split("$!$");
        	String userName = splitMsg[0];
        	//null if userName does not exist yet, else returns clientID
        	Integer clientID = UserNameToClientID.get(userName);
        	        	
        	String command = splitMsg[1];
        	String retMsg = "";
        	Player p;
        	
        	//add method invocations here based on the message received? -JL
        	switch(command) {        	
        	//Expecting "userName$!$addClient"
	        case "addClient":
	        	if (clientID == null){
	        		UserNameToClientID.put(userName, ++nextClientID);
	        		allPlayers.add(new Player((Integer)UserNameToClientID.get(userName)));
		        	out("added " + userName + " to game");
	        		retMsg = "success";		        		
	        	}
	        	else{
	        		retMsg = "failure$!$Failed to add " + userName + ". Name already exists as client ID " + UserNameToClientID.get(userName);
	        	}
	        	break;
	        //Expecting "userName$!$startGame"
	        case "startGame":
	        	dealCardsToPlayers();
	        	out("Dealt cards");
	        	assignCharactersToPlayers();
	        	out("Assigned Random ClueLess character");
	        	latestGameState = createInitialGameState();
	        	out("Initialezed Game");
	        	retMsg = "success$!$"+orderedPlayerList.get(currentPlayerIndex).getCharacter().getName()+"'s turn";
	        	break;
	        //Expecting "userName$!$getPlayerInfo"
	        case "getPlayerInfo":
	        	if(clientID == null){
	        		retMsg = "failure$!$"+userName + " does not exist";
	        	}
	        	else{
	        		p = getPlayer(clientID);
	        		if(p!= null)
	        			retMsg = "success$!$"+p.getCharacter().getName();
	        		else
	        			retMsg = "failure$!$client ID " + clientID+ " does not exist";
	        	}
	        	break;	
        	//Expecting "userName$!$getAvailableMoves"
	        case "getAvailableMoves":
	        	if(clientID == null){
	        		retMsg = "failure$!$"+userName + " does not exist";
	        	}
	        	else{
	        		p = getPlayer(clientID);
	        		if(p!= null){
	        			ArrayList<Location> validMoves = getValidMoves(p);
	        			retMsg = "success";
	        			for(Location l:validMoves)
	        				retMsg += "$!$" + l.getName();
	        		}
	        		else	
	        			retMsg = "failure$!$client ID " + clientID+ " does not exist";
	        	}
	        	break;	
	        //Expecting one of the following text messages
	        //"userName$!$takeTurn$!$move$!$Location"
	        //"userName$!$takeTurn$!$suggest$!$ClueCharacter$!$Weapon$!$Room"
	        //"userName$!$takeTurn$!$accuse$!$ClueCharacter$!$Weapon$!$Room"
	        //"userName$!$takeTurn$!$endTurn"
	        case "takeTurn":
	        	if(clientID == null){
	        		retMsg = "failure$!$"+userName + " does not exist";
	        	}
	        	else{
	        		p = getPlayer(clientID);
	        		if(p!=null){
	        			//verify that it is this player's turn
	        			if(p.equals(orderedPlayerList.get(currentPlayerIndex))){
	        				//switch on action
	        				switch(splitMsg[2]){	    
	        				case "move":
	    	        			ArrayList<Location> validMoves = getValidMoves(p);
	    	        			String debugMove = "";	    	        			
	    	        			for(Location loc:validMoves){
	    	        				if(loc.getName().equals(splitMsg[3])){
	    	        					handleMove(new PlayerMoveData(p, loc));
	    	    	        			retMsg = "success";
	    	    	        			debugMove += loc.getName() +" ";
	    	    	        		}
	    	        			}
	    	        			if(!retMsg.equals("success")){
	    	        				retMsg = "failure$!$"+splitMsg[3] + " is not a valid move. Please choose one of the following: " + debugMove;
	    	        			}	
	    	        			break;	    	        			
	        				case "suggest":
	        					retMsg = handleSuggest(new SuggestOrAccuseData(p, CharacterIndex.getCharacterByName(splitMsg[3]), WeaponIndex.getWeaponByName(splitMsg[4]), LocationIndex.getRoomByName(splitMsg[5]),false,false));
	        					break;
	        				case "accuse":
	        					retMsg = handleAccusation(new SuggestOrAccuseData(p, CharacterIndex.getCharacterByName(splitMsg[3]), WeaponIndex.getWeaponByName(splitMsg[4]), LocationIndex.getRoomByName(splitMsg[5]),true,false));
	        					break;
	        				case "endTurn":
	        					currentPlayerIndex ++;
	        					//check index
	        					if(currentPlayerIndex == orderedPlayerList.size())
	        						currentPlayerIndex = 0;
	        					retMsg = "success$!$" + p.getCharacter().getName()+ " has ended turn. " + orderedPlayerList.get(currentPlayerIndex).getCharacter().getName() + "'s turn.";
	        					break;
	        				default:	        					
	        				}
	        			}
	        			else{
	        				retMsg  = "failure$!$Not your turn. It is " + orderedPlayerList.get(currentPlayerIndex) +"'s turn.";
	        			}     				        		
	        		}
	        		else	
	        			retMsg = "failure$!$client ID " + clientID+ " does not exist";
	        	}
	        	break;
	        
	        default:
	        	globalBroadcast(retMsg);
	        }        	        			
        }


		private void globalBroadcast(String message) {
            for (ChatMessageInbound connection : globalConnections) {
                try {
                    CharBuffer buffer = CharBuffer.wrap(message);
                    connection.getWsOutbound().writeTextMessage(buffer);
                } catch (IOException ignore) {
                    // Ignore
                }
            }
        }
    }
    private ArrayList<Location> getValidMoves(Player p)
    {
		ArrayList<Location> possibleLocs = p.getCharacter().getLocation().getAllConnections();
		ArrayList<Location> validMoves = new ArrayList<Location>();
		for(Location loc: possibleLocs){
			for(Player otherPlayer: allPlayers){
				if(!p.equals(otherPlayer)){
					if(!loc.equals(otherPlayer.getCharacter().getLocation()));
						validMoves.add(loc);					
				}
			}
		}
		return validMoves;
    }
    
    private Player getPlayer(Integer id){
    	for(int i = 0; i < allPlayers.size(); i++)
    	{
    		if (allPlayers.get(i).getID() == id){
    			return allPlayers.get(i);
    		}
    	}
    	return null;
    	
    }
	/**
	 * finished
	 * Randomly provides all non-case-file cards to players
	 */
	private void dealCardsToPlayers() 
	{
		cardIndex = new CardIndex();
	
		int playerIndex = 0;
		
		ArrayList<Card> deck = cardIndex.getCards();

		// initialize the player's hands
		for (Player currPlayer : allPlayers)
		{
			currPlayer.playerCards = new Hand();
		}
		
		// iterate through cards, passing each to next player's hand (in order) until finished 
		for (Card currCard : deck)
		{
			allPlayers.get(playerIndex).playerCards.addCard(currCard);
			playerIndex = (playerIndex + 1 ) % allPlayers.size();
		}
	}
	/**
	 * finished
	 * Randomly assigns a character to every player
	 */
	private void assignCharactersToPlayers()
	{
		// don't shuffle the original list of characters because their 
		// ordering indicates player turn.
		// instead make a copy and shuffle the copy, then assign to players
		ArrayList<ClueCharacter> characters = new ArrayList<ClueCharacter>();
		characters.addAll(CharacterIndex.getCharacters());
		Collections.shuffle(characters, new Random(System.currentTimeMillis()));
		Iterator<ClueCharacter> characterIter = characters.iterator();
		for (Player currPlayer : allPlayers)
		{
			// assumes that we always have enough characters
			currPlayer.character = characterIter.next();
		}
		
		// create an ordered list of the players, 
		// ordered by character id's which is established by CharacterIndex
		orderedPlayerList.addAll(allPlayers);
		Collections.sort(orderedPlayerList, new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				return o1.character.getId() - o2.character.getId();
			}
		});
	}
	
	private GameState createInitialGameState() {
		Player currPlayer = orderedPlayerList.get(0);
		
		String turnStatus = "Awaiting move from "
				+ currPlayer.character.getName();
		
		// the first game state always has the following conditions:
		// next action is a player move
		// the default character positions from the character index are valid
		// the default weapon positions from the weapon index are valid
		GameState state = new GameState(currPlayer,
				UserActionTypes.PLAYER_MOVE, CharacterIndex.getCharacters(),
				WeaponIndex.getWeapons(), MOVE_PROMPT,
				turnStatus);
		
	
		return state;
	}
	/*
	 * Creates a new game state, using the last distributed game state, except
	 * for a change in the player location due to player move data
	 */
	private GameState createNewGameState(PlayerMoveData data) {
		GameState update = new GameState(this.latestGameState);
		out(data.getNewLocation().toString());
		out(data.getMovingPlayer().toString());
		movePlayerWithinGameState(data.getMovingPlayer().character, update, data.getNewLocation());
		return update;
	}
	private void movePlayerWithinGameState(ClueCharacter characterIn, GameState updateIn, Location loc)
	{
		characterIn.setLocation(loc);
		int i = 0;
		for (ClueCharacter character : updateIn.charactersPositions)
		{
			if (characterIn.getId() == character.getId())
			{
				updateIn.charactersPositions.set(i, new ClueCharacter(characterIn));
			}
			i++;
		}
	}
	public String handleMove(PlayerMoveData data)
	{
		String retMsg = "";
		out("handling Player Move");
		out("Player "+data.getMovingPlayer().character.getName()+" moved to "+data.getNewLocation().getName());
		GameState update = createNewGameState(data);

		update.currentPlayer = data.getMovingPlayer();
		
		// handle stay
		update.currentPlayer.character.setMovedBySuggestion(false);
		int prevLocation = -1;
		for (ClueCharacter c : this.latestGameState.charactersPositions)
		{
			if (c.getId() == data.getMovingPlayer().character.getId())
			{
				prevLocation = c.getLocation().getId();
			}
		}
		if (data.getNewLocation().getId() == prevLocation)
		{
			addStatusLogEntryForAll(data.getMovingPlayer().character.getName()+
					" stayed in the\n"+
					data.getNewLocation().getName());
		}
		else 
		{
			// add status Log entry for everyone
			retMsg = data.getMovingPlayer().character.getName()+
					" moved to\n"+
					data.getNewLocation().getName();
			addStatusLogEntryForAll(data.getMovingPlayer().character.getName()+
					" moved to\n"+
					data.getNewLocation().getName());
		}
		// end handle stay
		
		// determine if the move was to a hallway
		if (data.getNewLocation().getClass() == Hallway.class) 
		{
			// current Player gets a chance to accuse
			Player nextPlayer = this.latestGameState.currentPlayer;
			update.pendingAction = UserActionTypes.ACCUSE;
			update.currentPlayer = nextPlayer;
			update.nextActionText = ACCUSE_PROMPT;
			update.playerTurnStatus = "Awaiting accusation from "+
					nextPlayer.character.getName()+".";
		}
		else
		{
			// keep current player as-is
			update.pendingAction = UserActionTypes.SUGGEST;
			update.nextActionText = "Please make a suggestion.";
			update.playerTurnStatus = "Awaiting suggestion from "+
					update.currentPlayer.character.getName()+".";
		}

		out("New game state: " + this.latestGameState.toString());
		return retMsg;
	}
	/**
	 * finished
	 * @param entryText adds the entryText to all player's logs
	 */
	private void addStatusLogEntryForAll(String entryText) {
		StatusLogEntry newEntry = new StatusLogEntry(entryText,
				new Date(System.currentTimeMillis()), this.runningMessageId++);
		for (ArrayList<StatusLogEntry> currList : this.statusLogEntriesByPlayer.values()) {
			currList.add(newEntry);
		}
	}
	
	public String handleSuggest(SuggestOrAccuseData data)
	{
		String retMsg = "";
		// save this data for later
		this.suggestion = data;
		GameState updatedState = new GameState(this.latestGameState);
		Player disproverFound = null;
		
		// update the event log, with suggestion notification
		String logMessage = data.getSuggestingPlayer().character.getName()+
								" suggests:\n"+
								data.getSuspect().getName()+
								" in the "+
								data.getRoom().getName()+
								"\n with the "+
								data.getWeapon().getName();
		out(logMessage);
		addStatusLogEntryForAll(logMessage);
		// change the location of the implicated character
		
		data.getSuspect().setMovedBySuggestion(true);
		// move the object
		movePlayerWithinGameState(data.getSuspect(), updatedState, 
				LocationIndex.getLocationByName(data.getRoom().getName()));
		
		int j = 0;
		for (Player p : orderedPlayerList)
		{
			if (p.character.equals(data.getSuspect()))
			{
				Player p2 = new Player(p.playerId);
				// changes the location
				p2.character = data.getSuspect();
				p2.playerCards = p.playerCards;
				orderedPlayerList.set(j, p2);
			}
			j++;
		}
		
		retMsg +=data.getSuspect().getName() + " has been moved to the "+ data.getRoom().getName()+ ". ";
		
		
		// change the location of the implicated weapon
		Weapon weapon = data.getWeapon();
		weapon.setLocation(data.getRoom());
		
		retMsg +=data.getWeapon().getName() + " has been moved to the "+ data.getRoom().getName()+ ". ";
		
		
		// start looking at the next player, through the last player
		int startIndex = orderedPlayerList.indexOf(data.getSuggestingPlayer());
		// - look at all the player's hands, in player order
		for (int i = 1; null == disproverFound && i < this.allPlayers.size(); i++)
		{
			Player possibleDisprover = 
					orderedPlayerList.get((i+startIndex) % orderedPlayerList.size());
			if (possibleDisprover.playerId != this.suggestion.getSuggestingPlayer().playerId)
			{
				// - determine if player can disprove this suggestion
				if (possibleDisprover.playerCards.findCard(data.getRoom().getName()) ||
					possibleDisprover.playerCards.findCard(data.getSuspect().getName()) ||
					possibleDisprover.playerCards.findCard(data.getWeapon().getName()))
				{
					disproverFound = possibleDisprover;
				}
			}
		}
		
		// a suggester may have only selected cards within their own hand or 
		// within the case file
		if (null == disproverFound)
		{
			this.addStatusLogEntryForAll(
					"No players could disprove\n the suggestion of\n"+
					data.getSuggestingPlayer().character.getName());		
			updatedState.currentPlayer = this.suggestion.getSuggestingPlayer();
			updatedState.pendingAction = UserActionTypes.ACCUSE;
			updatedState.nextActionText = ACCUSE_PROMPT; 
			updatedState.playerTurnStatus = 
					updatedState.currentPlayer.character.getName()+
					"'s turn\n to make an accusation.";
			retMsg = "success$!$DisprovalCard:none$!$Message:" + "No cards found to disprove " +orderedPlayerList.get(currentPlayerIndex).getCharacter().getName()+ " "+retMsg;
			
		}
		// a possible disprover was found 
		else
		{
			// if a user can disprove, create a gamestate that tells the player to disprove
			updatedState.currentPlayer = disproverFound;
			updatedState.pendingAction = UserActionTypes.DISPROVE;
			
			//automatically choose card for disproval
			ArrayList<String> disproval = new ArrayList<String>();
			if(disproverFound.playerCards.findCard(data.getRoom().getName()))
					disproval.add(data.getRoom().getName());
			if(disproverFound.playerCards.findCard(data.getSuspect().getName()))
				disproval.add(data.getSuspect().getName());
			if(disproverFound.playerCards.findCard(data.getWeapon().getName()))
				disproval.add(data.getWeapon().getName());
			Random rand = new Random(); 
			int i = rand.nextInt(disproval.size());
			retMsg = "success$!$DisprovalCard:"+disproval.get(i) + "$!$Message:" + disproverFound.getCharacter().getName() + " showed the " + disproval.get(i) + " to " + orderedPlayerList.get(currentPlayerIndex).getCharacter().getName()+ " "+retMsg;
		}
		return retMsg;
	}
	public String handleAccusation(SuggestOrAccuseData data)
	{
		String retMsg ="";
		// the player could choose not to accuse
		/*
		if (data.isPass())
		{
			addStatusLogEntryForAll(data.getSuggestingPlayer().character.getName()+" chose not to\n make an accusation.");

			// send a game state, giving next player chance to move
			GameState update = new GameState(this.latestGameState);
			update.currentPlayer = determinePlayerTurn(data.getSuggestingPlayer());
			update.nextActionText = MOVE_PROMPT;
			update.pendingAction = UserActionTypes.PLAYER_MOVE;
			update.playerTurnStatus = "Awaiting move from "+update.currentPlayer.character.getName();
			NetworkDataWrapper message = new NetworkDataWrapper();
			notifyClients(message, update);
		}
		*/
		// if an accusation took place
		//else
		{
			// verify accusation against case file
			if (cardIndex.caseFile.isMatch(data.getWeapon(), data.getSuspect(), data.getRoom()))
			{
				retMsg = "success$!$correct$!$continue";
				out("end game!!!!");
				// end the game.  the suggesting player is the winner
			}
			else
			{
				// if incorrect, 
				// - inform accusing user of case file contents via  
				// event log message to only that user

				retMsg = "success$!Sincorrect";
				this.statusLogEntriesByPlayer.get(data.getSuggestingPlayer()).add(
						new StatusLogEntry("Incorrect accusation. Case file: "+
								cardIndex.caseFile.toString(), 
								new Date(System.currentTimeMillis()), this.runningMessageId++));
				
				// - inform other players that this player made an incorrect accusation
				addStatusLogEntryForAll(data.getSuggestingPlayer().character.getName()+" loses \nwith an incorrect accusation.");
				
				// - add the accusing player to the list of losers, meaning that this 
				// user can never again move, suggest or accuse.  They can still disprove.
				losers.add(data.getSuggestingPlayer());
				
				out("------CHECK for end of game--------");
				out(losers.toString());
				out(orderedPlayerList.toString());
				// if bad accusation means all but one player lost, end game.
				if (losers.size() == (orderedPlayerList.size() - 1))
				{
					// determine winner
					Player winner = null;
					for (Player curr : orderedPlayerList)
					{
						if (! losers.contains(curr)) winner = curr;
						out("wtf: "+ winner);
					}
					// end the game
					out("wtf");
					out("wtf");
					retMsg +="$!$end$!$Message: " + winner.getCharacter().getName() + " wins by default - all other players have lost";
				}
				else
				{
					// send a game state out that shows no active player
					GameState update = new GameState(this.latestGameState);
					update.currentPlayer = determinePlayerTurn(data.getSuggestingPlayer());
					update.nextActionText = MOVE_PROMPT;
					update.pendingAction = UserActionTypes.PLAYER_MOVE;
					update.playerTurnStatus = "Awaiting move from "+update.currentPlayer.character.getName();
					retMsg +="$!$continue$!$Message: " + orderedPlayerList.get(currentPlayerIndex).getCharacter().getName() + " is out of the game";
					
				}
			}
		}
		return retMsg;
	}

	private Player determinePlayerTurn(Player playerIn) {
		int index = this.orderedPlayerList.indexOf(playerIn);
		Player result = this.orderedPlayerList.get((index+1)%orderedPlayerList.size());
		// skip losers that made incorrect accusations
		if (losers.contains(result))
		{
			result = determinePlayerTurn(result);
		}
		return result;
	}
}