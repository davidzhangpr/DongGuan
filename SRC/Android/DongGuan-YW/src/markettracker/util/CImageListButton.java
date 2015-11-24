package markettracker.util;

import orient.champion.business.R;

import markettracker.data.SObject;
import markettracker.data.UIItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CImageListButton extends RelativeLayout
{
	
	private SObject report;
	private UIItem item;
	private CImageButton imgButton;
	private CAddImgButton add;
	private TextView count;
	
	public CImageListButton(Context context, UIItem item, OnClickListener listener, SObject report)
	{
		super(context);
		this.report = report;
		this.item = item;
		 this.setMinimumHeight(Tool.dip2px(context, 100));
//		this.setOnClickListener(listener);
		this.setPadding(Tool.dip2px(context, 2), Tool.dip2px(context, 2), Tool.dip2px(context, 2), Tool.dip2px(context, 2));
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, Tool.dip2px(context, 100));
////		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 100);
//
		this.setLayoutParams(param);
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.round));
		
//		this.setOrientation(LinearLayout.HORIZONTAL);
		
		this.setGravity(Gravity.CENTER);
//		this.
		
//		this.setScaleType(ScaleType.CENTER_INSIDE);
		addCount(context,report);
		addImg(context,item,listener,report);
		addButton(context,item,listener,report);
		
//		initImg(report);
	}
	
	public void setImgData(final Bitmap bm, String clientName, String photoType)
	{
		imgButton.setImgData(bm, clientName, photoType);
		
	}
	
	private void addImg(Context context, UIItem item, OnClickListener listener, SObject report)
	{
		imgButton=new CImageButton(context, item, listener, report,count);
		this.addView(imgButton);
		this.addView(count);
	}
	
	private void addButton(Context context, UIItem item, OnClickListener listener, SObject report)
	{
		add=new CAddImgButton(context,listener,this);

		this.addView(add);
	}
	
	private void addCount(Context context,SObject report)
	{
		
		count=new TextView(context);
		
		RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams( Tool.dip2px(context, 20),Tool.dip2px(context, 20));

		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		count.setLayoutParams(layoutParams);

		count.setBackgroundDrawable(Tool.getDrawable(context, R.drawable.round_background));
		count.setTextSize(10);
		count.setText("5");
		count.setTextColor(getResources().getColor(R.color.black));
		count.setGravity(Gravity.CENTER);
		
	}
	
}
