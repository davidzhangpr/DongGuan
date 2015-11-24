package markettracker.util;

import markettracker.data.DicData;
import markettracker.data.Fields; //import markettracker.data.Panal;
import markettracker.data.SObject;
import markettracker.data.UIItem;
import orient.champion.business.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.widget.LinearLayout;

public class CShotPhoto extends LinearLayout {

	private CImage image;
	private Fields data;

	public CShotPhoto(Context context, UIItem item, OnClickListener listener,
			Fields data, DicData dic,SObject report) {
		super(context);
		this.data =data;
		init(context, item, listener,dic,report);
	}
	
	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 80));
		return layoutParams;
	}

	private void init(Context context, UIItem item, OnClickListener listener,DicData dic,SObject report) {
		this.setLayoutParams(getCurLayoutParams());
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER);
		this.setPadding(5, 0, 5, 0);
		this.addView(new CSelectBox(context, item, this.data));
		initImage(context, item, listener,dic,report);
		// initLine(context, temp);
	}
	

	private void initImage(Context context, UIItem item,
			OnClickListener listener,DicData dic,SObject report) {
		image = new CImage(context,dic,listener);
//		image.setId(Integer.valueOf(dic.getValue()));
		image.setImageBitmap(getBitmap(report,dic));
//		image.set
		this.addView(image);

	}
	
	private Fields getPhoto(SObject report,DicData dic)
	{
		Fields data;
		for (int i=0;i<report.getRptAttCount();i++)
		{
			data=report.getRptAttfield(i);
			if(data.getStrValue("DisplayType").equals(
					dic.getDictType())
					&& data.getStrValue("displayobject").equals(
							dic.getValue()))
				return data;
		}
		return null; 
	}

	private Bitmap getBitmap(SObject report,DicData dic) {
		try {
			data=getPhoto(report, dic);
			byte[] bytes = data.getPhoto();
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			return bitmap;
		} catch (Exception ex) {
			return Tool.getBimap(getContext(), R.drawable.camera1);
		}
	}

	public void setImageBitmap(Bitmap bm) {
		image.setImageBitmap(bm);

//		boolean bHave = report.isHavePhoto(index);
//		data.setShotTime(Tool.getCurrPhotoTime());
//		data.setPhoto(Tool.Bitmap2Bytes(bm));
//
//		if (bHave)
//			report.setAttfield(index, data);
//		else
//			report.setAttfield(data);
	}

	public static int getCallPlanTitleWidth() {
		return Tool.getScreenWidth() / 2;
	}

	public Fields getData() {
		return data;
	}

	public void setData(Fields data) {
		this.data = data;
	}

}
