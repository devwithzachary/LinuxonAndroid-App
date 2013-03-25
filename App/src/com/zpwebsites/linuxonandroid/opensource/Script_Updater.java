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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Script_Updater extends DashboardActivity {

	private static String			TAG						= "Complete Linux Installer";		// Used when logging as app name
	private static String			NAME					= "Script_Updater";					// Used as activity name when logging

	public static final String		PREFS_NAME				= "ScriptPrefs";

	private static float			installed_Boot			= 0f;
	private static float			installed_AutoBoot		= 0f;
	public static float				online_Boot				= 0f;
	public static float				online_AutoBoot			= 0f;

	private static Button			btn_Back				= null;
	private static Button			btn_UpdateBootScript	= null;
	private static Button			btn_UpdateAutoBootScript= null;

	private static TextView			txt_InstalledBoot		= null;
	public static TextView			txt_OnlineBoot			= null;
	private static TextView			txt_InstalledAutoBoot	= null;
	public static TextView			txt_OnlineAutoBoot		= null;

	public static ProgressDialog	mProgressDialog;


	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.script_updater);
		setTitleFromActivityLabel (R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener(btn_Back_onClick);

		btn_UpdateBootScript = (Button) findViewById(R.id.btn_UpdateBootScript);
		btn_UpdateBootScript.setOnClickListener(btn_UpdateBootScript_onClick);

		btn_UpdateAutoBootScript = (Button) findViewById(R.id.btn_UpdateAutoBootScript);
		btn_UpdateAutoBootScript.setOnClickListener(btn_UpdateAutoBootScript_onClick);

		txt_InstalledBoot = (TextView) findViewById(R.id.txt_InstalledBoot);
		txt_OnlineBoot = (TextView) findViewById(R.id.txt_OnlineBoot);
		txt_InstalledAutoBoot = (TextView) findViewById(R.id.txt_InstalledAutoBoot);
		txt_OnlineAutoBoot = (TextView) findViewById(R.id.txt_OnlineAutoBoot);

		mProgressDialog = new ProgressDialog(Script_Updater.this);
		mProgressDialog.setMessage("Downloading.");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		installed_Boot = get_InstalledVersion(CFG.scriptPath);
		installed_AutoBoot = get_InstalledVersion(CFG.scriptPath_AutoBoot);

//		Log.d(TAG,  NAME + ": Installed boot script: " + installed_Boot);
//		Log.d(TAG,  NAME + ": Installed auto boot script: " + installed_AutoBoot);

		txt_InstalledBoot.setText(" V" + installed_Boot);
		txt_InstalledAutoBoot.setText(" V" + installed_AutoBoot);

		get_OnlineVersions();

		txt_OnlineBoot.setText(" V" + online_Boot);
		txt_OnlineAutoBoot.setText(" V" + online_AutoBoot);

//		Log.d(TAG,  NAME + ": Online boot script: " + online_Boot);
//		Log.d(TAG,  NAME + ": Online auto boot script: " + online_AutoBoot);
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	private OnClickListener btn_UpdateBootScript_onClick = new OnClickListener() {
		public void onClick(View v) {
			DownloadFile downloadFile = new DownloadFile();
			downloadFile.execute(CFG.updater_LatestBoot, CFG.scriptPath);
			btn_UpdateBootScript.setEnabled(false);
		}
	};

	private OnClickListener btn_UpdateAutoBootScript_onClick = new OnClickListener() {
		public void onClick(View v) {
			DownloadFile downloadFile = new DownloadFile();
			downloadFile.execute(CFG.updater_LatestAutoboot, CFG.scriptPath_AutoBoot);
			btn_UpdateAutoBootScript.setEnabled(false);
		}
	};

	private Float get_InstalledVersion(String scriptName) {
		File file = new File(scriptName);
		if (!file.exists()) {
			Log.e(TAG, NAME + ": Script not found! " + "(" + scriptName + ")");
			return -1f;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			String ver = null;

			while ((line = reader.readLine()) != null && ver == null) {
				if (line.toLowerCase().contains("$ver:")) {
					String tmp = line.substring(line.indexOf("$ver:") + 5).trim();

					if (tmp.toLowerCase().startsWith("v")) {
						ver = tmp.substring(1, tmp.indexOf(" "));
					} else {
						ver = tmp.substring(0, tmp.indexOf(" "));
					}
				}
			}

			Float verF = Float.valueOf(ver);
			return verF;

		} catch (IOException e) {
			Log.e(TAG, NAME + ": get_InstalledVersions: " + e.getMessage());

		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				Log.e(TAG, NAME + ": get_InstalledVersions: " + e.getMessage());
			}
		}
		return -1f;
	}

	private void get_OnlineVersions() {
		DownloadVersionInfo versionInfo = new DownloadVersionInfo();
		versionInfo.execute(CFG.updater_VersionsFile);
	}

	public static void updateLayout() {
		btn_UpdateBootScript.setEnabled(false);
		btn_UpdateAutoBootScript.setEnabled(false);

		if (online_Boot > installed_Boot) {
			btn_UpdateBootScript.setEnabled(true);
		}

		if (online_AutoBoot > installed_AutoBoot) {
			btn_UpdateAutoBootScript.setEnabled(true);
		}

	}

}
