package markettracker.data;

public class UpsertResult {
	private Fields result=new Fields();

	public UpsertResult() {
//		init();
	}

//	private void init() {
//		result = new Fields();
//	}

	public Fields getResult() {
		return result;
	}

	public void setResult(Fields result) {
		this.result = result;
	}

	public void setField(String key, String value) {
		this.result.put(key, value);
	}

	public int isSuccess() {
		return result.getIntValue("success");
	}

	public String getErrorCode() {
		return result.getStrValue("errorCode");
	}

	public String getErrorMsg() {
		return result.getStrValue("msg");
	}

	public int getRptId() {
		return result.getIntValue("reportId");
	}

}
