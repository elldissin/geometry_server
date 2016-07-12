import java.net.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.io.*;

public class GameServer {
	int portNumber = 4444;
	ServerSocket serverSocket=null;
	Socket clientSocket=null;
	ArrayList<Socket> socketList = new ArrayList<Socket>();
	ArrayList<GameServerThread> threadList = new ArrayList<GameServerThread>();
	KeyEvent keyEvent;
	boolean hasNewEvents=false;
	
	public static void main(String[] args) {
		GameServer myServer = new GameServer();
		try {
			myServer.serverSocket = new ServerSocket(myServer.portNumber);
			
			while (true) { //fix this to stop somehow
				System.out.println("Waiting for new connection...");
				myServer.clientSocket = myServer.serverSocket.accept();
				myServer.socketList.add(myServer.clientSocket);
				System.out.println("New connection accepted from " + myServer.clientSocket.getInetAddress());
				GameServerThread st=new GameServerThread(myServer.clientSocket, myServer);
				new Thread(st).start();
				myServer.threadList.add(st);
				//				System.out.println("New thread for connection was started");
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ myServer.portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
		finally {
			try {
				for(int i=0;i<myServer.socketList.size();i++)
					myServer.socketList.get(i).close();
				myServer.serverSocket.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public synchronized void setKeyEvent(KeyEvent event) {
		keyEvent=event;
		hasNewEvents=true;
//		clientMessageRequestCount=threadList.size()*2;
		System.out.println("New event was received from client");
	}
//
	public synchronized KeyEvent getKeyEvent() {
//		clientMessageRequestCount--;
//		System.out.println("getting global message, clients to notify: "+clientMessageRequestCount);
//		if(clientMessageRequestCount<=0)
			hasNewEvents=false;
		return keyEvent;
	}
//
	public synchronized boolean hasNewEvents() {
		return hasNewEvents;
	}
}