package my.games.geometry.game;

import java.io.IOException;
import java.net.ServerSocket;


import java.net.Socket;
import java.util.ArrayList;

import geometry.networking.NetworkMessage;

public class Server {
	int portNumber = 4444;
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	ArrayList<Socket> socketList = new ArrayList<Socket>();
	ArrayList<ClientServiceThread> clientList = new ArrayList<ClientServiceThread>();
	NetworkMessage keyEvent;
	boolean hasNewEvents = false;

	public static void main(String[] args) {
		Server myServer = new Server();
		try {
			myServer.serverSocket = new ServerSocket(myServer.portNumber);

			while (true) { // fix this to stop somehow
				System.out.println("Waiting for new connection...");
				myServer.clientSocket = myServer.serverSocket.accept();
				myServer.socketList.add(myServer.clientSocket);
				System.out.println("New connection accepted from " + myServer.clientSocket.getInetAddress());
				ClientServiceThread st = new ClientServiceThread(myServer.clientSocket, myServer);
				new Thread(st).start();
				myServer.clientList.add(st);
				// System.out.println("New thread for connection was started");
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + myServer.portNumber
					+ " or listening for a connection");
			System.out.println(e.getMessage());
		} finally {
			try {
				for (int i = 0; i < myServer.socketList.size(); i++)
					myServer.socketList.get(i).close();
				myServer.serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void setMessage(NetworkMessage event) {
		keyEvent = event;
		// hasNewEvents=true;
		notifyAllClients();
	}

	//
	public synchronized NetworkMessage getMessage() {
		// clientMessageRequestCount--;
		// System.out.println("getting global message, clients to notify:
		// "+clientMessageRequestCount);
		// if(clientMessageRequestCount<=0)
		hasNewEvents = false;
		return keyEvent;
	}

	//
	public synchronized boolean hasNewEvents() {
		return hasNewEvents;
	}

	private void notifyAllClients() {
		for (int i = 0; i < clientList.size(); i++)
			try {
				clientList.get(i).getOutputStream().writeObject(keyEvent);
				// System.out.println("Event sent to client:
				// "+clientList.get(i).getClientID());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void closeObsoleteClients() {
		for (int i = 0; i < clientList.size(); i++) {
			if (!clientList.get(i).isRunning()) {
				System.out.println("The following client disconnected from server: " + clientList.get(i).getClientID());
				clientList.remove(i);
			}
		}
	}
}