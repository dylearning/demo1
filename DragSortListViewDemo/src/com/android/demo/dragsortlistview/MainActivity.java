package com.android.demo.dragsortlistview;

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
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;

public class MainActivity extends Activity {

	private List<Map<String, Object>> mDatas = new ArrayList<Map<String, Object>>();

	private MyAdapter adapter;

	private int imgIDs[] = { R.drawable.ipod_colours_01, R.drawable.ipod_colours_02, R.drawable.ipod_colours_03, R.drawable.ipod_colours_04, R.drawable.ipod_colours_05, R.drawable.ipod_colours_06, R.drawable.ipod_colours_07, R.drawable.ipod_colours_08, R.drawable.ipod_colours_09, R.drawable.ipod_colours_10, R.drawable.ipod_colours_11 };

	private String txts[] = { "蓝花苹果", "花苹果", "红苹果", "黄苹果", "绿苹果", "青苹果", "粉苹果", "金苹果", "蓝苹果", "银苹果", "绿苹果" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();

		initUI();
	}

	public void initUI() {

		adapter = new MyAdapter(this);

		DragSortListView mDragSortListView = (DragSortListView) findViewById(R.id.dslvList);
		mDragSortListView.setAdapter(adapter);

		mDragSortListView.setDropListener(onDrop);
		mDragSortListView.setRemoveListener(onRemove);

		mDragSortListView.setOnItemClickListener(onItemClick);

		mDragSortListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				String txt = String.valueOf(mDatas.get(position).get("txt"));

				Toast.makeText(getApplicationContext(), "LongClick txt = " + txt, Toast.LENGTH_SHORT).show();

				return false;
			}
		});

		mDragSortListView.setDragEnabled(true);
	}

	private void initData() {

		for (int i = 0; i < imgIDs.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("img", imgIDs[i]);
			map.put("txt", txts[i]);

			mDatas.add(map);
		}
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				Map<String, Object> item = mDatas.get(from);
				mDatas.remove(from);
				mDatas.add(to, item);
				adapter.notifyDataSetChanged();
			}
		}
	};

	private RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			mDatas.remove(which);
			adapter.notifyDataSetChanged();
		}
	};

	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			String txt = String.valueOf(mDatas.get(position).get("txt"));

			Toast.makeText(getApplicationContext(), "Click txt = " + txt, Toast.LENGTH_SHORT).show();
		}
	};

	/****************************************************************************************************/
	static class ViewHolder {
		public ImageView img;
		public TextView txt;

		public ImageView imgDel;
		public ImageView imgDrag;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;

		private MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		/*
		 * public void remove(int arg0) { mDatas.remove(arg0);
		 * this.notifyDataSetChanged(); }
		 * 
		 * public void insert(Map<String, Object> item, int arg0) {
		 * mDatas.add(arg0, item); this.notifyDataSetChanged(); }
		 */

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
				holder.imgDel = (ImageView) convertView.findViewById(R.id.click_remove);
				holder.imgDrag = (ImageView) convertView.findViewById(R.id.drag_handle);

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