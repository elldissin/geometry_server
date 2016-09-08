package my.games.geometry.game;

import java.awt.event.KeyEvent;
import java.net.Socket;
import java.util.ArrayList;

import my.games.geometry.events.GameEvent;
import my.games.geometry.events.MoveEvent;
import my.games.geometry.events.ShootEvent;
import my.games.geometry.events.TurnEventCCW;
import my.games.geometry.events.TurnEventCW;
import my.games.geometry.networking.ClientService;
import my.games.geometry.networking.ConnectedClient;
import my.games.geometry.networking.NetworkMessage;
import my.games.geometry.networking.PlayerInput;

public class Application {
	ArrayList<Socket> socketList = new ArrayList<Socket>();
	ArrayList<ConnectedClient> clientList = new ArrayList<ConnectedClient>();
	public ClientService clientService;

	public static void main(String[] args) {
		Application myServer = new Application();
		myServer.clientService = new ClientService();
		myServer.clientService.start();
		while (true) {
			myServer.pollAndNotifyClients(myServer.clientService);
		}
	}

	private void pollAndNotifyClients(ClientService service) {
		PlayerInput input;
		closeObsoleteClients(service);
		for (int i = 0; i < service.getClientList().size(); i++) {
			input = service.getClientList().get(i).getInput();
			if (input != null) {
				for (int j = 0; j < service.getClientList().size(); j++) {
					service.getClientList().get(j).sendMessage(responceFromInput(input));
				}
			}
		}
	}

	public void closeObsoleteClients(ClientService service) {
		for (int i = 0; i < service.getClientList().size(); i++) {
			if (!service.getClientList().get(i).isConnected()) {
				System.out.println("The following client disconnected from server: "
						+ service.getClientList().get(i).getClientID());
				service.getClientList().remove(i);
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