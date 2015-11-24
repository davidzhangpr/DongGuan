package markettracker.adapter;

import orient.champion.business.R;

import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.util.Tool;
import android.R.raw;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlanListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private FieldsList list;
	private ViewHolder view;
	private Fields client;
	private Drawable success, fail;

	public PlanListAdapter(Context context, FieldsList list) {
		inflater = LayoutInflater.from(context);
		success = Tool.getDrawable(context, R.drawable.success);
		fail = Tool.getDrawable(context, R.drawable.fail);
		if (list != null)
			this.list = list;
		else
			this.list = new FieldsList();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.cell_plan, null);
			view = new ViewHolder();
			view.tv_client = (TextView) convertView.findViewById(R.id.name);
			view.tv_status = (TextView) convertView.findViewById(R.id.status);
			view.img_status = (ImageView) convertView
					.findViewById(R.id.img_status);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		client = list.getFields(position);
		if (null != client) {
			view.tv_client.setText(client.getStrValue("fullname"));
			view.tv_status.setText(client.getStrValue("status1"));
			if (client.getStrValue("status1").equals("100%")) {
				view.img_status.setImageDrawable(success);
			} else {
				view.img_status.setImageDrawable(fail);
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_client, tv_status;
		ImageView img_status;
	}

	public Fields getFields(int index) {
		return this.list.getFields(index);
	}

}
