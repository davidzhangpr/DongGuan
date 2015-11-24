package markettracker.data;

public class DBDetailConfig {

	private String fieldName;
	private String type;

	private boolean isNull = true;
	private boolean isKey = false;
	
	private boolean isQuery = true;

	private boolean isDownLoad = true;

	private boolean isUpload = true;

	private boolean isReplace=false;
	
	private boolean isUnique = false;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

	public boolean isDownLoad() {
		return isDownLoad;
	}

	public void setDownLoad(boolean isDownLoad) {
		this.isDownLoad = isDownLoad;
	}

	public boolean isUpload() {
		return isUpload;
	}

	public void setUpload(boolean isUpload) {
		this.isUpload = isUpload;
	}

//	public String getFieldIndex() {
//		return fieldIndex;
//	}
//
//	public void setFieldIndex(String fieldIndex) {
//		this.fieldIndex = fieldIndex;
//	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public boolean isQuery() {
		return isQuery;
	}

	public void setQuery(boolean isQuery) {
		this.isQuery = isQuery;
	}

	public boolean isReplace() {
		return isReplace;
	}

	public void setReplace(boolean isReplace) {
		this.isReplace = isReplace;
	}

}
