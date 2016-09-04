package my.games.geometry.game;

import java.awt.event.KeyEvent;
import java.net.Socket;
import java.util.ArrayList;

import my.games.geometry.events.GameEvent;
import my.games.geometry.events.MoveEvent;
import my.games.geometry.events.ShootEvent;
import my.games.geometry.events.TurnEventCCW;
import my.games.geometry.events.TurnEventCW;
import my.games.geometry.networking.ConnectedClient;
import my.games.geometry.networking.ConnectionWaiter;
import my.games.geometry.networking.NetworkMessage;
import my.games.geometry.networking.PlayerInput;

public class Application {
	ArrayList<Socket> socketList = new ArrayList<Socket>();
	ArrayList<ConnectedClient> clientList = new ArrayList<ConnectedClient>();

	public static void main(String[] args) {
		Application myServer = new Application();
		Socket newConnect;
		ConnectionWaiter waiter = new ConnectionWaiter();
		while (true) {
			if ((newConnect = waiter.acceptConnection()) != null) {
				myServer.socketList.add(newConnect);
				ConnectedClient client = new ConnectedClient(newConnect);
				myServer.clientList.add(client);
			}
			myServer.pollAndNotifyClients();
			try {
				Thread.sleep(5); // To reduce CPU load
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void pollAndNotifyClients() {
		PlayerInput input;
		closeObsoleteClients();
		for (int i = 0; i < clientList.size(); i++) {
			input = clientList.get(i).getInput();
			if (input != null) {
				for (int j = 0; j < clientList.size(); j++) {
					clientList.get(j).sendMessage(responceFromInput(input));
				}
			}
		}

	}

	public void closeObsoleteClients() {
		for (int i = 0; i < clientList.size(); i++) {
			if (!clientList.get(i).isConnected()) {
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