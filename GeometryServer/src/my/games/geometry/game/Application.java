package my.games.geometry.game;

import javax.swing.SwingUtilities;

public class Application {

	public static void main(String[] args) {
		ServerLogDisplay logDisplay = new ServerLogDisplay();
		SwingUtilities.invokeLater(logDisplay);

		Server server = new Server();
		server.start();
	}
}