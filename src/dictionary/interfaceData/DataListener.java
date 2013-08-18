package dictionary.interfaceData;

import java.io.*;


/**
 * Status: finished, but untested
 * 
 * Implementation taken from: http://lycog.com/java/tcp-object-transmission-java/
 * http://download.oracle.com/javase/tutorial/networking/sockets/examples/KKMultiServerThread.java
 * and
 * http://java.sun.com/developer/technicalArticles/ALT/sockets/
 */
public class DataListener extends Thread {
	
	private DataReceiver receiver;
	private ObjectInputStream in;
	
	public DataListener(DataReceiver receiverIn, ObjectInputStream in) {
		receiver = receiverIn;
		this.in = in;
	}
	
	public void run()
	{
		try {
			while (true) {
//				System.out.println("Waiting for data....");

				NetworkDataWrapper d = (NetworkDataWrapper) in.readObject();
//				System.out.println("Data Listener RECEIVED: "+d.toString());
				receiver.receiveData(d);
//				System.out.println("Received data....");
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe);
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
}