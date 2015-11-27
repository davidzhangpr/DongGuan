package markettracker.util;

import java.util.List;

import markettracker.util.Constants.ControlType;
import orient.champion.business.R;
import markettracker.data.Fields;
import markettracker.data.SObject;
import markettracker.data.Template;
import markettracker.data.UIItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

@SuppressLint("ResourceAsColor")
public class CustomLineLayoutContent extends LinearLayout {

	private CEditText cEditText;

	private CSpinner spinner;
	private CSelectBox selectBox;
	private CMutiSpinner cMutiSpinner;

	private CContent content;

	private CDatePicker cdatePicker;

	private UIItem mUIItem;

	private CSpinnerList cSpinnerList;

	private Fields fields;
	private CCellTakePhoto cCellTakePhoto;
	
	private CShotPhotoList shotPhoto;
	
	private Fields mData;

	public CustomLineLayoutContent(Context context, UIItem item,
			Activity activity, Fields data, Handler handler, SObject object,OnClickListener listener) {
		super(context);
		setUIItem(item);
		fields = data;
		init(context, item, activity, data, handler, object,listener);
	}

	public CustomLineLayoutContent(Context context, Template template, UIItem item,
			Activity activity, Fields data, Handler handler, SObject report,OnClickListener listener) {
		super(context);
		setUIItem(item);
		fields = data;
		init(context, template, item, activity, report.getDetailfields().getList(), handler, report,listener);
	}
	
	public CustomLineLayoutContent(Context context, UIItem item, Activity activity, Fields data, OnClickListener listener)
	{
		super(context);
		setUIItem(item);
		init(context, item, data, listener, null, null);
	}

	private void setUIItem(UIItem item) {
		this.mUIItem = item;
	}

	public UIItem getCurUIItem() {
		return this.mUIItem;
	}

	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.rightMargin = Tool.dip2px(getContext(), 5);
		layoutParams.topMargin = Tool.dip2px(getContext(), 5);
		layoutParams.bottomMargin = Tool.dip2px(getContext(), 5);
		layoutParams.leftMargin = Tool.dip2px(getContext(), 5);
		return layoutParams;
	}

	private int getCurGravity() {
		return Gravity.CENTER_VERTICAL;
	}

	private void init(Context context, UIItem item, Activity activity,
			Fields data, Handler handler, SObject object,OnClickListener listener) {
		this.setGravity(getCurGravity());
		this.setLayoutParams(getCurLayoutParams());
		if (item.isMustInput())
			this.addView(new CustomImageView(context, null));

		// 添加具体控件
		if (item.getControlType() == ControlType.TEXT) {
			cEditText = new CEditText(context, item);
			cEditText.setText(data.getStrValue(item.getDataKey()));
			
			if("地堆一平米，地堆两平米".equals(data.getStrValue(item.getDataKey()))){
				cEditText.setTextColor(R.color.background);
			}
			
			cEditText.addTextChangedListener(new EditTextWatcher(item,
					cEditText, data, context));
			this.addView(cEditText);
		} else if (item.getControlType() == ControlType.SINGLECHOICE) {
			spinner = new CSpinner(context, item, data, handler);
			this.addView(spinner);
		} else if (item.getControlType() == ControlType.SINGLECHOICELIST) {
			cSpinnerList = new CSpinnerList(context, item, data, handler);
			this.addView(cSpinnerList);
		} else if (item.getControlType() == ControlType.TAKEPHOTO) {
			cCellTakePhoto = new CCellTakePhoto(context, object, item, listener);
			this.addView(cCellTakePhoto);
		}

		else if (item.getControlType() == ControlType.RADIOBUTTON) {
			selectBox = new CSelectBox(context, item, data);
			this.addView(selectBox);
		}

		else if (item.getControlType() == ControlType.MULTICHOICE) {
			cMutiSpinner = new CMutiSpinner(context, item, data, "");
			this.addView(cMutiSpinner);
		} else if (item.getControlType() == ControlType.NONE) {
			content = new CContent(context, item);
			content.setText(data.getStrValue(item.getDataKey()));
			this.addView(content);
		} else if (item.getControlType() == ControlType.DATATIME
				|| item.getControlType() == ControlType.TIME
				|| item.getControlType() == ControlType.DATE) {
			cdatePicker = new CDatePicker(context, item, data, handler);
			this.addView(cdatePicker);
		}

	}

	private void init(Context context, Template template, UIItem item, Activity activity,
			List<Fields> fieldsGroup, Handler handler, SObject object,OnClickListener listener) {
		this.setGravity(getCurGravity());
		this.setLayoutParams(getCurLayoutParams());
		if (item.isMustInput()){
			this.addView(new CustomImageView(context, null));
		}
		
		Fields data = new Fields();
		for(int i=0; i<fieldsGroup.size(); i++){
			//根据serverid来为item匹配相对应的数据
			if(((String)fieldsGroup.get(i).getHashMap().get("serverid")).equals(item.getServerid())){
				data = fieldsGroup.get(i);
			}
		}
		
		// 添加具体控件
		if (item.getControlType() == ControlType.TEXT) {
			cEditText = new CEditText(context, item);
			cEditText.setText(data.getStrValue(item.getDataKey()));
			cEditText.addTextChangedListener(new EditTextWatcher(item,
					cEditText, data, context));
			this.addView(cEditText);
		} else if (item.getControlType() == ControlType.SINGLECHOICE) {
			spinner = new CSpinner(context, item, data, handler);
			this.addView(spinner);
		} else if (item.getControlType() == ControlType.SINGLECHOICELIST) {
			cSpinnerList = new CSpinnerList(context, item, data, handler);
			this.addView(cSpinnerList);
		} else if (item.getControlType() == ControlType.TAKEPHOTO) {
			cCellTakePhoto = new CCellTakePhoto(context, object, item, listener);
			this.addView(cCellTakePhoto);
		}
		
		else if (item.getControlType() == ControlType.RADIOBUTTON) {
			selectBox = new CSelectBox(context, item, data);
			this.addView(selectBox);
		}
		
		else if (item.getControlType() == ControlType.MULTICHOICE) {
			cMutiSpinner = new CMutiSpinner(context, item, data, "");
			this.addView(cMutiSpinner);
		} else if (item.getControlType() == ControlType.NONE) {
			content = new CContent(context, item);
			content.setText(data.getStrValue(item.getDataKey()));
			this.addView(content);
		} else if (item.getControlType() == ControlType.DATATIME
				|| item.getControlType() == ControlType.TIME
				|| item.getControlType() == ControlType.DATE) {
			cdatePicker = new CDatePicker(context, item, data, handler);
			this.addView(cdatePicker);
		}
		
	}
	
	private void init(Context context, UIItem item, Fields data, OnClickListener listener, SObject report, Handler handler)
	{
		
		mData = data;
		this.setGravity(getCurGravity());
		this.setLayoutParams(getCurLayoutParams());
		if (item.isMustInput())
			this.addView(new CustomImageView(context, null));
		
		// 添加具体控件
		if (item.getControlType() == ControlType.TEXT)
		{
			cEditText = new CEditText(context, item);
			cEditText.setText(data.getStrValue(item.getDataKey()));
			cEditText.addTextChangedListener(new EditTextWatcher(item, cEditText, data, context, handler));
			this.addView(cEditText);
		}
		else if (item.getControlType() == ControlType.SINGLECHOICE)
		{
			spinner = new CSpinner(context, item, data);
			spinner.addTextChangedListener(new CSpinnerWatcher(item, spinner, data, context, handler));
			this.addView(spinner);
		}
		
		else if (item.getControlType() == ControlType.RADIOBUTTON)
		{
			selectBox = new CSelectBox(context, item, data);
			this.addView(selectBox);
		}
		
		else if (item.getControlType() == ControlType.MULTICHOICE)
		{
			cMutiSpinner = new CMutiSpinner(context, item, data, "");
			this.addView(cMutiSpinner);
		}
		else if (item.getControlType() == ControlType.NONE)
		{
			content = new CContent(context, item);
			content.setText(data.getStrValue(item.getDataKey()));
			this.addView(content);
		}
		else if (item.getControlType() == ControlType.DATATIME||item.getControlType() == ControlType.DATE)
		{
			cdatePicker = new CDatePicker(context, item, data);
			this.addView(cdatePicker);
		}
		else if (item.getControlType() == ControlType.SHOTPHOTO)
		{
			shotPhoto = new CShotPhotoList(context, item, listener, data, report);
			this.addView(shotPhoto);
		}
		
	}

	public void resetData() {
		fields.put(mUIItem.getDataKey(), "");
	}

	public void refresh() {
		cdatePicker.refresh();
	}
}
