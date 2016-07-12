import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
					String msg="";
					while(!msg.equals("stop"))
						if(server.hasNewMessage()) {
							msg=server.getGlobalMessage().getMessage();
							try {
								out.writeObject(server.getGlobalMessage());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}});

			outputThread.start();
			
			//The below is continously scanning for new input from clients
			while ((inputMessage = (Message)in.readObject()) != null) {
				server.setGlobalMessage(inputMessage);
			}

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
