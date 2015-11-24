package markettracker.util;

import orient.champion.business.R;

import markettracker.data.Fields;
import markettracker.data.SObject;
import markettracker.data.UIItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CImageButton extends ImageView
{
	
	private SObject report;
	private UIItem item;
	private TextView count;
	private boolean noImg = true;
	
	public CImageButton(Context context, UIItem item, OnClickListener listener, SObject report, TextView txt)
	{
		super(context);
		this.report = report;
		this.item = item;
		count = txt;
		// this.setMinimumHeight(Tool.dip2px(context, 100));
		 this.setOnClickListener(listener);
		// this.setPadding(Tool.dip2px(context, 2), Tool.dip2px(context, 2),
		// Tool.dip2px(context, 2), Tool.dip2px(context, 2));
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layoutParams.rightMargin = Tool.dip2px(context, 35);
		layoutParams.leftMargin = Tool.dip2px(context, 35);
		this.setLayoutParams(layoutParams);
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.round));
		
		 this.setScaleType(ScaleType.CENTER_INSIDE);
		
		initImg(report);
	}
	
	private void initImg(SObject report)
	{
		Bitmap bitmap = null;
		byte[] bytes;
		Fields data;
		int count = 0;
		for (int i = 0; i < report.getRptAttCount(); i++)
		{
			try
			{
				data = report.getRptAttfield().getFields(i);
				if (data.getPhotoName().equals(this.item.getCaption()))
				{
					bytes = data.getPhoto();
					bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					count++;
				}
				
			}
			catch (Exception ex)
			{
				bitmap = null;
			}
		}
		if (bitmap == null)
		{
			bitmap = Tool.getBimap(getContext(), R.drawable.camera1);
		}
		if (count > 0)
			noImg = false;
		this.count.setText(count + "");
		this.setImageBitmap(bitmap);
	}
	
	public boolean isNoImg()
	{
		return noImg;
	}
	
	public String title()
	{
		return this.item.getCaption();
	}
	public void setImgData(final Bitmap bm, String clientName, String photoType)
	{
		final Fields photo = new Fields();
		
		String date = Tool.getCurrPhotoTime();
		Bitmap bm1 = Tool.generatorContactCountIcon(bm, date, clientName, photoType, getContext());
		
		photo.setShotTime(date);
		photo.setPhoto(Tool.Bitmap2Bytes(bm1));
		photo.setPhotoName(this.item.getCaption());
		photo.put("str10", "10");
		report.addRptAttfield(photo);
		// boolean isHave = false;
		Fields data;
		int count = 0;
		for (int i = 0; i < report.getRptAttCount(); i++)
		{
			try
			{
				data = report.getRptAttfield().getFields(i);
				if (data.getPhotoName().equals(this.item.getCaption()))
				{
					count++;
					// report.setRptAttfield(i, photo);
					// isHave = true;
				}
			}
			catch (Exception ex)
			{
				
			}
		}
		// if (!isHave)
		// {
		// report.addRptAttfield(photo);
		// }
		if (count > 0)
			noImg = false;
		this.count.setText(count + "");
		this.setImageBitmap(bm1);
		
	}
	
}
