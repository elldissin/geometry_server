package my.games.geometry.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import my.games.geometry.UniqueIdProvider;
import my.games.geometry.networking.PlayerInput;

public class ClientServiceThread implements AutoCloseable, Runnable {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int clientID;
	private Socket socket;
	private Application server;
	private boolean running = false;

	public boolean isRunning() {
		return running;
	}

	public ClientServiceThread(Socket socket, Application server) throws IOException {
		clientID = UniqueIdProvider.getID();
		this.socket = socket;
		this.server = server;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public ObjectOutputStream getOutputStream() throws IOException {
		return out;
	}

	public ObjectInputStream getInputStream() throws IOException {
		return in;
	}

	@Override
	public void close() throws IOException {
		if (out != null)
			out.close();
		if (in != null)
			in.close();
		if (socket != null)
			socket.close();
	}

	@Override
	public void run() {
		running = true;
		// The below is continously scanning for new input from clients
		PlayerInput inputFromPlayer = null;
		try {
			while ((inputFromPlayer = (PlayerInput) in.readObject()) != null) {
				// System.out.println("Event received (Server)");
				server.setMessage(inputFromPlayer);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// System.out.println("Following client id stopped: " + clientID);
			running = false;
			server.closeObsoleteClients();
		}
	}
}
