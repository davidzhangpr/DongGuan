package markettracker.data;

import java.util.*;

public class Fields {
	private HashMap<String, Object> hashMap;
	private final static String PHOTO = "photo";
	private final static String SHOTTIME = "shotTime";
	
	private final static String PHOTONAME = "photoname";
	public void setPhotoName(String value) {
		put(PHOTONAME, value);
	}
	
	public String getPhotoName() {
		return getStrValue(PHOTONAME);
	}
	
	public String getShotTime() {
		return getStrValue(SHOTTIME);
	}
	
	public void setShotTime(String value) {
		put(SHOTTIME, value);
	}
	
	public byte[] getPhoto() {
		return (byte[])getOValue(PHOTO);
	}
	
	public void setPhoto(Object value) {
		put(PHOTO, value);
	}
	
	public Fields() {
		init();
	}

	public boolean isTrue(String key) {
		if(getStrValue(key).equals(""))
			return false;
		return Boolean.parseBoolean(getStrValue(key));
	}

	public void put(String key, Object value) {
		hashMap.put(key.toLowerCase(), value);
	}

	public void put(String key, String value) {
		hashMap.put(key.toLowerCase(), value);
	}

	public void Set(String key, String value) {
		hashMap.put(key, value);
	}

	public Object getOValue(String key) {
		return hashMap.get(key.toLowerCase());
	}



	public byte[] getBValue(String key) {
		return (byte[]) hashMap.get(key.toLowerCase());
	}

	private Object getValue(String key) {
		return hashMap.get(key);
	}

	public String getSValue(String key) {
		return getValue(key) == null ? "" : (String) getValue(key);
	}

	public String getStrValue(String key) {
		return getOValue(key) == null ? "" : (String) getOValue(key);
	}
	
	public Double getDValue(String key) {
		return getStrValue(key).equals("") ? 0.00 : Double.parseDouble(getStrValue(key));
	}

	public int getIntValue(String iKey) {
		int result = 0;
		String str = getStrValue(iKey);
		if (!str.equals(""))
			result = Integer.parseInt(str);
		return result;
	}

	public void init() {
		if (hashMap == null)
			hashMap = new HashMap<String, Object>();
	}

	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}

	public Set<String> keySet() {
		return hashMap.keySet();
	}
	
	public Double getDoubleValue(String iKey) {
		Double result = 0.0;
		String str = getStrValue(iKey);
		if (!str.equals(""))
			result = Double.parseDouble(str);
		return result;
	}

}
