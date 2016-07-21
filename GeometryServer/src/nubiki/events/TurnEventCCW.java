package nubiki.events;

public class TurnEventCCW extends GameEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TurnEventCCW(int targetID) {
		super(targetID);
		eventType=EventType.TURNCCW;
	}
	
	@Override
	public EventType doEvent() {
		return eventType;
	}

}
