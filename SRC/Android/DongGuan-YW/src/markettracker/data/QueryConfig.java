package markettracker.data;

import java.util.Set;

import markettracker.util.Constants;

public class QueryConfig {

	private Fields fields;

	public QueryConfig() {
		init();
	}

	public void init() {
		if (fields == null)
			fields = new Fields();
	}

	public String getType() {
		return fields.getSValue(Constants.QueryConfig.TYPE);
	}

	public void setType(String type) {
		fields.Set(Constants.QueryConfig.TYPE, type);
	}

	public String getUserId() {
		return fields.getSValue(Constants.QueryConfig.USER_ID);
	}

	public void setUserId(String userid) {
		fields.Set(Constants.QueryConfig.USER_ID, userid);
	}

	public void set(String key,String value) {
		fields.Set(key,value);
	}
	
	public String get(String key) {
		return fields.getSValue(key);
	}

	public String getIsAll() {
		return fields.getSValue(Constants.QueryConfig.ISALL);
	}

	public void setIsAll(String isAll) {
		fields.Set(Constants.QueryConfig.ISALL, isAll);
	}

	public String getLastTime() {
		return fields.getSValue(Constants.QueryConfig.LASTTIME);
	}

	public void setLastTime(String lastTime) {
		fields.Set(Constants.QueryConfig.LASTTIME, lastTime);
	}

	public String getStartRow() {
		return fields.getSValue(Constants.QueryConfig.STARTROW);
	}

	public void setStartRow(String startRow) {
		fields.Set(Constants.QueryConfig.STARTROW, startRow);
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
