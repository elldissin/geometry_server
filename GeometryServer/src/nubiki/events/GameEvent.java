package nubiki.events;

import java.io.Serializable;

public abstract class GameEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum EventType {STOP, MOVE, TURNCW, TURNCCW}
	protected int targetID;
	protected EventType eventType;
	public GameEvent(int targetID) {
		this.targetID = targetID;
	}


	public abstract EventType doEvent();
	
	public int getTargetID() {
		return targetID;
	}
}
