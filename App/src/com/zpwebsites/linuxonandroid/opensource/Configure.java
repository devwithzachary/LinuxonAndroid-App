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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Configure extends BaseActivity {

	private static String		TAG							= "Complete Linux Installer";		// Used when logging as app name
	private static String		NAME						= "Configure";						// Used as activity name when logging

	private static Button		btn_Back					= null;
	private static Button		btn_Config_Mounts			= null;

	private static CheckBox		chk_RunSSH					= null;
	private static CheckBox		chk_RunVNC					= null;
	private static CheckBox		chk_SWAP					= null;

	private static EditText		txt_ScreenWidth				= null;
	private static EditText		txt_ScreenHeight			= null;

	private static LinearLayout	runVNC_Resolution_Width		= null;
	private static LinearLayout	runVNC_Resolution_Height	= null;

	private String				linuxName					= null;
	private String				imageName					= null;


	@Override public void onCreate(Bundle savedInstanceState) {
		mTitleRes = R.string.title_ConfigureLinux;
		super.onCreate(savedInstanceState);

		getBundle();

		setContentView(R.layout.configure);
		setSlidingActionBarEnabled(false);
		
		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener(btn_Back_onClick);

		btn_Config_Mounts= (Button) findViewById(R.id.btn_Config_Mounts);
		btn_Config_Mounts.setOnClickListener(btn_Config_Mounts_onClick);

		chk_RunSSH = (CheckBox) findViewById(R.id.chk_RunSSH);
		chk_SWAP = (CheckBox) findViewById(R.id.chk_swap);

		chk_RunVNC = (CheckBox) findViewById(R.id.chk_RunVNC);
		chk_RunVNC.setOnCheckedChangeListener(chk_RunVNC_OnCheckedChange);

		txt_ScreenWidth = (EditText) findViewById(R.id.txt_ScreenWidth);
		txt_ScreenHeight = (EditText) findViewById(R.id.txt_ScreenHeight);

		runVNC_Resolution_Width = (LinearLayout) findViewById(R.id.runVNC_Resolution_Width);
		runVNC_Resolution_Height = (LinearLayout) findViewById(R.id.runVNC_Resolution_Height);

		load_Config();
		update_Layot();
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			save_Config();
			finish();
		}
	};

	private OnClickListener btn_Config_Mounts_onClick = new OnClickListener() {
		public void onClick(View v) {
			save_Config();

			Bundle bundle = new Bundle();
			bundle.putString("Name", linuxName);
			bundle.putString("Image", imageName);

			Intent intent = new Intent(v.getContext(), Mounts_Editor.class);
			intent.putExtras(bundle);
			v.getContext().startActivity(intent);
		}
	};

	private OnCheckedChangeListener chk_RunVNC_OnCheckedChange = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			update_Layot();
		}
	};

	private void update_Layot() {
		if (chk_RunVNC.isChecked()) {
			runVNC_Resolution_Width.setVisibility(View.VISIBLE);
			runVNC_Resolution_Height.setVisibility(View.VISIBLE);
		} else {
			runVNC_Resolution_Width.setVisibility(View.GONE);
			runVNC_Resolution_Height.setVisibility(View.GONE);
		}
	}

	private void load_Config() {
		File file = new File(imageName + ".config");
		if (!file.exists()) {
			chk_RunSSH.setChecked(false);
			chk_SWAP.setChecked(false);
			chk_RunVNC.setChecked(true);
			txt_ScreenWidth.setText("800");
			txt_ScreenHeight.setText("450");
			return;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			String value = null;

			while ((line = reader.readLine()) != null) {
				value = line.substring(line.indexOf("=") + 1);
				if (line.toLowerCase().startsWith("run_ssh")) {
					chk_RunSSH.setChecked(value.toLowerCase().equals("yes"));

				} else if (line.toLowerCase().startsWith("use_swap")) {
					chk_SWAP.setChecked(value.toLowerCase().equals("yes"));

				}
				
				else if (line.toLowerCase().startsWith("run_vnc")) {
					chk_RunVNC.setChecked(value.toLowerCase().equals("yes"));

				} else if (line.toLowerCase().startsWith("resolution")) {
					if (line.contains("x")) {
						txt_ScreenWidth.setText(value.substring(0, value.indexOf("x")));
						txt_ScreenHeight.setText(value.substring(value.indexOf("x") + 1));
					} else {
						txt_ScreenWidth.setText("");
						txt_ScreenHeight.setText("");
					}
				}
			}

		} catch (IOException e) {
			Log.e(TAG, NAME + ": load_Config: " + e.getMessage());

		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				Log.e(TAG, NAME + ": load_Config: " + e.getMessage());
			}
		}
	}

	private void save_Config() {
		file_Delete(imageName + ".config");
		try {
			FileWriter outFile = new FileWriter(imageName + ".config");
			PrintWriter out = new PrintWriter(outFile);

			if (chk_RunSSH.isChecked()) {
				out.println("run_ssh=yes");
			} else {
				out.println("run_ssh=no");
			}
			if (chk_SWAP.isChecked()) {
				out.println("use_swap=yes");
			} else {
				out.println("use_swap=no");
			}
			if (chk_RunVNC.isChecked()) {
				out.println("run_vnc=yes");
			} else {
				out.println("run_vnc=no");
			}

			out.println("resolution=" + txt_ScreenWidth.getText().toString() + "x" + txt_ScreenHeight.getText().toString());

			out.close();

		} catch (IOException e){
			Log.e(TAG, NAME + ": save_Config: Save Error " + e.getMessage());
		}
	}

	private void file_Delete(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			if (!file.delete()) {
				Log.e(TAG, NAME + ": Error deleting " + fileName);
			}
		}
	}

	private void getBundle() {
		// Read the log file name from the extras
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) { // Bundle was passed so we read it
			linuxName	= bundle.getString("Name");
			imageName	= bundle.getString("Image");

		} else { // Bundle is null
			Log.e (TAG, NAME + ": No bundle was passed to the intent!");
			finish();
		}

		if (imageName == null) {
			Log.e (TAG, NAME + ": No image name was passed to the intent or it was null!");
			finish();
		}
	}
}
