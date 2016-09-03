package my.games.geometry.game;

import java.io.IOException;
import java.net.Socket;

import my.games.geometry.UniqueIdProvider;
import my.games.geometry.networking.ClientConnection;
import my.games.geometry.networking.NetworkMessage;
import my.games.geometry.networking.PlayerInput;

//TODO add code to close connections on exceptions
public class ConnectedClient {
	private int clientID;
	private ClientConnection connection;

	public ConnectedClient(Socket socket, Application server) {
		clientID = UniqueIdProvider.getID();
		try {
			connection = new ClientConnection(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(connection).start();
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public void sendMessage(NetworkMessage message) {
		if (connection != null) {
			try {
				connection.getOutputStream().writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public PlayerInput getInput() {
		return connection.getInput();
	}

	public boolean isConnected() {
		return connection.isConnected();
	}

}
