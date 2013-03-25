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

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//Used for the debian widget 
public class Debian_Activity extends Activity {

	public static final String			PREFS_NAME			= "DebianWidgetPrefs";
	public static final String			cfg_Image			= "DebianImage";

	private SharedPreferences			prefs;
	private SharedPreferences.Editor	prefsEditor;

	private static	EditText 			txt_ImagePath;
	private static	Button 				btn_SaveConfig;


	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (file_Exists(CFG.MNT + "/home/")) { // A image is already mounted, offer user to chroot into it now
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						String mountedImage = ""; // I need to find a good way to get the mounted image name here, but it works without.
						String cmdString = "su \n";
						cmdString += CFG.busyBoxPath + " chroot " + CFG.MNT + " /root/init.sh " + mountedImage + "\n";

						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.setComponent(new ComponentName("jackpal.androidterm", "jackpal.androidterm.RemoteInterface"));
						intent.setAction("jackpal.androidterm.RUN_SCRIPT");
						intent.putExtra("jackpal.androidterm.iInitialCommand", cmdString);
						startActivity(intent);
						finish();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						toast(getApplicationContext().getString(R.string.launcer_StartAborted));
						finish();
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(Debian_Activity.this);
			builder.setMessage(R.string.launcer_ImageAlreadyMounted).setPositiveButton(R.string.dialog_button_yes, dialogClickListener).setNegativeButton(R.string.dialog_button_no, dialogClickListener).show();
			return;
		}

        prefs = getSharedPreferences(PREFS_NAME, 0);

		if (prefs.contains(cfg_Image) && file_Exists(prefs.getString(cfg_Image, ""))) {
			startLinux();
			finish();
		}

		setContentView(R.layout.widget_config);

		txt_ImagePath = (EditText) this.findViewById(R.id.txt_ImagePath);

		btn_SaveConfig = (Button) this.findViewById(R.id.btn_SaveConfig);
		btn_SaveConfig.setOnClickListener(btn_SaveConfig_Onclick);
	}

	private OnClickListener btn_SaveConfig_Onclick = new OnClickListener() {
		public void onClick(View v) {
			if (!file_Exists(txt_ImagePath.getText().toString())) {
				toast(getApplicationContext().getString(R.string.widget_toast_image_not_found));
				return;
			}

			prefsEditor = prefs.edit();
			prefsEditor.putString(cfg_Image, txt_ImagePath.getText().toString());
			prefsEditor.commit();

			startLinux();
            finish();
		}
	};

	private void startLinux() {
		String image_Name = prefs.getString(cfg_Image, "error");
		String selPath = image_Name.substring(0, image_Name.lastIndexOf("/"));

		String cmdString  = "cd " + selPath + "\n";
		cmdString += "su \n";
		cmdString += "sh " + CFG.scriptPath + " " + image_Name;

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setComponent(new ComponentName("jackpal.androidterm", "jackpal.androidterm.RemoteInterface"));
		intent.setAction("jackpal.androidterm.RUN_SCRIPT");
		intent.putExtra("jackpal.androidterm.iInitialCommand", cmdString);
		startActivity(intent);
	}

	private boolean file_Exists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public void toast (String msg) {
		Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	}

}
