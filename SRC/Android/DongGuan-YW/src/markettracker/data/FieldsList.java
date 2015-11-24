package markettracker.data;

import java.util.*;

public class FieldsList {
	private List<Fields> list;

	public List<Fields> getList() {
		return list;
	}

	
	public void addList(List<Fields> list) {
		if (list != null) {
			for (Fields data : list) {
				setFields(data);
			}
		}
	}
	
	public void setList(List<Fields> list) {
		this.list = list;
	}

	public void setFields(Fields fields) {
		if (list == null)
			list = new ArrayList<Fields>();
		this.list.add(fields);
	}

	public void setFields(int index, Fields fields) {
		if (list == null)
			list = new ArrayList<Fields>();
		this.list.set(index, fields);
	}

	public void removeFields(int index) {
		if (index >= 0 && list != null && index < list.size())
			this.list.remove(index);
	}

	public Fields getFields(int index) {
		if (index >= 0 && list != null && index < list.size())
			return list.get(index);
		return null;
	}

	public int size() {
		if (list != null)
			return list.size();
		else
			return 0;
	}
	
	public void removeAllFields() {
		list = new ArrayList<Fields>();
	}

	public Iterator<Fields> iterator() {
		return list.iterator();
	}

}
