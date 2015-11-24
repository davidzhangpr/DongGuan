package markettracker.data;


public class DataPool   {
	private static DataPool s_instance = null;

	public static DataPool getInstance() {
		if (s_instance == null) {
			s_instance = new DataPool();
		}
		return s_instance;
	}
	


}