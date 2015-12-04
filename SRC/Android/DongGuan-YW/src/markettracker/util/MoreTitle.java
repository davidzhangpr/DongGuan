package markettracker.util;

import orient.champion.business.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
/**
 * 信息公告 标题 
 * @author Mark
 *
 */
public class MoreTitle extends LinearLayout {

	public MoreTitle(Context context, String title, String count) {
		super(context);
		
		this.setBackgroundResource(R.drawable.tabletop);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 40));
		layoutParams.bottomMargin = Tool.dip2px(context, 1);
		this.setLayoutParams(layoutParams);

		View view = LayoutInflater.from(context).inflate(R.layout.more_title,
				null);
		
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		
		ImageView iv_title_icon = (ImageView) view.findViewById(R.id.iv_title_icon);
		if ("促销信息".equals(title)) {
			iv_title_icon.setBackgroundResource(R.drawable.cxxx_icon);
		}else if("公司规章".equals(title)){
			iv_title_icon.setBackgroundResource(R.drawable.gsgz_icon);
		}else if("培训资料".equals(title)){
			iv_title_icon.setBackgroundResource(R.drawable.pxzl_icon);
		}
		
		TextView tv_title_count = (TextView) view.findViewById(R.id.tv_title_count);
		if(!"".equals(count)){
			tv_title_count.setVisibility(View.VISIBLE);
			tv_title_count.setText(count);
		}
		
		this.addView(view);
	}

}
