package markettracker.data;

import java.util.ArrayList;

import markettracker.util.Constants.ControlType;

import java.util.List;

import markettracker.util.Tool;
import android.R.bool;
import android.graphics.Paint.Align;
import android.view.Gravity;
import android.widget.LinearLayout;

public class UIItem {

	private int id;
	private String dataKey = "";
	private String caption = "";
	private boolean showValue = false;
	private String dicId = "";

	private String containerId = "";
	private int validate = -1;

	private ControlType controlType;

	private int controlTypeId = -1;
	private int index = -1;
	private boolean showLable = true;
	private int required = -1;
	private int maxLength = -1;
	private int minValue = -1;
	private int maxValue = -1;

	private String verifytype = "";

	private int position = 0;

	private Align align = Align.CENTER;
	private int gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;

	private Contrast contrast = new Contrast();

	private long minDate = -1;
	private long maxDate = -1;

	private int width = -1;

	private int titleWidth = Tool.getScreenWidth() / 4;

	private int orientation = LinearLayout.VERTICAL;

	private int captionWidth = -1;

	private boolean mustInput = false;

	private int spinnerType = -1;
	
	private int itemType = 0;

	private List<UIItem> reItemlist = new ArrayList<UIItem>();

	private boolean refreshItem;
	
	private String serverid;

	public String getServerid() {
		return serverid;
	}

	public void setServerid(String serverid) {
		this.serverid = serverid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public boolean isShowValue() {
		return showValue;
	}

	public void setShowValue(boolean showValue) {
		this.showValue = showValue;
	}

	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}

	public int getGravity() {
		return gravity;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public int getTitleWidth() {
		return titleWidth;
	}

	public void setTitleWidth(int titleWidth) {
		this.titleWidth = titleWidth;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getValidate() {
		return validate;
	}

	public void setValidate(int validate) {
		this.validate = validate;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getRequired() {
		return required;
	}

	public void setRequired(int required) {
		this.required = required;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Contrast getContrast() {
		return contrast;
	}

	public void setContrast(Contrast contrast) {
		this.contrast = contrast;
	}

	public long getMinDate() {
		return minDate;
	}

	public void setMinDate(long minDate) {
		this.minDate = minDate;
	}

	public long getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(long maxDate) {
		this.maxDate = maxDate;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getControlTypeId() {
		return controlTypeId;
	}

	public void setControlTypeId(int controlTypeId) {
		this.controlTypeId = controlTypeId;
	}

	public int getCaptionWidth() {
		return captionWidth;
	}

	public void setCaptionWidth(int captionWidth) {
		this.captionWidth = captionWidth;
	}

	public int getSpinnerType() {
		return spinnerType;
	}

	public void setSpinnerType(int spinnerType) {
		this.spinnerType = spinnerType;
	}

	public boolean isShowLable() {
		return showLable;
	}

	public void setShowLable(boolean showLable) {
		this.showLable = showLable;
	}

	public List<UIItem> getReItemlist() {
		return reItemlist;
	}

	public void setReItemlist(List<UIItem> reItemlist) {
		this.reItemlist = reItemlist;
	}

	public boolean isMustInput() {
		return mustInput;
	}

	public void setMustInput(boolean mustInput) {
		this.mustInput = mustInput;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getVerifytype() {
		return verifytype;
	}

	public void setVerifytype(String verifytype) {
		this.verifytype = verifytype;
	}

	public ControlType getControlType() {
		return controlType;
	}

	public void setControlType(ControlType controlType) {
		this.controlType = controlType;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	
			/**
			* 返回
			refreshItem 的值
			*@return refreshItem
			*/  
	
		
	public boolean isRefreshItem()
	{
		return refreshItem;
	}

	
			/**
			* 设置
			refreshItem 的值
			* @param refreshItem
			*/
		
	public void setRefreshItem(boolean refreshItem)
	{
		this.refreshItem = refreshItem;
	}

}
