package me.yeso.yeelink.util;

import java.text.DecimalFormat;
import java.util.List;

import me.yeso.yeelink.base.Device;
import me.yeso.yeelink.client.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DevLsAdapter extends BaseAdapter {
	
	private Context context;
	private List<Device> list;
	private TextView tvDevNum;
	private TextView tvDevTitle;
	private TextView tvDevAbout;
	
	
	
	public DevLsAdapter(Context context,List<Device> list) {
		super();
		this.list = list;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=View.inflate(context, R.layout.device_item, null);
		tvDevNum=(TextView)convertView.findViewById(R.id.dev_num);
		tvDevTitle=(TextView)convertView.findViewById(R.id.dev_title);
		tvDevAbout=(TextView)convertView.findViewById(R.id.dev_about);
		tvDevNum.setText((new DecimalFormat("000").format(position+1)));
		tvDevTitle.setText(list.get(position).getTitle());
		tvDevAbout.setText(list.get(position).getAbout());
		return convertView;
	}

}
