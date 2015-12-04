package markettracker.adapter;

import orient.champion.business.R;

import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.util.Tool;
import android.R.raw;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private FieldsList list;
	private ViewHolder view;
	private Fields client;
	private Drawable success, fail;

	public MsgListAdapter(Context context, FieldsList list) {
		inflater = LayoutInflater.from(context);
		success = Tool.getDrawable(context, R.drawable.success);
		fail = Tool.getDrawable(context, R.drawable.fail);
		if (list != null)
			this.list = list;
		else
			this.list = new FieldsList();
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.cell_msg, null);
			view = new ViewHolder();
			view.tv_client = (TextView) convertView.findViewById(R.id.name);
			view.tv_status = (TextView) convertView.findViewById(R.id.status);
			view.tv_date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		client = list.getFields(position);
		if (null != client) {
			view.tv_client.setText(client.getStrValue("title"));
			view.tv_status.setText(client.getStrValue("status"));
			view.tv_date.setText(client.getStrValue("stime"));
			
			view.tv_client.setTextColor(Color.BLACK);
			view.tv_date.setTextColor(Color.BLACK);
			
			if("未读".equals(client.getStrValue("status"))){
				view.tv_status.setTextColor(Color.RED);
			}else{
				view.tv_status.setTextColor(Color.BLACK);
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_client, tv_status,tv_date;
	}

	public Fields getFields(int index) {
		return this.list.getFields(index);
	}

}
