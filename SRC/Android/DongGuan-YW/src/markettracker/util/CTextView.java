package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.Template;
import orient.champion.business.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ResourceAsColor")
public class CTextView extends TextView
{
	
	private Template template;
	public Fields ziliaoField;
	
	public CTextView(Context context, Template temp)
	{
		super(context);
		template = temp;
		this.setTextColor(Tool.getTextColor(context));
		// this.setText("  " + temp.getName());
		this.setTextSize(16.0f);
		if (temp.getDescription() != null)
		{
			this.setText(temp.getName() + "\r\n    " + temp.getDescription());
			highlight(template.getName().length(), this.getText().toString().length());
		}
		else
		{
			if(temp.isSubmit())
			{
				this.setText(temp.getName()+"(已上传)");
				highlight(template.getName().length(), this.getText().toString().length());
			}
			
			else {
				this.setText(temp.getName());
			}
			
		}
		
		this.setCompoundDrawablePadding(getH(5));
		this.setTextSize(16.0f);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setBackgroundResource(R.drawable.tablemid);
		
		this.setPadding(getH(20), 0, getH(15), 0);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tablemid);
		if (template.isComplete())
			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(1), null, getDrawable(2), null);
		else
			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(0), null, getDrawable(2), null);
		
	}
	
	@SuppressLint("ResourceAsColor")
	public CTextView(Context context, Fields field) {
		super(context);
		ziliaoField = field;
		this.setTextColor(Tool.getTextColor(context));
		
		if("未下载".equals(field.getStrValue("status"))){
			String text;
			if("".equals(field.getStrValue("str3"))){	//大小
				text = "  " + field.getStrValue("AttachmentName")
				+ field.getStrValue("AttachmentType")+"    "+field.getStrValue("status")+"";
			}else{
				text = "  " + field.getStrValue("AttachmentName")
				+ field.getStrValue("AttachmentType")+" ("+field.getStrValue("str3")+")"+"    "+field.getStrValue("status")+"";
			}
			
			//设置字体样式
			Spannable s = new SpannableString(text);
			s.setSpan(new ForegroundColorSpan(Color.RED), text.length()-3, text.length(), 
	                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			
			this.setText(s);
		}else{
			if("".equals(field.getStrValue("str3"))){	//大小
				this.setText("  " + field.getStrValue("AttachmentName")
				+ field.getStrValue("AttachmentType")+"    "+field.getStrValue("status")+"");
			}else{
				this.setText("  " + field.getStrValue("AttachmentName")
				+ field.getStrValue("AttachmentType")+" ("+field.getStrValue("str3")+")"+"    "+field.getStrValue("status")+"");
			}
		}
		
		this.setTextSize(14);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setPadding(Tool.dip2px(getContext(), 10), 1,
				Tool.dip2px(getContext(), 10), 1);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 70));
		layoutParams.bottomMargin = 1;
		this.setLayoutParams(layoutParams);
		this.setBackgroundResource(R.drawable.round_white);

		Drawable arrow=getDrawable(2);
		arrow.setBounds(0, 0, Tool.dip2px(context, 20), Tool.dip2px(context, 20));
		
//		Drawable tubiao=getDrawable(10);
//		tubiao.setBounds(0, 0, Tool.dip2px(context, 40), Tool.dip2px(context, 40));
		
		this.setCompoundDrawables(null, null, arrow, null);
	}
	
	private int getH(int dip)
	{
		return Tool.dip2px(getContext(), dip);
	}
	
	private void highlight(int start, int end)
	{
		SpannableStringBuilder spannable = new SpannableStringBuilder(getText().toString());// 用于可变字符串
		spannable.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new RelativeSizeSpan(0.6f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setText(spannable);
	}
	
	private Drawable getDrawable(int type)
	{
		if (type == 0)
			return getResources().getDrawable(R.drawable.checkbox_off);
		else if (type == 1)
			return getResources().getDrawable(R.drawable.checkbox_click2);
		else
			return getResources().getDrawable(R.drawable.arrow);
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 50));
		layoutParams.topMargin=1;
		return layoutParams;
	}
	
	public void change2Complete()
	{
		this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(1), null, getDrawable(2), null);
		template.setComplete(true);
	}
	
	private void change2pending()
	{
		this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(0), null, getDrawable(2), null);
		template.setComplete(false);
	}
	
	public void changeStatus()
	{
		if (template.isComplete())
			change2pending();
		else
			change2Complete();
	}
	
	public boolean isComplete()
	{
		return template.isComplete();
	}
	
	public String getTemplateType()
	{
		return template.getType();
	}
	
	public String getTemplateValue()
	{
		return template.getValue();
	}
	
	public String getOnlyType()
	{
		return template.getOnlyType() + "";
	}
	
	public int getInputType()
	{
		return template.getInputType();
	}
	
	public String getTemplateName()
	{
		return template.getName();
	}
}
