package server.ServerClient;
import server.Server.AbstractServer ;
import server.Server.ConnectionToClient ;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class SimpleChatServer extends AbstractServer {

	public SimpleChatServer(int port) {
		super(port);
	}


	ArrayList<String> newQuestion = new ArrayList<String>();
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

		try {
			// here we get the operation and call for Commands class with its enum
			client.sendToClient("here we get the operation and call for Commands class with its enum");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	  }
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		// TODO Auto-generated method stub
		
		System.out.println("Client Disconnected.");
		super.clientDisconnected(client);
	}
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		System.out.println("Client connected: " + client.getInetAddress());
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Required argument: <port>");
			SimpleChatServer server = new SimpleChatServer(Integer.parseInt("1000"));
			server.listen();
		} else {
			SimpleChatServer server = new SimpleChatServer(Integer.parseInt(args[0]));
			server.listen();
		}
	}
}
