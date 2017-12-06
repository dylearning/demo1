package com.android.demo.mainuidemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.demo.mainuidemo.R;

public class FragmentMovement extends Fragment {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View fragmentView = inflater.inflate(R.layout.fragment_movement, container, false);
		
		return fragmentView;
	}

	@Override
	public void onDestroy() {

		// TODO Auto-generated method stub

		super.onDestroy();
	}

}