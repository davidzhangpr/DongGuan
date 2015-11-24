package markettracker.data;

import java.util.Set;

import markettracker.util.Constants;

public class LoginConfig {

	private Fields fields;

	public LoginConfig() {
		init();
	}

	public void init() {
		if (fields == null)
			fields = new Fields();
	}

	public String getUsername() {
		return fields.getSValue(Constants.LoginConfig.USER_NAME);
	}

	public void setUsername(String name) {
		fields.Set(Constants.LoginConfig.USER_NAME, name);
	}

	public String getPassword() {
		return fields.getSValue(Constants.LoginConfig.USER_PWD);
	}

	public void setPassword(String pwd) {
		fields.Set(Constants.LoginConfig.USER_PWD, pwd);
	}

	public String get(String key) {
		return fields.getSValue(key);
	}

	public String getApiVersion() {
		return fields.getSValue(Constants.LoginConfig.APP_VERSION);
	}

	public void setApiVersion(String version) {
		fields.Set(Constants.LoginConfig.APP_VERSION, version);
	}

	public String getImei() {
		return fields.getSValue(Constants.LoginConfig.IMEI);
	}

	public void setImei(String imei) {
		fields.Set(Constants.LoginConfig.IMEI, imei);
	}

	public String getDeviceType() {
		return fields.getSValue(Constants.LoginConfig.DEVICE_TYPE);
	}

	public void setDeviceType(String deviceType) {
		fields.Set(Constants.LoginConfig.DEVICE_TYPE, deviceType);
	}

	public Fields getFields() {
		return fields;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}
	
	public Set<String> keySet() {
		return fields.keySet();
	}

}
