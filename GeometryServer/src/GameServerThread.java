import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import nubiki.networking.NetworkMessage;

public class GameServerThread implements Runnable {
	Socket socket;
	GameServer server;

	public GameServerThread(Socket socket, GameServer server) {
		this.server=server;
		this.socket=socket;
	}

	@Override
	public void run() {
		try (GameClient client = new GameClient(socket);) {
			//The below created thread is continously scanning for server if it has new messages
			Thread outputThread = new Thread (new Runnable(){
				@Override
				public void run() {
					String msg=""; //TOBECLEANED
					while(!msg.equals("stop")) //TOBECLEANED
						if(server.hasNewEvents()) {
							try {
								client.getOutputStream().writeObject(server.getMessage());
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println("Event sent (Server)");
						}
				}});
			outputThread.start();
			//The below is continously scanning for new input from clients
			NetworkMessage inputEvent = null;
			while ((inputEvent = (NetworkMessage)client.getInputStream().readObject()) != null) {
				System.out.println("Event received (Server)");
				server.setMessage(inputEvent);
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
