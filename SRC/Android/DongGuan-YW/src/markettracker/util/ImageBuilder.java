package markettracker.util;

import markettracker.data.Fields;
import orient.champion.business.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class ImageBuilder extends Builder {

	public ImageBuilder(final Context context, final Fields data) {
		super(context);
		this.setIcon(android.R.drawable.ic_dialog_info);

		init(context, data);

		this.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
	}
	
	private void init(Context context, Fields data) {
		View view = LayoutInflater.from(context).inflate(R.layout.imageview,
				null);
		ImageView image = (ImageView) view.findViewById(R.id.image);
		byte[] bytes = Base64.decode(data.getStrValue("photo"), 0);
		
		image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0,
				bytes.length));
		
		this.setView(view);
	}

}
