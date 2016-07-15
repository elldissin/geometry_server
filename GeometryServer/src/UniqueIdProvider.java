
public class UniqueIdProvider {

	static int id;
	
	public static int getID() {
		return ++id;
	}
}
