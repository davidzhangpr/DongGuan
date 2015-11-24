package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.Sqlite;
import orient.champion.business.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MsgDetailBuilder extends Builder {

	public MsgDetailBuilder(final Context context, final Handler handler,
			final Fields data) {
		super(context);
		this.setIcon(android.R.drawable.ic_dialog_info);

		init(context, handler, data);

		this.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				if (!data.getStrValue("status").equals("已读")) {
					Sqlite.execSingleSql(context,
							"update t_message_detail set status='已读',issubmit=0 where serverid ='"
									+ data.getStrValue("serverid") + "'");
					data.put("status", "已读");
					handler.sendMessage(handler.obtainMessage(1, ""));
				}
			}
		});
	}

	private void init(Context context, Handler handler, Fields data) {
		View view = LayoutInflater.from(context).inflate(R.layout.msg_detail,
				null);
		TextView txt_title = (TextView) view.findViewById(R.id.msg_title);
		txt_title.setText(data.getStrValue("title"));

		TextView txt_sendboy = (TextView) view.findViewById(R.id.msg_sendboy);
		txt_sendboy.setText(data.getStrValue("sender"));

		TextView txt_date = (TextView) view.findViewById(R.id.msg_date);

		txt_date.setText(data.getStrValue("stime"));

		TextView txt_content = (TextView) view.findViewById(R.id.msg_content);
		txt_content.setText("内容:\r\n    " + data.getStrValue("content"));

		this.setView(view);
	}

}
