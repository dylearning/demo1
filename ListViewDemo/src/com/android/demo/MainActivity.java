package com.android.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private List<Map<String, Object>> mDatas = new ArrayList<Map<String, Object>>();

	private int imgIDs[] = { R.drawable.ipod_colours_01, R.drawable.ipod_colours_02, R.drawable.ipod_colours_03, R.drawable.ipod_colours_04, R.drawable.ipod_colours_05, R.drawable.ipod_colours_06, R.drawable.ipod_colours_07, R.drawable.ipod_colours_08, R.drawable.ipod_colours_09, R.drawable.ipod_colours_10, R.drawable.ipod_colours_11 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();

		initUI();
	}

	public void initUI() {
		ListView listView = (ListView) findViewById(R.id.listView);
		MyAdapter adapter = new MyAdapter(this);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String txt = String.valueOf(mDatas.get(position).get("txt"));

				Toast.makeText(getApplicationContext(), "Click txt = " + txt, Toast.LENGTH_SHORT).show();
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				String txt = String.valueOf(mDatas.get(position).get("txt"));

				Toast.makeText(getApplicationContext(), "LongClick txt = " + txt, Toast.LENGTH_SHORT).show();

				return false;
			}
		});
	}

	private void initData() {

		for (int i = 0; i < imgIDs.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("img", imgIDs[i]);
			map.put("txt", "item" + i);

			mDatas.add(map);
		}
	}

	/****************************************************************************************************/
	static class ViewHolder {
		public ImageView img;
		public TextView txt;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;

		private MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {

			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				holder = new ViewHolder();

				WindowManager wm = (WindowManager) getApplicationContext().getSystemService("window");
				Display display = wm.getDefaultDisplay();
				DisplayMetrics dm = new DisplayMetrics();
				display.getMetrics(dm);
				// int scr_w = dm.widthPixels;
				int scr_h = dm.heightPixels;

				convertView = mInflater.inflate(R.layout.list_item_linear, null);
				AbsListView.LayoutParams param = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, scr_h / 8);
				convertView.setLayoutParams(param);

				holder.img = (ImageView) convertView.findViewById(R.id.imageView);
				holder.txt = (TextView) convertView.findViewById(R.id.textView);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setImageResource((Integer) mDatas.get(position).get("img"));
			holder.txt.setText((String) mDatas.get(position).get("txt"));

			return convertView;
		}
	}
	/****************************************************************************************************/
}