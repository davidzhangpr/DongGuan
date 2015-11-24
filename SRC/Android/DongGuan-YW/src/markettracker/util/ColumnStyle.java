package markettracker.util;

import java.util.List;

import markettracker.data.DicData;
import markettracker.data.UIItem;
import markettracker.util.Constants.ControlType;
import android.graphics.Paint;

public class ColumnStyle {

	/** 设置居中、居左�?居右, 默认居中 */
	private Paint.Align mAlign = Paint.Align.CENTER;

	/** 设置列的字体大小 */
	private float mTextSize = 18;

	/** 设置列的输入方式 */
	private ControlType mInputType;

	/**
	 * 当输入方式是单�?、多选时，必须设置，表示选项列表
	 */
	private List<DicData> mList;
	private String[] mChoiceArray;

	public Paint.Align getAlign() {
		return mAlign;
	}

	public void setAlign(Paint.Align align) {
		mAlign = align;
	}

	public float getTextSize() {
		return mTextSize;
	}

	public void setTextSize(float size) {
		mTextSize = size;
	}

	public ControlType getControlType() {
		
		return mInputType;
	}

	/**
	 * 设置输入方式
	 * 
	 * @param type
	 * @param choiceArr
	 *            当输入方式是单�?、多选时必须设置，其他情况不必设�?
	 */
	public void setControlType(ControlType type, List<DicData> list) {
		mInputType = type;
		mList = list;
	}

	public void setControlType(ControlType type) {
		mInputType = type;
	}

	public void setControlType(UIItem item) {

		if (item.getControlType().equals(ControlType.TEXT)) {
			if (item.getVerifytype().equalsIgnoreCase("number"))
				mInputType = ControlType.INTEGER;
			else if (item.getVerifytype().equalsIgnoreCase("amount"))
				mInputType = ControlType.DECIMAL;
			else if (item.getCaption().equals("产品名称"))
				mInputType = ControlType.NONE;
			else
				mInputType = ControlType.TEXT;
		} else
			mInputType = item.getControlType();
	}

	public List<DicData> getChoiceList() {
		return mList;
	}

	public String[] getChoiceArray() {
		if (mList != null && mList.size() > 0) {
			mChoiceArray = new String[mList.size()];
			for (int i = 0; i < mList.size(); i++)
				mChoiceArray[i] = mList.get(i).getValue();
			return mChoiceArray;
		} else
			return null;
	}
}