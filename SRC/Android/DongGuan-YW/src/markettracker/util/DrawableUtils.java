package markettracker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class DrawableUtils {

	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	public static Drawable getDrawable(Context context, int iMsgId) {
		return context.getResources().getDrawable(iMsgId);
	}
	
	public static Drawable getUriImage(Context context, String url)
	{
		Drawable drawable = null;
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// Bitmap bitmap =null;
			Bitmap bitmap = BitmapFactory.decodeFile(url, options); // 此时返回 bm
			options.inJustDecodeBounds = false;
			int heightRatio = (int) Math.ceil(options.outHeight / Tool.dip2px(context, 40));
			int widthRatio = (int) Math.ceil(options.outWidth / Tool.dip2px(context, 40));
			if (heightRatio > 1 && widthRatio > 1)
			{
				if (heightRatio > widthRatio)
				{
					options.inSampleSize = heightRatio;
				}
				else
				{
					options.inSampleSize = widthRatio;
				}
			}
			else
				options.inSampleSize = 1;
			bitmap = BitmapFactory.decodeFile(url, options);
			drawable = bitmap2Drawable(context, bitmap);
		}
		catch (Exception ex)
		{
		}
		return drawable;
	}
	
	public static Drawable bitmap2Drawable(Context context, Bitmap bm)
	{
		return new BitmapDrawable(context.getResources(), bm);
	}
}
