package my.games.geometry.game;

import javax.swing.SwingUtilities;

public class Application {

	public static void main(String[] args) {
		Server server = new Server();
		ServerLogDisplay logDisplay = new ServerLogDisplay();
		SwingUtilities.invokeLater(logDisplay);
		server.start();
	}
}