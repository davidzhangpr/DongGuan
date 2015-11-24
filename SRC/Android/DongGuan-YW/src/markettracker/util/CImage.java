package markettracker.util;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import markettracker.data.DicData;

public class CImage extends ImageView {
	// private Rpt_Table table;
	private int index;

	private CDeleteButton button;
	
	private DicData dicData;

	public CImage(Context context, CDeleteButton button,int index) {
		super(context);
		this.button = button;
		this.index=index;
		init(context);
	}
	
	public CImage(Context context, DicData dic, OnClickListener listener) {
		super(context);
		dicData = dic;
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		this.setImageBitmap(getBitmap());
		this.setOnClickListener(listener);

	}

	private void init(Context context) {

		// initLine(context, temp);
	}
	public int getIndex()
	{
		return index;
	}
	
	public CDeleteButton getDelete()
	{
		return button;
	}

}
