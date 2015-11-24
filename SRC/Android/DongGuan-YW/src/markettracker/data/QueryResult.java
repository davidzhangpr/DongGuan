package markettracker.data;

import java.util.ArrayList;

public class QueryResult {
	private String type;
	private String nextStartRow;
	private String isAll;
	private int isDone;
	private ArrayList<SObject> clientTable;
	private int isSuccess;
	private String errorCode;
	private String errorMsg;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNextStartRow() {
		return nextStartRow;
	}

	public void setNextStartRow(String nextStartRow) {
		this.nextStartRow = nextStartRow;
	}



	public int isDone() {
		return isDone;
	}

	public void setDone(int isDone) {
		this.isDone = isDone;
	}

	public int isSuccess() {
		return isSuccess;
	}

	public void setSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
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

	public ArrayList<SObject> getClientTable() {
		return clientTable;
	}

	public void setClientTableList(ArrayList<SObject> clientTable) {
		this.clientTable = clientTable;
	}
	
	public void setClientTable(SObject clientTable) {
		if(this.clientTable==null)
			this.clientTable=new ArrayList<SObject>();
		this.clientTable.add(clientTable);
	}

	public String isAll() {
		return isAll;
	}
	public void setAll(String isAll) {
		this.isAll = isAll;
	}

}
