/*
* Copyright (C) 2013 linuxonandroid.org
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.zpwebsites.linuxonandroid.opensource;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.TextView;


import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingListActivity;

public class ListBaseActivity extends SlidingListActivity {

	private int mTitleRes;
	protected ListFragment mFrag;

	public ListBaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		//sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindWidth(400);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		TextView txt_Installview = (TextView) findViewById(R.id.Installtxtview);
    	txt_Installview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Install_Guides.class);
    			v.getContext().startActivity(intent);
			}
    		
    	});
    	TextView txt_Launchtxtview = (TextView) findViewById(R.id.Launchtxtview);
    	txt_Launchtxtview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Launcher.class);
    			v.getContext().startActivity(intent);
			}
    		
    	});
    	TextView txt_Tipstxtview = (TextView) findViewById(R.id.Tipstxtview);
    	txt_Tipstxtview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Tips.class);
    			v.getContext().startActivity(intent);
			}
    		
    	});
    	TextView txt_FAQtxtview = (TextView) findViewById(R.id.FAQtxtview);
    	txt_FAQtxtview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), FAQ.class);
    			v.getContext().startActivity(intent);
			}
    		
    	});
    	TextView txt_Changelogtxtview = (TextView) findViewById(R.id.Changelogtxtview);
    	txt_Changelogtxtview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Change_logs.class);
    			v.getContext().startActivity(intent);
			}
    		
    	});
    	TextView txt_Newstxtview = (TextView) findViewById(R.id.Newstxtview);
    	txt_Newstxtview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), News.class);
    			v.getContext().startActivity(intent);
			}
    		
    	});
    	TextView txt_Aboutstxtview = (TextView) findViewById(R.id.Aboutstxtview);
    	txt_Aboutstxtview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), About.class);
    			v.getContext().startActivity(intent);
			}
    		
    	});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
