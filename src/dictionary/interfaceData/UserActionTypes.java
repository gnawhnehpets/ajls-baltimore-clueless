package dictionary.interfaceData;

/**
 * status: finished
 * @author mjp
 *
 */
public enum UserActionTypes {

	PLAYER_MOVE("move"), SUGGEST("suggestion"), ACCUSE("accusation"), DISPROVE("disproval");
	
	String printableName;
	UserActionTypes(String printable)
	{
		printableName = printable;
	}
	
	public String getReadableName()
	{
		return printableName;
	}
	
}
