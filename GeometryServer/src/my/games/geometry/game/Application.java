package my.games.geometry.game;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import geometry.networking.NetworkMessage;
import geometry.networking.PlayerInput;
import geometry.networking.events.GameEvent;
import geometry.networking.events.MoveEvent;
import geometry.networking.events.ShootEvent;
import geometry.networking.events.TurnEventCCW;
import geometry.networking.events.TurnEventCW;

public class Application {
	int portNumber = 4444;
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	ArrayList<Socket> socketList = new ArrayList<Socket>();
	ArrayList<ClientServiceThread> clientList = new ArrayList<ClientServiceThread>();
	NetworkMessage serverResponce;
	boolean hasNewEvents = false;

	public static void main(String[] args) {
		Application myServer = new Application();
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

	public synchronized void setMessage(PlayerInput inputFromPlayer) {
		serverResponce = responceFromInput(inputFromPlayer);
		// hasNewEvents=true;
		notifyAllClients();
	}

	public synchronized NetworkMessage getMessage() {
		hasNewEvents = false;
		return serverResponce;
	}

	public synchronized boolean hasNewEvents() {
		return hasNewEvents;
	}

	private void notifyAllClients() {
		for (int i = 0; i < clientList.size(); i++)
			try {
				clientList.get(i).getOutputStream().writeObject(serverResponce);
				// System.out.println("Event sent to client:
				// "+clientList.get(i).getClientID());
			} catch (IOException e) {
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

	private NetworkMessage responceFromInput(PlayerInput input) {
		GameEvent ev;
		NetworkMessage msg = new NetworkMessage();

		switch (input.getKeyCode()) {
		case KeyEvent.VK_W:
			ev = new MoveEvent(1);
			msg.setEvent(ev);
			break;
		case KeyEvent.VK_D:
			ev = new TurnEventCW(1);
			msg.setEvent(ev);
			break;
		case KeyEvent.VK_A:
			ev = new TurnEventCCW(1);
			msg.setEvent(ev);
			break;
		case KeyEvent.VK_Q:
			ev = new ShootEvent(1);
			msg.setEvent(ev);
			break;
		case KeyEvent.VK_UP:
			ev = new MoveEvent(2);
			msg.setEvent(ev);
			break;
		case KeyEvent.VK_RIGHT:
			ev = new TurnEventCW(2);
			msg.setEvent(ev);
			break;
		case KeyEvent.VK_LEFT:
			ev = new TurnEventCCW(2);
			msg.setEvent(ev);
			break;
		case KeyEvent.VK_CONTROL:
			ev = new ShootEvent(2);
			msg.setEvent(ev);
			break;
		}
		return msg;
	}
}