import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClient implements AutoCloseable {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int clientID;
	private Socket socket;
	
	public GameClient(Socket socket) throws IOException {
		clientID=UniqueIdProvider.getID();
		this.socket=socket;
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
}
