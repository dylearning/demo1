package com.android.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
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
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_vertical);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		//layoutManager.setOrientation(LinearLayoutManager.VERTICAL);// 默认是Vertical，可以不写
		//layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(layoutManager);
		DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
		
		/*recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView布局管理器为4列垂直排布
		DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration(this);*/

		recyclerView.addItemDecoration(itemDecoration);

		MyAdapter adapter = new MyAdapter();
		recyclerView.setAdapter(adapter);
	}

	private void initData() {

		for (int i = 0; i < imgIDs.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("img", imgIDs[i]);
			map.put("txt", "item" + i);

			mDatas.add(map);
		}
	}

	/******************************************************************************************************************************/

	public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

			View view = View.inflate(viewGroup.getContext(), R.layout.list_item_linear, null);//list_item_linear  list_item_relative

			WindowManager wm = (WindowManager) viewGroup.getContext().getApplicationContext().getSystemService("window");
			Display display = wm.getDefaultDisplay();
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			int scr_w = dm.widthPixels;
			int scr_h = dm.heightPixels;
			
			//设置item宽度、高度 一屏8个
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, scr_h / 8);
			view.setLayoutParams(param);

			ViewHolder holder = new ViewHolder(view);
			return holder;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {
			// ViewGroup.LayoutParams params =
			// viewHolder.mTextView.getLayoutParams();// 得到item的LayoutParams布局参数
			// params.height = (int)(200+Math.random()*400);// 把随机的高度赋予item布局
			// viewHolder.mTextView.setLayoutParams(params);// 把params设置给item布局

			// 绑定数据到ViewHolder上
			viewHolder.mTextView.setText((CharSequence) mDatas.get(position).get("txt"));
			viewHolder.mImageView.setImageResource((Integer) mDatas.get(position).get("img"));
		}

		@Override
		public int getItemCount() {
			return mDatas.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {

			public TextView mTextView;
			public ImageView mImageView;

			public ViewHolder(View itemView) {
				super(itemView);
				mTextView = (TextView) itemView.findViewById(R.id.textView);
				mImageView = (ImageView) itemView.findViewById(R.id.imageView);

				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(getApplicationContext(), "item " + getPosition(), Toast.LENGTH_SHORT).show();
					}
				});

				mImageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(getApplicationContext(), "imageView item " + getPosition(), Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
}
