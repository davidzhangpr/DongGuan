package markettracker.comm;

public class ExceptionResponse implements Response {
	private String result="";


	public ExceptionResponse() {
	}

	public ExceptionResponse(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Response getSoapResponse() {
		return this;
	}
}
