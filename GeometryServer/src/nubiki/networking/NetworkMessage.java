package nubiki.networking;
import java.io.Serializable;

public class NetworkMessage implements Serializable {
private int keyCode;
	public NetworkMessage(int keyCode) {
		this.keyCode=keyCode;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
}
