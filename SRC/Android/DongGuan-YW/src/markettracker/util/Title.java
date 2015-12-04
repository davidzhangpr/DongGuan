package markettracker.util;

import markettracker.data.TemplateGroup;
import orient.champion.business.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Title extends TextView {
	private TemplateGroup group;

	public Title(Context context, TemplateGroup group) {
		super(context);
		this.group = group;
		this.setTextColor(getResources().getColor(R.color.black));

		this.setText(group.getName());
		this.setTextSize(15);
		this.setPadding(Tool.dip2px(getContext(), 10), 1, Tool.dip2px(getContext(), 15), 1);
		this.setGravity(Gravity.CENTER_VERTICAL);

		this.setCompoundDrawablePadding(Tool.dip2px(context, 5));

		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tabletop);

		if (group.isShowView()) {
			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(group.getImgId()), null,
					getDrawable(R.drawable.dropup), null);
		} else {
			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(group.getImgId()), null,
					getDrawable(R.drawable.dropdown), null);
		}
	}

	public Title(Context context, String title) {
		super(context);
		this.setTextColor(getResources().getColor(R.color.black));
		this.setText(title);
		this.setTextSize(15);
		this.setPadding(Tool.dip2px(getContext(), 5), 1, Tool.dip2px(getContext(), 15), 1);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tabletop);
		// this.setCompoundDrawablesWithIntrinsicBounds(null, null,
		// getDrawable(R.drawable.dropdown), null);
		
		Drawable icon;
		if ("促销信息".equals(title.substring(0, 4))) {
			icon = getDrawable(R.drawable.cxxx_icon);
			this.setCompoundDrawablePadding(20);
			this.setCompoundDrawablesWithIntrinsicBounds(zoomDrawable(icon, Tool.dip2px(getContext(), 100), Tool.dip2px(getContext(), 100)), null, null, null);
		}else if("公司规章".equals(title.substring(0, 4))){
			icon = getDrawable(R.drawable.gsgz_icon);
			this.setCompoundDrawablePadding(20);
			this.setCompoundDrawablesWithIntrinsicBounds(zoomDrawable(icon, Tool.dip2px(getContext(), 100), Tool.dip2px(getContext(), 100)), null, null, null);
		}else if("培训资料".equals(title.substring(0, 4))){
			icon = getDrawable(R.drawable.pxzl_icon);
			this.setCompoundDrawablePadding(20);
			this.setCompoundDrawablesWithIntrinsicBounds(zoomDrawable(icon, Tool.dip2px(getContext(), 100), Tool.dip2px(getContext(), 100)), null, null, null);
		}
	}

	public Title(Context context, String title, boolean isCount) {
		super(context);
		this.setTextColor(getResources().getColor(R.color.black));
		this.setTextSize(15);
		this.setPadding(Tool.dip2px(getContext(), 5), 1, Tool.dip2px(getContext(), 15), 1);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tabletop);

		// 设置字体样式
		Spannable s = new SpannableString(title);
		s.setSpan(new ForegroundColorSpan(Color.RED), title.lastIndexOf("（"), title.lastIndexOf("）") + 1,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		Drawable icon;
		if ("促销信息".equals(title.substring(0, 4))) {
			icon = getDrawable(R.drawable.cxxx_icon);
			this.setCompoundDrawablePadding(20);
			this.setCompoundDrawablesWithIntrinsicBounds(zoomDrawable(icon, Tool.dip2px(getContext(), 100), Tool.dip2px(getContext(), 100)), null, null, null);
		}else if("公司规章".equals(title.substring(0, 4))){
			icon = getDrawable(R.drawable.gsgz_icon);
			this.setCompoundDrawablePadding(20);
			this.setCompoundDrawablesWithIntrinsicBounds(zoomDrawable(icon, Tool.dip2px(getContext(), 100), Tool.dip2px(getContext(), 100)), null, null, null);
		}else if("培训资料".equals(title.substring(0, 4))){
			icon = getDrawable(R.drawable.pxzl_icon);
			this.setCompoundDrawablePadding(20);
			this.setCompoundDrawablesWithIntrinsicBounds(zoomDrawable(icon, Tool.dip2px(getContext(), 100), Tool.dip2px(getContext(), 100)), null, null, null);
		}

		this.setText(s);
	}

	public Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成 bitmap
	{
		int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
																	// bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把 drawable 内容画到画布中
		return bitmap;
	}

	public Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
		Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
		float scaleWidth = ((float) w / width); // 计算缩放比例
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true); // 建立新的
																						// bitmap
																						// ，其内容是对原
																						// bitmap
																						// 的缩放后的图
		return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回
	}

	private LayoutParams getCurLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, getH(40));
		layoutParams.topMargin = getH(1);
		return layoutParams;
	}

	public void setCompoundDrawables(int type) {
		this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(group.getImgId()), null, getDrawable(type), null);
	}

	private int getH(int dip) {
		return Tool.dip2px(getContext(), dip);
	}

	private Drawable getDrawable(int type) {
		if (type == -1)
			return null;
		return getResources().getDrawable(type);
	}

}
