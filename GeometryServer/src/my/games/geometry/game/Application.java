package my.games.geometry.game;

import javax.swing.SwingUtilities;

import my.games.geometry.game.engine.Server;
import my.games.geometry.ui.ServerLogDisplay;

public class Application {

	public static void main(String[] args) {
		ServerLogDisplay logDisplay = new ServerLogDisplay();
		SwingUtilities.invokeLater(logDisplay);

		Server server = new Server();
		server.setLogDisplay(logDisplay);
		server.start();
	}
}