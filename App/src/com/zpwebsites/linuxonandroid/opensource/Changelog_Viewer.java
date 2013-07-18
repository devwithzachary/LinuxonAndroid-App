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
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Changelog_Viewer extends BaseActivity {
	private static String	TAG		= "Complete Linux Installer";		// Used when logging as app name
	private static String	NAME 	= "Changelog_Viewer";				// Used as activity name when logging

	private String		logName			= null;


	public void onCreate(Bundle savedInstanceState) {
		mTitleRes = R.string.frontpage_btn_Changelog;
		super.onCreate(savedInstanceState);
		setContentView (R.layout.changelog_viewer);
		setSlidingActionBarEnabled(false);
		
		getLogName();
		viewChangeLog();
	}

	private void getLogName() {
		// Read the log file name from the extras
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) { // Bundle was passed so we read it
			logName = bundle.getString("logName");

		} else { // Bundle is null
			Log.e (TAG, NAME + ": No bundle was passed to the intent!");
			finish();
		}

		if (logName == null) { // Check if log file name is null
			Log.e (TAG, NAME + ": No log file was passed to the intent or it was null!");
			finish();
		}
	}

	private void viewChangeLog() {
		TextView txt_LogView = (TextView) findViewById(R.id.txt_LogView);

		try {
			InputStreamReader reader = new InputStreamReader(getAssets().open(logName), "UTF-8");
			BufferedReader br = new BufferedReader(reader); 

			String line, text = "";
			while ((line = br.readLine()) != null) {
				if (line.equals("")) {
					text = text + "\n";
				} else {
					text = text + line + "\n";
				}
			}

			txt_LogView.setText(text);

			br.close();
			reader.close();

		} catch (IOException e) {
			Log.e (TAG, NAME + ": Unable to read file! (" + logName + ")");
			Log.e (TAG, e.getMessage());
			finish();
		}
	}
}
