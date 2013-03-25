/*
 * Copyright (C) 2012 linuxonandroid.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zpwebsites.linuxonandroid.opensource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TextViewer extends DashboardActivity {

	private static String	TAG				= "Complete Linux Installer";		// Used when logging as app name
	private static String	NAME 			= "TextViewer";						// Used as activity name when logging

	private Button			btn_Back		= null;
	private Button			btn_Next		= null;
	private Button			btn_Finish		= null;
	private TextView		text_view		= null;

	private String			fileExt			= ".txt";
	private String			baseName		= null;
	private int				currentPage		= 0;
	private int				pageCount		= 0;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.textviewer_layout);
		setTitleFromActivityLabel (R.id.title_text);

		getBundle();

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Next = (Button) findViewById(R.id.btn_Next);

		btn_Back.setOnClickListener(btn_Back_onClick);
		btn_Next.setOnClickListener(btn_Next_onClick);

		btn_Finish = (Button) findViewById(R.id.btn_Finish);
		btn_Finish.setOnClickListener(btn_Finish_onClick);

		text_view = (TextView) findViewById(R.id.text_view);

		viewText();
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			if (currentPage == 1) {
				finish();

			} else if (currentPage > 1) {
				currentPage--;
				viewText();
			}
		}
	};

	private OnClickListener btn_Next_onClick = new OnClickListener() {
		public void onClick(View v) {
			if  (currentPage < pageCount) {
				currentPage++;
				viewText();
			}
		}
	};

	private void getBundle() {
		// Read the log file name from the extras
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) { // Bundle was passed so we read it
			baseName = bundle.getString("FileName");
			pageCount = bundle.getInt("Pages");
			currentPage = 1;

		} else { // Bundle is null
			Log.e (TAG, NAME + ": No bundle was passed to the intent!");
			finish();
		}

		if (baseName == null) { // Check if log file name is null
			Log.e (TAG, NAME + ": No log file was passed to the intent or it was null!");
			finish();
		}
	}

	private OnClickListener btn_Finish_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), HomeActivity.class);
			v.getContext().startActivity(intent);
		}
	};

	private void viewText() {
		btn_Next.setVisibility(View.GONE);
		btn_Finish.setVisibility(View.GONE);

		if (currentPage == pageCount) {
			btn_Finish.setVisibility(View.VISIBLE);
		} else {
			btn_Next.setVisibility(View.VISIBLE);
		}

		if (pageCount < 2) {
			btn_Next.setVisibility(View.GONE);
			btn_Finish.setVisibility(View.GONE);
		}

		InputStreamReader reader = null;
		try {
			if (pageCount < 2) {
				reader = new InputStreamReader(getAssets().open(baseName), "UTF-8");
			} else {
				reader = new InputStreamReader(getAssets().open(baseName + currentPage + fileExt), "UTF-8");
			}

			BufferedReader br = new BufferedReader(reader); 

			String line, text = "";
			while ((line = br.readLine()) != null) {
				if (line.equals("")) {
					text = text + "\n";
				} else {
					text = text + line + "\n";
				}
			}

			text_view.setText(text);

			br.close();
			reader.close();

		} catch (IOException e) {
			Log.e (TAG, NAME + ": Unable to read file!");
			Log.e (TAG, e.getMessage());
			finish();
		}
	}

}
