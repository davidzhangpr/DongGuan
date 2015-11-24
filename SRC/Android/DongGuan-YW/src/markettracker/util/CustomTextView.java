package markettracker.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import markettracker.data.UIItem;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CustomTextView extends TextView {

	public CustomTextView(Context context, UIItem item) {
		super(context);
		this.setGravity(item.getGravity());
		this.setTextColor(Tool.getTitleTextColor(context));
		this.setText(item.getCaption() + ":");
		this.setPadding(5, 0, 0, 0);
		this.setMinimumHeight(Tool.dip2px(getContext(), 40));
		this.setTextSize(getTextSize(item));
		this.setLayoutParams(getCurLayoutParams(item));
	}
	
	public CustomTextView(Context context, UIItem item, Activity activity) {
		super(context);
		this.setGravity(item.getGravity());
		this.setTextColor(Tool.getTitleTextColor(context));
		this.setText(item.getCaption() + ":");
		this.setPadding(5, 0, 0, 0);
		this.setTextSize(14);
		this.setLayoutParams(getCurLayoutParams(item));
	
		
//		highlight(0,1);
	}
	
	private float getTextSize(UIItem item) {
//		float font = item.getTitleWidth()
//				/ ((item.getCaption() + ":").length()+2);
//		if (font > Tool.getTitleTextSize())
//			font = Tool.getTitleTextSize();
//		else if (font < 10) {
//			font = 10;
//		}
//		return font;
		return 16.0f;
	}

	private LayoutParams getCurLayoutParams(UIItem item) {
		LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.width = item.getTitleWidth();
		return layoutParams;
	}
	
	@SuppressWarnings("unused")
	private void highlight(int start,int end){  
        SpannableStringBuilder spannable=new SpannableStringBuilder(getText().toString());//用于可变字符串  
        ForegroundColorSpan span=new ForegroundColorSpan(Color.RED);  
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        setText(spannable);  
    } 
	@SuppressWarnings("unused")
	private void underline(int start,int end){  
        SpannableStringBuilder spannable=new SpannableStringBuilder(getText().toString());  
        CharacterStyle span=new UnderlineSpan();  
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        setText(spannable);  
    }  
	@SuppressWarnings("unused")
	private void highlight(String target){  
        String temp=getText().toString();  
        SpannableStringBuilder spannable = new SpannableStringBuilder(temp);  
        CharacterStyle span=null;  
          
        Pattern p = Pattern.compile(target);  
        Matcher m = p.matcher(temp);  
        while (m.find()) {  
            span = new ForegroundColorSpan(Color.RED);//需要重复！  
            spannable.setSpan(span, m.start(), m.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        }  
        setText(spannable);  
    }  
}
