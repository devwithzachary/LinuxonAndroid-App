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

//TODO: Add a handler for the real back key on the device so the configs are saved too!

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AutoRun_ConfigEditor extends BaseActivity {

	@SuppressWarnings("unused")
	private static String				TAG							= "Complete Linux Installer";		// Used when logging as app name
	@SuppressWarnings("unused")
	private static String				NAME						= "AutoRun_ConfigEditor";			// Used as activity name when logging

	private static final String			PREFS_NAME					= "Preferences";	// If changed don't forget to change the OnBootReciever too!
	private static final String			cfg_AutoBoot_Image			= "AutoBoot_Image";	// If changed don't forget to change the OnBootReciever too!
	private static final String			cfg_AutoBoot_On				= "AutoBoot_On";	// If changed don't forget to change the OnBootReciever too!

	private static Button				btn_Back					= null;
	private static CheckBox				chk_AutoRunOn				= null;
	private static EditText				txt_AutoRunImageName		= null;
	private static LinearLayout			image_input_layout			= null;

	private String						imageName					= null;

	private SharedPreferences			prefs;
	private SharedPreferences.Editor	prefsEditor;


	@Override public void onCreate(Bundle savedInstanceState) {
		mTitleRes = R.string.title_ConfigureAutoRun;
		super.onCreate(savedInstanceState);

		getBundle();

		setContentView(R.layout.autorun_configeditor);
//		setTitle(R.id.title_text, this.getString(R.string.title_ConfigureLinux) + " " + linuxName);
		setSlidingActionBarEnabled(false);
		
		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener(btn_Back_onClick);

		chk_AutoRunOn = (CheckBox) findViewById(R.id.chk_AutoRunOn);
		chk_AutoRunOn.setOnCheckedChangeListener(chk_AutoRunOn_OnCheckedChange);

		txt_AutoRunImageName = (EditText) findViewById(R.id.txt_AutoRunImageName);
		image_input_layout = (LinearLayout) findViewById(R.id.image_input_layout);

        prefs = getSharedPreferences(PREFS_NAME, 0);

		load_Config();
		update_Layot();
}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			save_Config();
			finish();
		}
	};

	private OnCheckedChangeListener chk_AutoRunOn_OnCheckedChange = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			update_Layot();
		}
	};

	private void update_Layot() {
		if (chk_AutoRunOn.isChecked()) {
			image_input_layout.setVisibility(View.VISIBLE);
		} else {
			image_input_layout.setVisibility(View.GONE);
		}
	}

	private void load_Config() {
		imageName = prefs.getString(cfg_AutoBoot_Image, imageName);
		chk_AutoRunOn.setChecked(prefs.getBoolean(cfg_AutoBoot_On, false));

		txt_AutoRunImageName.setText(imageName);
	}

	private void save_Config() {
		// TODO: Check if image file exists and offer the user to "cancel" or "keep anyway" if not.
		prefsEditor = prefs.edit();
		prefsEditor.putString(cfg_AutoBoot_Image, txt_AutoRunImageName.getText().toString());
		prefsEditor.putBoolean(cfg_AutoBoot_On, chk_AutoRunOn.isChecked());
		prefsEditor.commit();
	}

	private void getBundle() {
		// Read the log file name from the extras
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) { // Bundle was passed so we read it
//			linuxName	= bundle.getString("Name");
			imageName	= bundle.getString("Image");

		} else { // Bundle is null
			imageName = "";
//			Log.e (TAG, NAME + ": No bundle was passed to the intent!");
//			finish();
		}

//		if (imageName == null) {
//			Log.e (TAG, NAME + ": No image name was passed to the intent or it was null!");
//			finish();
//		}
	}

}
