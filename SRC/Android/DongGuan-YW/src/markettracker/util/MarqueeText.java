package markettracker.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeText extends TextView {
	public MarqueeText(Context con) {
		super(con);
		this.setSingleLine();
		this.setMarqueeRepeatLimit(-1);
		this.setFocusable(true);
		this.setEllipsize(android.text.TextUtils.TruncateAt.valueOf("MARQUEE"));
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}
