package markettracker.util;

import orient.champion.business.R;

import java.util.HashMap;

import markettracker.data.Fields;
import markettracker.data.SObject;
import markettracker.data.Template;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CImageView extends LinearLayout
{
	
	private CImage image;
	
	private ImageView image2;
	
	private Fields data;
	
	private HashMap<Integer, Fields> report;
	
	private SObject report2;
	
	private Rpt_Table table;
	
	private int index;
	
	private CDeleteButton delete;
	
	public CImageView(Context context, Template temp, OnTouchListener listener, HashMap<Integer, Fields> report, int index, OnClickListener click)
	{
		super(context);
		this.report = report;
		this.index = index;
		this.data = report.get(index - 100) == null ? new Fields() : report.get(index - 100);
		this.setId(index - 100);
		init(context, temp, listener, click);
	}
	
	public CImageView(Context context, Template temp, OnClickListener listener,
			SObject report, int index) {
		super(context);
		this.report2 = report;
		this.index = index;
		this.data = report2.getAttfield(index) == null ? new Fields() : report2
				.getAttfield(index);
		init(context, temp, listener);
	}
	
	public CImageView(Context context, HashMap<Integer, Fields> report, int index, OnTouchListener listener)
	{
		super(context);
		this.report = report;
		this.index = index;
		this.data = report.get(index - 100) == null ? new Fields() : report.get(index - 100);
		this.setId(index - 100);
		this.setOnTouchListener(listener);
		init(context, listener);
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, Tool.getScreenHeight() / 3);
		return layoutParams;
	}
	
	private void init(Context context, Template temp, OnTouchListener listener, OnClickListener click)
	{
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(HORIZONTAL);
		// this.setBackgroundResource(R.drawable.mentou_bg);
		this.setGravity(Gravity.CENTER);
		
		initDeleteButton(context, temp, click);
		initImage(context, temp, listener);
		this.addView(image);
		this.addView(delete);
		// initLine(context, temp);
	}
	
	private void init(Context context, Template temp, OnClickListener listener) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(HORIZONTAL);
		this.setBackgroundResource(R.drawable.mentou_bg);
		this.setGravity(Gravity.CENTER);
		initImage(context, temp, listener);
		initLine(context, temp);
	}
	
	private void initImage(Context context, Template temp,
			OnClickListener listener) {
		LinearLayout.LayoutParams layoutParams;
		if (temp.havePhoto())
			layoutParams = new LinearLayout.LayoutParams(
					Tool.getScreenWidth() / 2, LayoutParams.FILL_PARENT);
		else
			layoutParams = new LinearLayout.LayoutParams(
					Tool.getScreenWidth() * 2 / 3, LayoutParams.FILL_PARENT);
		image2 = new ImageView(context);
		image2.setId(index - 100);
		image2.setLayoutParams(layoutParams);
		image2.setImageBitmap(getBitmap());
		image2.setOnClickListener(listener);
		this.addView(image2);

	}
	
	private void initLine(Context context, Template temp) {
		if (temp.havePhoto()) {
			table = new Rpt_Table(context, temp.getPhotoPanal(), data, null,
					null);
			this.addView(table);
		}
	}
	
	private void init(Context context, OnTouchListener listener)
	{
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(HORIZONTAL);
		// this.setBackgroundResource(R.drawable.mentou_bg);
		this.setGravity(Gravity.CENTER);
		
		initImage(context, listener);
		this.addView(image);
		// this.addView(delete);
		// initLine(context, temp);
	}
	
	private void initImage(Context context, Template temp, OnTouchListener listener)
	{
		LinearLayout.LayoutParams layoutParams;
		// if (temp.getOnlyType() == 1000 || temp.getOnlyType() == 1001)
		// layoutParams = new LinearLayout.LayoutParams(
		// Tool.getScreenWidth() / 3, LayoutParams.FILL_PARENT);
		// else
		layoutParams = new LinearLayout.LayoutParams(Tool.getScreenWidth() * 4 / 5, LayoutParams.FILL_PARENT);
		image = new CImage(context, delete, index);
		image.setId(index - 100);
		image.setLayoutParams(layoutParams);
		image.setScaleType(ScaleType.CENTER_INSIDE);
		image.setImageBitmap(getBitmap());
		image.setOnTouchListener(listener);
		
//		 image.setOnClickListener(listener);
	}
	
	private void initImage(Context context, OnTouchListener listener)
	{
		LinearLayout.LayoutParams layoutParams;
		// if (temp.getOnlyType() == 1000 || temp.getOnlyType() == 1001)
		// layoutParams = new LinearLayout.LayoutParams(
		// Tool.getScreenWidth() / 3, LayoutParams.FILL_PARENT);
		// else
		layoutParams = new LinearLayout.LayoutParams(Tool.getScreenWidth() * 4 / 5, LayoutParams.FILL_PARENT);
		image = new CImage(context, delete, index);
		image.setId(index - 100);
		image.setScaleType(ScaleType.CENTER_INSIDE);
		image.setLayoutParams(layoutParams);
		image.setImageBitmap(getBitmap());
		
//		image.setOnTouchListener(listener);
		// image.setOnClickListener(listener);
	}
	
	private void initDeleteButton(Context context, Template temp, OnClickListener listener)
	{
		delete = new CDeleteButton(context);
		delete.setId(index - 100);
		
		if (data.getPhoto() == null)
			delete.setVisibility(View.GONE);
		
		delete.setOnClickListener(listener);
	}
	
	private Bitmap getBitmap()
	{
		try
		{
			byte[] bytes = data.getPhoto();
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			return bitmap;
		}
		catch (Exception ex)
		{
			return Tool.getBimap(getContext(), R.drawable.camera1);
		}
	}
	
	public void setImageBitmap(Bitmap bm, String date,int type,String productid)
	{
		image.setImageBitmap(bm);
		
		// boolean bHave = report.isHavePhoto(index);
		data.setShotTime(date);
		data.setPhoto(Tool.Bitmap2Bytes(bm));
		
		data.put("Productid", productid);
		
		if (report.containsKey(index - 100))
			report.remove(index - 100);
		report.put(index - 100, data);
		if(type!=1)
			delete.setVisibility(View.VISIBLE);
		// if (bHave)
		// report.setAttfield(index, data);
		// else
		// report.setAttfield(data);
	}
	
	public void setImageBitmap(Bitmap bm, String date,int type)
	{
		image.setImageBitmap(bm);
		
		// boolean bHave = report.isHavePhoto(index);
		data.setShotTime(date);
		data.setPhoto(Tool.Bitmap2Bytes(bm));
		
		
		if (report.containsKey(index - 100))
			report.remove(index - 100);
		report.put(index - 100, data);
		if(type!=1)
			delete.setVisibility(View.VISIBLE);
		// if (bHave)
		// report.setAttfield(index, data);
		// else
		// report.setAttfield(data);
	}
	
	public static int getCallPlanTitleWidth()
	{
		return Tool.getScreenWidth() / 2;
	}
	
	// private Panal getPhotoPanal() {
	// Panal panal = new Panal();
	// panal.setCaption("照片描述");
	// panal.setType(Constants.PanalType.PANEL);
	// panal.setShowCaption(false);
	// UIItem item = new UIItem();
	// item.setCaption("是否符合要求");
	// item.setControlType(ControlType.SINGLECHOICE);
	// item.setTitleWidth(getCallPlanTitleWidth());
	// item.setDicId("105");
	// // item.setShowLable(false);
	// item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	// item.setDataKey("DisplayType");
	// item.setOrientation(LinearLayout.VERTICAL);
	// panal.setItem(item);
	//
	// item = new UIItem();
	// item.setCaption("评定结果");
	// item.setControlType(ControlType.TEXT);
	// item.setTitleWidth(getCallPlanTitleWidth());
	// item.setVerifytype("text");
	// item.setMaxLength(50);
	// // item.setShowLable(false);
	// item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	// item.setDataKey("PhotoName");
	// item.setOrientation(LinearLayout.VERTICAL);
	// panal.setItem(item);
	//
	// return panal;
	// }
	
	// private void initLine(Context context, Template temp) {
	// if (temp.getOnlyType() == 1000 || temp.getOnlyType() == 1001) {
	// table = new Rpt_Table(context, getPhotoPanal(), data, null, null);
	// this.addView(table);
	// }
	// }
	
	public Fields getData()
	{
		return data;
	}
	
	public void setData(Fields data)
	{
		this.data = data;
	}
	
}
