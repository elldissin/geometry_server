package my.games.geometry.game;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class ServerLogDisplay implements Runnable {

	@Override
	public void run() {
		JFrame frame = new JFrame("Server log");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setResizable(false);
		JLabel[] mLabel = new JLabel[8];
		final JTextPane[] mText = new JTextPane[8];
		for (int i = 0; i < 8; i++) {
			mText[i] = new JTextPane();
			mText[i].setBounds(0, (i * 50) + 25, 200, 20);
			mText[i].setText("0");
			frame.add(mText[i]);

			mLabel[i] = new JLabel();
			mLabel[i].setBounds(0, (i * 50), 200, 20);
			frame.add(mLabel[i]);
		}
		mLabel[0].setText("gameObjectsMap");
		mLabel[1].setText("drawableObjectList");
		mLabel[2].setText("updatableObjectList");
		mLabel[3].setText("collidableObjectList");
		mLabel[4].setText("shootersList");
		mLabel[5].setText("hz");
		mLabel[6].setText("hz");
		mLabel[7].setText("hz");

		// mText[0].setText(server.getWorld().get gameObjectsMap.getSize());
		// mText[0].setText(Integer.toString(server.getWorld().getDrawableObjectList().size()));
		// mText[0].setText(Integer.toString(server.getWorld().getUpdatableObjectList().size()));
		// mText[0].setText(Integer.toString(server.getWorld().getCollidableObjectList().size()));
		// mText[0].setText(Integer.toString(server.getWorld().getDrawableObjectList().size()));

		frame.setVisible(true);
		frame.setFocusable(true);
	}

}
