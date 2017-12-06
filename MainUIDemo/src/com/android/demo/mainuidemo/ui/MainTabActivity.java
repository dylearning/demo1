package com.android.demo.mainuidemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.demo.mainuidemo.R;
import com.android.demo.mainuidemo.widget.CareViewPager;

public class MainTabActivity extends FragmentActivity implements OnClickListener {

	private CareViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	 
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	private LinearLayout mTabLayoutParentsLocal;
	private LinearLayout mTabLayoutMovement;
	private LinearLayout mTabLayoutRemoteControl;
	private LinearLayout mTabLayoutSettings;
	private LinearLayout mTabLayoutAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//getActivity = null
		if (savedInstanceState != null) {
			savedInstanceState.putParcelable("android:support:fragments", null);
		}			
		
		super.onCreate(savedInstanceState);
			
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main_tab_activity);
		mViewPager = (CareViewPager) findViewById(R.id.id_viewpager);

		mViewPager.setOffscreenPageLimit(4);
		
		initView();

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}
  
			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);

		mViewPager.enabledSlide = true;//是否支持滑动
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			private int currentIndex;

			@Override
			public void onPageSelected(int position) {
				resetTabBtn();
				switch (position) {
				case 0:
					((ImageButton) mTabLayoutParentsLocal.findViewById(R.id.tab_bottom_btn_parents_local)).setImageResource(R.drawable.main_tab_button_parents_local_pressed);
					break;
				case 1:
					((ImageButton) mTabLayoutMovement.findViewById(R.id.tab_bottom_btn_movement)).setImageResource(R.drawable.main_tab_button_movement_pressed);
					break;
				case 2:
					((ImageButton) mTabLayoutRemoteControl.findViewById(R.id.tab_bottom_btn_remote_control)).setImageResource(R.drawable.main_tab_button_remote_control_pressed);
					break;
				case 3:
					((ImageButton) mTabLayoutSettings.findViewById(R.id.tab_bottom_btn_setting)).setImageResource(R.drawable.main_tab_button_setting_pressed);
					break;
				case 4:
					((ImageButton) mTabLayoutAbout.findViewById(R.id.tab_bottom_btn_about)).setImageResource(R.drawable.main_tab_button_about_pressed);
					break;					
				}

				currentIndex = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		//String packageDir = "/data/data/" + getPackageName();
		//int sdkVersion = android.os.Build.VERSION.SDK_INT;
		//new AppUninstall().uninstall(packageDir, sdkVersion);
		
	}

	protected void resetTabBtn() {
		((ImageButton) mTabLayoutParentsLocal.findViewById(R.id.tab_bottom_btn_parents_local)).setImageResource(R.drawable.main_tab_button_parents_local_normal);
		((ImageButton) mTabLayoutMovement.findViewById(R.id.tab_bottom_btn_movement)).setImageResource(R.drawable.main_tab_button_movement_normal);
		((ImageButton) mTabLayoutRemoteControl.findViewById(R.id.tab_bottom_btn_remote_control)).setImageResource(R.drawable.main_tab_button_remote_control_normal);
		((ImageButton) mTabLayoutSettings.findViewById(R.id.tab_bottom_btn_setting)).setImageResource(R.drawable.main_tab_button_setting_normal);
		((ImageButton) mTabLayoutAbout.findViewById(R.id.tab_bottom_btn_about)).setImageResource(R.drawable.main_tab_button_about_normal);
	}

	private void initView() {
		mTabLayoutParentsLocal = (LinearLayout) findViewById(R.id.tab_bottom_layout_parents_local);
		mTabLayoutMovement = (LinearLayout) findViewById(R.id.tab_bottom_layout_movement);
		mTabLayoutRemoteControl = (LinearLayout) findViewById(R.id.tab_bottom_layout_remote_control);
		mTabLayoutSettings = (LinearLayout) findViewById(R.id.tab_bottom_layout_setting);
		mTabLayoutAbout = (LinearLayout) findViewById(R.id.tab_bottom_layout_about);

		FragmentGetLocal mFragmentGetLocal = new FragmentGetLocal();
		FragmentMovement mFragmentFragmentMovement = new FragmentMovement();
		FragmentRemoteControl mFragmentRemoteControl = new FragmentRemoteControl();
		FragmentSetting mFragmentSetting = new FragmentSetting();
		FragmentAbout mFragmentAbout = new FragmentAbout();
		mFragments.add(mFragmentGetLocal);
		mFragments.add(mFragmentFragmentMovement);
		mFragments.add(mFragmentRemoteControl);
		mFragments.add(mFragmentSetting);
		mFragments.add(mFragmentAbout);
		
		mTabLayoutParentsLocal.setOnClickListener(this);
		mTabLayoutMovement.setOnClickListener(this);
		mTabLayoutRemoteControl.setOnClickListener(this);
		mTabLayoutSettings.setOnClickListener(this);
		mTabLayoutAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tab_bottom_layout_parents_local:
			mViewPager.setCurrentItem(0,false);
			break;
		case R.id.tab_bottom_layout_movement:                                                                                                                 
			mViewPager.setCurrentItem(1,false);
			break;
		case R.id.tab_bottom_layout_remote_control:
			mViewPager.setCurrentItem(2,false);
			break;
		case R.id.tab_bottom_layout_setting:
			mViewPager.setCurrentItem(3,false);
			break;
		case R.id.tab_bottom_layout_about:
			mViewPager.setCurrentItem(4,false);
			break;
		default:
			break;
		}
	}
}
