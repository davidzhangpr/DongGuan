package markettracker.data;

import java.util.ArrayList;
import java.util.List;

public class DBMainConfig {

	/**
	 * 表名称
	 */
	private String tableName;

//	/**
//	 * 关联类型
//	 */
//	private String type;

	/**
	 * 字段列表
	 */
	private List<DBDetailConfig> fieldList;

	/**
	 * 主键
	 */
	private String key;



//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setField(DBDetailConfig field) {
		if (this.fieldList == null)
			this.fieldList = new ArrayList<DBDetailConfig>();
		this.fieldList.add(field);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<DBDetailConfig> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<DBDetailConfig> fieldList) {
		this.fieldList = fieldList;
	}

}
