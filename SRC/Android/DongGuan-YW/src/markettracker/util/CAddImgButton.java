package markettracker.util;


import orient.champion.business.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.RelativeLayout;

public class CAddImgButton extends Button
{
	
	private CImageListButton imgButton;
	public CAddImgButton(Context context, OnClickListener listener,CImageListButton cImageListButton)
	{
		super(context);
		imgButton=cImageListButton;
		RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams( Tool.dip2px(context, 30),Tool.dip2px(context, 30));
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		this.setLayoutParams(layoutParams);
		this.setOnClickListener(listener);
		this.setBackgroundDrawable(Tool.getDrawable(context, R.drawable.add));
	}
	
	public void setImgData(final Bitmap bm, String clientName, String photoType)
	{
		imgButton.setImgData(bm, clientName, photoType);
		
	}
	
}
