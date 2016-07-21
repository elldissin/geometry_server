package nubiki.events;

public class ShootEvent extends GameEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShootEvent(int targetID) {
		super(targetID);
		eventType=EventType.SHOOT;
	}
	
	@Override
	public EventType doEvent() {
		return eventType;
	}

}
