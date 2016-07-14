import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import nubiki.networking.NetworkMessage;

public class GameServerThread implements Runnable {
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	GameServer server;
	
	public GameServerThread(Socket socket, GameServer server) {
		this.server=server;
		this.socket=socket;
	}

	@Override
	public void run() {
		try (
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				) {
			this.out=out;
			this.in=in;
//			out.writeObject();
//			System.out.println("Greeting to client sent");

			//The below created thread is continously scanning for server if it has new messages
			Thread outputThread = new Thread (new Runnable(){
				@Override
				public void run() {
					String msg=""; //TOBECLEANED
					while(!msg.equals("stop")) //TOBECLEANED
						if(server.hasNewEvents()) {
//							msg=server.getKeyEvent()().getMessage(); //TOBECLEANED
							try {
								out.writeObject(server.getMessage());
//								System.out.println("Event sent (Server)");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}});
			outputThread.start();
			
			//The below is continously scanning for new input from clients
			NetworkMessage inputEvent = null;
			while ((inputEvent = (NetworkMessage)in.readObject()) != null) {
//				System.out.println("Event received (Server)");
				server.setMessage(inputEvent);
			}

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
