import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import nubiki.networking.NetworkMessage;

public class GameClient implements AutoCloseable, Runnable {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int clientID;
	private Socket socket;
	private GameServer server;

	public GameClient(Socket socket, GameServer server) throws IOException {
		clientID=UniqueIdProvider.getID();
		this.socket=socket;
		this.server=server;
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
		if(out!=null)
			out.close();
		if(in!=null)
			in.close();
		if(socket!=null)
			socket.close();
	}

	@Override
	public void run() {
//		//The below created thread is continously scanning for server if it has new messages
//		Thread outputThread = new Thread (new Runnable(){
//			@Override
//			public void run() {
//				String msg=""; //TOBECLEANED
//				while(!msg.equals("stop")) //TOBECLEANED
//					if(server.hasNewEvents()) {
//						try {
//							out.writeObject(server.getMessage());
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						System.out.println("Event sent (Server)");
//					}
//			}});
//		outputThread.start();
		//The below is continously scanning for new input from clients
		NetworkMessage inputEvent = null;
		try {
			while ((inputEvent = (NetworkMessage)in.readObject()) != null) {
//				System.out.println("Event received (Server)");
				server.setMessage(inputEvent);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
