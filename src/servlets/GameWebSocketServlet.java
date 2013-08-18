package servlets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

//import util.HTMLFilter;

/**
 * Base WebSocketServlet.
 */
public class GameWebSocketServlet extends WebSocketServlet {

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
        	
        	//add method invocations here based on the message received? -JL
        	switch(messStr.toString()) {
        		default:
        			globalBroadcast(messStr);
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
}