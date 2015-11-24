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

public class QJListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private FieldsList list;
	private ViewHolder view;
	private Fields client;
	private Drawable success, fail;

	public QJListAdapter(Context context, FieldsList list) {
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
			convertView = inflater.inflate(R.layout.cell_qj, null);
			view = new ViewHolder();
			view.tv_name = (TextView) convertView.findViewById(R.id.name);
			view.tv_date = (TextView) convertView.findViewById(R.id.date1);
			view.tv_date1 = (TextView) convertView.findViewById(R.id.date2);
//			view.img_status = (ImageView) convertView
//					.findViewById(R.id.img_status);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		client = list.getFields(position);
		if (null != client) {
			view.tv_name.setText("请假类型:"+client.getStrValue("name"));
			view.tv_date.setText("请假时间:"+client.getStrValue("date"));
			view.tv_date1.setText(client.getStrValue("date1"));
//			if (client.getStrValue("status1").equals("100%")) {
//				view.img_status.setImageDrawable(success);
//			} else {
//				view.img_status.setImageDrawable(fail);
//			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_name, tv_date,tv_date1;
//		ImageView img_status;
	}

	public Fields getFields(int index) {
		return this.list.getFields(index);
	}

}
