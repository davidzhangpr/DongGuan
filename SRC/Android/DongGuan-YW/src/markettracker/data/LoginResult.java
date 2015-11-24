package markettracker.data;

public class LoginResult {
	private String userId;
	private String errorCode;
	private String errorMsg;
	private String serverTime;

	private Fields data;

	private FieldsList msgTypeList = new FieldsList();

	private int isSuccess;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public FieldsList getMsgTypeList() {
		return msgTypeList;
	}

	public void setMsgTypeList(FieldsList msgTypeList) {
		this.msgTypeList = msgTypeList;
	}

	// public void setMsgType(Fields msgType) {
	// this.msgTypeList.setFields(msgType);
	// }

	public int isSuccess() {
		return isSuccess;
	}

	public void setSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Fields getData() {
		return data;
	}

	public void setData(Fields data) {
		this.data = data;
	}

	public void setField(String key, String value) {
		if (this.data == null)
			this.data = new Fields();
		this.data.put(key, value);
	}

	public String getField(String key) {

		return this.data.getStrValue(key);
	}
}
