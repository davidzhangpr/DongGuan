package markettracker.util;

import java.util.ArrayList;
import java.util.List;

import markettracker.data.DicData;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import orient.champion.business.R;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ChoiceGroupAdapter extends ArrayAdapter<String> {

	private List<DicData> list;
	private UIItem item;

	public ChoiceGroupAdapter(Context context, UIItem item) {
		super(context, R.layout.spinner_dropdown_item, new ArrayList<String>());

		this.setDropDownViewResource(R.layout.spinner_dropdown_item);
		this.item = item;
		addItem(context);
	}

	private void addItem(Context context) {
		try {
			for (DicData data : getDictData(context)) {
				if (data != null)
					this.add(data.getItemname());
			}
		} catch (Exception ex) {

		}
	}

	private DicData getDicData(int index) {
		return list.get(index);
	}

	public int getPosition(String value) {
		DicData dic = null;
		for (int i = 0; i < list.size(); i++) {
			dic = list.get(i);
			if (dic.getValue().equals(value)) {
				return i;
			}
		}
		return -1;
	}

	public int getDicData(String value) {
		DicData dic = null;
		for (int i = 0; i < list.size(); i++) {
			dic = list.get(i);
			if (dic.getValue().equals(value)) {
				return i;
			}
		}
		return -1;
	}

	public String getSaveValue(int index) {
		if (list == null)
			return "";
		return getDicData(index).getValue();
	}

	public String getValue(int index) {
		if (list == null)
			return "";
		return getDicData(index).getValue();
	}

	public String getName(int index) {
		if (list == null)
			return "";
		return getDicData(index).getItemname();
	}

	public String getName(String value) {
		if (value == null)
			return "";
		int index = getDicData(value);
		if (index == -1)
			return "";
		return getDicData(getDicData(value)).getItemname();
	}

	private List<DicData> getDictData(Context context) {
		if (list == null) {
			if (this.item.getItemType() == 1)
				list = Sqlite.getAnswerList(context, item.getDicId());
			else
				list = Sqlite.getDictDataList(context, item.getDicId(), "");
		}
		return list;
	}

}
