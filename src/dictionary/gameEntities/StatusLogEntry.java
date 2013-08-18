package dictionary.gameEntities;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * status: finished
 * @author mjp
 *
 */
public class StatusLogEntry implements Serializable, Comparable<StatusLogEntry> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2542955455120298310L;
	private String text;
	private Date time;
	private int id;
	public StatusLogEntry() {super();}
	public StatusLogEntry(String text, Date time, int id) {
		super();
		this.text = text;
		this.time = time;
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private static final DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
	@Override
	public String toString() {
		return formatter.format(this.time)+" "+text;
	}
	@Override
	public int compareTo(StatusLogEntry o) {
		int result = (int)((o.getTime().getTime()) - (this.getTime().getTime()));
		if (result == 0) result = o.getId() - this.getId();
		return result;
	}
}
